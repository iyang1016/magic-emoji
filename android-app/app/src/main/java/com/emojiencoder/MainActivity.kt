package com.emojiencoder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {
    private lateinit var modeSwitch: SwitchMaterial
    private lateinit var modeTitle: TextView
    private lateinit var modeDescription: TextView
    private lateinit var inputText: EditText
    private lateinit var outputText: EditText
    private lateinit var emojiContainer: FlexboxLayout
    private lateinit var alphabetContainer: FlexboxLayout
    private lateinit var emojiSelectorLabel: TextView
    private lateinit var alphabetSelectorLabel: TextView
    private lateinit var selectorCard: LinearLayout
    private lateinit var messageCard: LinearLayout
    private lateinit var messageText: TextView
    private lateinit var inputCharCount: TextView
    private lateinit var outputCharCount: TextView
    private lateinit var copyButton: MaterialButton
    private lateinit var pasteButton: MaterialButton
    private lateinit var shareButton: MaterialButton
    private lateinit var clearInputButton: MaterialButton
    private lateinit var loadingSpinner: ProgressBar

    private var selectedEmoji = "😀"
    private var isEncoding = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupModeSwitch()
        setupInputListener()
        setupButtons()
        setupEmojiSelectors()
        updateUI()
    }

    private fun initializeViews() {
        modeSwitch = findViewById(R.id.modeSwitch)
        modeTitle = findViewById(R.id.modeTitle)
        modeDescription = findViewById(R.id.modeDescription)
        inputText = findViewById(R.id.inputText)
        outputText = findViewById(R.id.outputText)
        emojiContainer = findViewById(R.id.emojiContainer)
        alphabetContainer = findViewById(R.id.alphabetContainer)
        emojiSelectorLabel = findViewById(R.id.emojiSelectorLabel)
        alphabetSelectorLabel = findViewById(R.id.alphabetSelectorLabel)
        selectorCard = findViewById(R.id.selectorCard)
        messageCard = findViewById(R.id.messageCard)
        messageText = findViewById(R.id.messageText)
        inputCharCount = findViewById(R.id.inputCharCount)
        outputCharCount = findViewById(R.id.outputCharCount)
        copyButton = findViewById(R.id.copyButton)
        pasteButton = findViewById(R.id.pasteButton)
        shareButton = findViewById(R.id.shareButton)
        clearInputButton = findViewById(R.id.clearInputButton)
        loadingSpinner = findViewById(R.id.loadingSpinner)
    }

    private fun setupModeSwitch() {
        modeSwitch.isChecked = isEncoding
        modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            isEncoding = isChecked
            inputText.setText("")
            animateMatrixTextChange()
            updateUI()
        }
    }

    private fun animateMatrixTextChange() {
        val targetText = if (isEncoding) getString(R.string.encode_label) else getString(R.string.decode_label)
        val targetDesc = if (isEncoding) getString(R.string.mode_encode_description) else getString(R.string.mode_decode_description)
        
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@#$%&*"
        val random = java.util.Random()
        
        var iteration = 0
        val maxIterations = 15
        
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (iteration < maxIterations) {
                    val scrambledTitle = StringBuilder()
                    for (i in targetText.indices) {
                        if (iteration > i * 2) {
                            scrambledTitle.append(targetText[i])
                        } else {
                            scrambledTitle.append(chars[random.nextInt(chars.length)])
                        }
                    }
                    modeTitle.text = scrambledTitle.toString()
                    
                    val scrambledDesc = StringBuilder()
                    for (i in targetDesc.indices) {
                        if (iteration > i / 3) {
                            scrambledDesc.append(targetDesc[i])
                        } else {
                            scrambledDesc.append(chars[random.nextInt(chars.length)])
                        }
                    }
                    modeDescription.text = scrambledDesc.toString()
                    
                    iteration++
                    handler.postDelayed(this, 50)
                } else {
                    modeTitle.text = targetText
                    modeDescription.text = targetDesc
                }
            }
        }
        handler.post(runnable)
    }

    private var typingTimer: android.os.Handler? = null
    private val typingDelay: Long = 500

    private fun setupInputListener() {
        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateCharCount()
                
                typingTimer?.removeCallbacksAndMessages(null)
                typingTimer = android.os.Handler(mainLooper)
                typingTimer?.postDelayed({
                    processInput()
                }, typingDelay)
            }
        })
    }

    private fun setupButtons() {
        copyButton.setOnClickListener { copyToClipboard() }
        pasteButton.setOnClickListener { pasteFromClipboard() }
        shareButton.setOnClickListener { shareOutput() }
        clearInputButton.setOnClickListener { clearInput() }
    }

    private fun setupEmojiSelectors() {
        for (emoji in EmojiLists.EMOJI_LIST) {
            val button = createEmojiButton(emoji)
            emojiContainer.addView(button)
        }

        for (letter in EmojiLists.ALPHABET_LIST) {
            val button = createEmojiButton(letter.toString())
            alphabetContainer.addView(button)
        }
    }

    private fun createEmojiButton(emoji: String): MaterialButton {
        val button = MaterialButton(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle)
        button.text = emoji
        button.textSize = 24f
        button.minimumWidth = 0
        button.minimumHeight = 0
        button.setBackgroundResource(R.drawable.button_hacker)
        button.setTextColor(ContextCompat.getColor(this, R.color.neon_green))
        button.strokeWidth = 2
        button.strokeColor = ContextCompat.getColorStateList(this, R.color.neon_green)
        button.cornerRadius = 8
        button.insetTop = 0
        button.insetBottom = 0
        button.setPadding(0, 0, 0, 0)
        
        val size = (resources.displayMetrics.density * 64).toInt()
        val params = FlexboxLayout.LayoutParams(size, size)
        params.setMargins(8, 8, 8, 8)
        button.layoutParams = params
        
        button.setOnClickListener {
            selectedEmoji = emoji
            updateEmojiSelection()
            processInput()
        }
        
        return button
    }

    private fun updateEmojiSelection() {
        for (i in 0 until emojiContainer.childCount) {
            val button = emojiContainer.getChildAt(i) as MaterialButton
            button.isSelected = button.text.toString() == selectedEmoji
            if (button.isSelected) {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.neon_green))
                button.setTextColor(ContextCompat.getColor(this, R.color.terminal_bg))
            } else {
                button.setBackgroundResource(R.drawable.button_hacker)
                button.setTextColor(ContextCompat.getColor(this, R.color.neon_green))
            }
        }
        
        for (i in 0 until alphabetContainer.childCount) {
            val button = alphabetContainer.getChildAt(i) as MaterialButton
            button.isSelected = button.text.toString() == selectedEmoji
            if (button.isSelected) {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.neon_cyan))
                button.setTextColor(ContextCompat.getColor(this, R.color.terminal_bg))
            } else {
                button.setBackgroundResource(R.drawable.button_hacker)
                button.setTextColor(ContextCompat.getColor(this, R.color.neon_cyan))
            }
        }
    }

    private fun updateUI() {
        inputText.hint = getString(if (isEncoding) R.string.input_hint_encode else R.string.input_hint_decode)
        outputText.hint = getString(if (isEncoding) R.string.output_hint_encode else R.string.output_hint_decode)
        
        if (isEncoding) {
            if (selectorCard.visibility != View.VISIBLE) {
                selectorCard.visibility = View.VISIBLE
                selectorCard.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in))
            }
            if (pasteButton.visibility == View.VISIBLE) {
                pasteButton.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_out))
                pasteButton.postDelayed({ pasteButton.visibility = View.GONE }, 200)
            }
        } else {
            if (selectorCard.visibility == View.VISIBLE) {
                selectorCard.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_out))
                selectorCard.postDelayed({ selectorCard.visibility = View.GONE }, 200)
            }
            if (pasteButton.visibility != View.VISIBLE) {
                pasteButton.visibility = View.VISIBLE
                pasteButton.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in))
            }
        }

        val enabled = isEncoding
        emojiSelectorLabel.alpha = if (enabled) 1.0f else 0.5f
        alphabetSelectorLabel.alpha = if (enabled) 1.0f else 0.5f
        
        for (i in 0 until emojiContainer.childCount) {
            emojiContainer.getChildAt(i).isEnabled = enabled
        }
        for (i in 0 until alphabetContainer.childCount) {
            alphabetContainer.getChildAt(i).isEnabled = enabled
        }

        updateEmojiSelection()
        updateCharCount()
        processInput()
    }

    private fun updateCharCount() {
        val inputLength = inputText.text?.length ?: 0
        val outputLength = outputText.text?.length ?: 0
        
        inputCharCount.text = getString(R.string.characters_count, inputLength)
        outputCharCount.text = getString(R.string.characters_count, outputLength)
    }

    private fun processInput() {
        val input = inputText.text.toString()
        
        if (input.isEmpty()) {
            outputText.setText("")
            hideMessage()
            return
        }

        try {
            val result = if (isEncoding) {
                EmojiEncoder.encode(selectedEmoji, input)
            } else {
                EmojiEncoder.decode(input)
            }
            
            outputText.setText(result)
            updateCharCount()
            hideMessage()
        } catch (e: Exception) {
            outputText.setText("")
            val errorMsg = if (isEncoding) 
                getString(R.string.error_encoding) 
            else 
                getString(R.string.error_decoding)
            showMessage(errorMsg, true)
        }
    }

    private fun showLoading(show: Boolean) {
        loadingSpinner.visibility = if (show) View.VISIBLE else View.GONE
        outputText.alpha = if (show) 0.5f else 1.0f
    }

    private fun copyToClipboard() {
        val text = outputText.text.toString()
        if (text.isNotEmpty()) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Emoji Encoder", text)
            clipboard.setPrimaryClip(clip)
            showMessage(getString(R.string.copied_toast), false)
        }
    }

    private fun pasteFromClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        if (clip != null && clip.itemCount > 0) {
            val text = clip.getItemAt(0).text.toString()
            inputText.setText(text)
            showMessage(getString(R.string.pasted_toast), false)
        } else {
            showMessage(getString(R.string.error_empty_clipboard), true)
        }
    }

    private fun shareOutput() {
        val text = outputText.text.toString()
        if (text.isNotEmpty()) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share encoded message"))
        }
    }

    private fun clearInput() {
        inputText.setText("")
        showMessage(getString(R.string.cleared_toast), false)
    }

    private fun showMessage(message: String, isError: Boolean) {
        messageText.text = message
        if (isError) {
            messageText.setTextColor(ContextCompat.getColor(this, R.color.neon_pink))
        } else {
            messageText.setTextColor(ContextCompat.getColor(this, R.color.neon_green))
        }
        messageCard.visibility = View.VISIBLE
        
        messageCard.postDelayed({
            hideMessage()
        }, 3000)
    }

    private fun hideMessage() {
        messageCard.visibility = View.GONE
    }
}
