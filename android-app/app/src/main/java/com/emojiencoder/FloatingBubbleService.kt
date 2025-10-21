package com.emojiencoder

import android.app.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat

class FloatingBubbleService : Service() {

    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var encodeWindow: View? = null
    private var decodeWindow: View? = null
    private var isExpanded = false
    private var selectedCarrier = "😀"
    
    // State preservation
    private var encodeInputText = ""
    private var encodeOutputText = ""
    private var decodeInputText = ""
    private var decodeOutputText = ""

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, createNotification(), android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(1, createNotification())
        }
        showFloatingBubble()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "emogic_bubble",
                "EMOGIC Bubble",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "EMOGIC floating bubble is active"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, "emogic_bubble")
            .setContentTitle("EMOGIC Active")
            .setContentText("Tap bubble to encode/decode")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun showFloatingBubble() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "[!] OVERLAY PERMISSION REQUIRED", Toast.LENGTH_LONG).show()
                stopSelf()
                return
            }
        }

        try {
            windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

            floatingView = LayoutInflater.from(this).inflate(R.layout.floating_bubble, null)

            val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.END
                x = 0
                y = 100
            }

            windowManager?.addView(floatingView, params)

            setupBubbleInteraction(params)
        } catch (e: Exception) {
            Toast.makeText(this, "[!] FAILED TO START BUBBLE: ${e.message}", Toast.LENGTH_LONG).show()
            stopSelf()
        }
    }

    private fun setupBubbleInteraction(params: WindowManager.LayoutParams) {
        val bubbleIcon = floatingView?.findViewById<ImageView>(R.id.bubbleIcon)
        val encodeButton = floatingView?.findViewById<Button>(R.id.encodeButton)
        val decodeButton = floatingView?.findViewById<Button>(R.id.decodeButton)
        val closeButton = floatingView?.findViewById<Button>(R.id.closeButton)

        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f

        bubbleIcon?.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = initialX + (initialTouchX - event.rawX).toInt()
                    params.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager?.updateViewLayout(floatingView, params)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (Math.abs(initialTouchX - event.rawX) < 10 && 
                        Math.abs(initialTouchY - event.rawY) < 10) {
                        toggleExpanded()
                    }
                    true
                }
                else -> false
            }
        }

        encodeButton?.setOnClickListener {
            // Close decode if open, show encode
            if (decodeWindow != null) {
                saveDecodeState()
                hideDecodeWindow()
            }
            showEncodeWindow()
            toggleExpanded()
        }

        decodeButton?.setOnClickListener {
            // Close encode if open, show decode
            if (encodeWindow != null) {
                saveEncodeState()
                hideEncodeWindow()
            }
            showDecodeWindow()
            toggleExpanded()
        }

        closeButton?.setOnClickListener {
            stopSelf()
        }
    }

    private fun toggleExpanded() {
        isExpanded = !isExpanded
        val expandedMenu = floatingView?.findViewById<View>(R.id.expandedMenu)
        expandedMenu?.visibility = if (isExpanded) View.VISIBLE else View.GONE
        
        // Update window params to allow clicks when expanded
        floatingView?.let { view ->
            val params = view.layoutParams as WindowManager.LayoutParams
            params.flags = if (isExpanded) {
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            } else {
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            }
            windowManager?.updateViewLayout(view, params)
        }
    }

    private fun showEncodeWindow() {
        if (encodeWindow != null) {
            closeEncodeWindow()
            return
        }

        encodeWindow = LayoutInflater.from(this).inflate(R.layout.floating_encode_window, null)
        
        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        windowManager?.addView(encodeWindow, params)

        val carrierSpinner = encodeWindow?.findViewById<Spinner>(R.id.carrierSpinner)
        val inputText = encodeWindow?.findViewById<EditText>(R.id.inputText)
        val outputText = encodeWindow?.findViewById<TextView>(R.id.outputText)
        val pasteBtn = encodeWindow?.findViewById<Button>(R.id.pasteBtn)
        val clearBtn = encodeWindow?.findViewById<Button>(R.id.clearBtn)
        val copyBtn = encodeWindow?.findViewById<Button>(R.id.copyBtn)
        val closeBtn = encodeWindow?.findViewById<Button>(R.id.closeBtn)
        
        // Restore state
        inputText?.setText(encodeInputText)
        if (encodeOutputText.isNotEmpty() && encodeOutputText != "[OUTPUT]") {
            outputText?.text = encodeOutputText
        }

        // Setup carrier spinner
        val carriers = EmojiLists.EMOJI_LIST + EmojiLists.ALPHABET_LIST.map { it.toString() }
        val adapter = ArrayAdapter(this, R.layout.spinner_item, carriers)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        carrierSpinner?.adapter = adapter
        carrierSpinner?.setSelection(carriers.indexOf(selectedCarrier))
        
        carrierSpinner?.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCarrier = carriers[position]
                // Auto-encode if there's input
                val text = inputText?.text.toString()
                if (text.isNotEmpty()) {
                    val encoded = EmojiEncoder.encode(selectedCarrier, text)
                    outputText?.text = encoded
                }
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        pasteBtn?.setOnClickListener {
            clipboard.primaryClip?.getItemAt(0)?.text?.toString()?.let { text ->
                inputText?.setText(text)
                Toast.makeText(this, "[✓] PASTED!", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "[!] CLIPBOARD EMPTY", Toast.LENGTH_SHORT).show()
            }
        }
        
        clearBtn?.setOnClickListener {
            inputText?.setText("")
            outputText?.text = "[OUTPUT]"
        }

        // Auto-encode as user types
        var typingTimer: Handler? = null
        inputText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                typingTimer?.removeCallbacksAndMessages(null)
                typingTimer = Handler(Looper.getMainLooper())
                typingTimer?.postDelayed({
                    val text = s.toString()
                    if (text.isNotEmpty()) {
                        val encoded = EmojiEncoder.encode(selectedCarrier, text)
                        outputText?.text = encoded
                    } else {
                        outputText?.text = "[OUTPUT]"
                    }
                }, 300)
            }
        })

        copyBtn?.setOnClickListener {
            val output = outputText?.text.toString()
            if (output != "[OUTPUT]" && output.isNotEmpty()) {
                val clip = ClipData.newPlainText("EMOGIC", output)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "[✓] COPIED!", Toast.LENGTH_SHORT).show()
            }
        }

        closeBtn?.setOnClickListener {
            closeEncodeWindow()
        }
    }

    private fun saveEncodeState() {
        encodeWindow?.let {
            encodeInputText = it.findViewById<EditText>(R.id.inputText)?.text.toString()
            encodeOutputText = it.findViewById<TextView>(R.id.outputText)?.text.toString()
        }
    }
    
    private fun hideEncodeWindow() {
        encodeWindow?.let {
            windowManager?.removeView(it)
            encodeWindow = null
        }
    }
    
    private fun closeEncodeWindow() {
        encodeInputText = ""
        encodeOutputText = ""
        hideEncodeWindow()
    }

    private fun showDecodeWindow() {
        if (decodeWindow != null) {
            closeDecodeWindow()
            return
        }

        decodeWindow = LayoutInflater.from(this).inflate(R.layout.floating_decode_window, null)
        
        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        windowManager?.addView(decodeWindow, params)

        val inputText = decodeWindow?.findViewById<EditText>(R.id.inputText)
        val outputText = decodeWindow?.findViewById<TextView>(R.id.outputText)
        val pasteBtn = decodeWindow?.findViewById<Button>(R.id.pasteBtn)
        val clearBtn = decodeWindow?.findViewById<Button>(R.id.clearBtn)
        val copyBtn = decodeWindow?.findViewById<Button>(R.id.copyBtn)
        val closeBtn = decodeWindow?.findViewById<Button>(R.id.closeBtn)
        
        // Restore state
        inputText?.setText(decodeInputText)
        if (decodeOutputText.isNotEmpty() && decodeOutputText != "[OUTPUT]") {
            outputText?.text = decodeOutputText
        }

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        pasteBtn?.setOnClickListener {
            clipboard.primaryClip?.getItemAt(0)?.text?.toString()?.let { text ->
                inputText?.setText(text)
                Toast.makeText(this, "[✓] PASTED!", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(this, "[!] CLIPBOARD EMPTY", Toast.LENGTH_SHORT).show()
            }
        }
        
        clearBtn?.setOnClickListener {
            inputText?.setText("")
            outputText?.text = "[OUTPUT]"
        }

        // Auto-decode as user types
        var typingTimer: Handler? = null
        inputText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                typingTimer?.removeCallbacksAndMessages(null)
                typingTimer = Handler(Looper.getMainLooper())
                typingTimer?.postDelayed({
                    val text = s.toString()
                    if (text.isNotEmpty()) {
                        try {
                            val decoded = EmojiEncoder.decode(text)
                            outputText?.text = decoded
                        } catch (e: Exception) {
                            outputText?.text = "[!] DECODE FAILED"
                        }
                    } else {
                        outputText?.text = "[OUTPUT]"
                    }
                }, 300)
            }
        })

        copyBtn?.setOnClickListener {
            val output = outputText?.text.toString()
            if (output != "[OUTPUT]" && output.isNotEmpty()) {
                val clip = ClipData.newPlainText("EMOGIC", output)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "[✓] COPIED!", Toast.LENGTH_SHORT).show()
            }
        }

        closeBtn?.setOnClickListener {
            closeDecodeWindow()
        }
    }

    private fun saveDecodeState() {
        decodeWindow?.let {
            decodeInputText = it.findViewById<EditText>(R.id.inputText)?.text.toString()
            decodeOutputText = it.findViewById<TextView>(R.id.outputText)?.text.toString()
        }
    }
    
    private fun hideDecodeWindow() {
        decodeWindow?.let {
            windowManager?.removeView(it)
            decodeWindow = null
        }
    }
    
    private fun closeDecodeWindow() {
        decodeInputText = ""
        decodeOutputText = ""
        hideDecodeWindow()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            closeEncodeWindow()
            closeDecodeWindow()
            floatingView?.let { 
                if (it.windowToken != null) {
                    windowManager?.removeView(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
