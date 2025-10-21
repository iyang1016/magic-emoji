package com.emojiencoder

import android.app.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat

class FloatingBubbleService : Service() {

    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var isExpanded = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, createNotification())
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
    }

    private fun setupBubbleInteraction(params: WindowManager.LayoutParams) {
        val bubbleIcon = floatingView?.findViewById<ImageView>(R.id.bubbleIcon)
        val encodeButton = floatingView?.findViewById<View>(R.id.encodeButton)
        val decodeButton = floatingView?.findViewById<View>(R.id.decodeButton)
        val closeButton = floatingView?.findViewById<View>(R.id.closeButton)

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
            encodeClipboard()
            toggleExpanded()
        }

        decodeButton?.setOnClickListener {
            decodeClipboard()
            toggleExpanded()
        }

        closeButton?.setOnClickListener {
            stopSelf()
        }
    }

    private fun toggleExpanded() {
        isExpanded = !isExpanded
        floatingView?.findViewById<View>(R.id.expandedMenu)?.visibility = 
            if (isExpanded) View.VISIBLE else View.GONE
    }

    private fun encodeClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        
        if (clip != null && clip.itemCount > 0) {
            val text = clip.getItemAt(0).text.toString()
            val encoded = EmojiEncoder.encode("😀", text)
            
            val newClip = ClipData.newPlainText("EMOGIC", encoded)
            clipboard.setPrimaryClip(newClip)
            
            Toast.makeText(this, "[✓] ENCODED & COPIED", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "[!] CLIPBOARD EMPTY", Toast.LENGTH_SHORT).show()
        }
    }

    private fun decodeClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        
        if (clip != null && clip.itemCount > 0) {
            val text = clip.getItemAt(0).text.toString()
            try {
                val decoded = EmojiEncoder.decode(text)
                
                val newClip = ClipData.newPlainText("EMOGIC", decoded)
                clipboard.setPrimaryClip(newClip)
                
                Toast.makeText(this, "[✓] DECODED & COPIED", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "[!] DECODE FAILED", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "[!] CLIPBOARD EMPTY", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        floatingView?.let { windowManager?.removeView(it) }
    }
}
