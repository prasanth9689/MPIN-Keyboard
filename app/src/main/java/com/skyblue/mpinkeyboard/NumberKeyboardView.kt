package com.skyblue.mpinkeyboard

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.SoundEffectConstants
import android.view.View
import android.view.HapticFeedbackConstants
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible

class NumberKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val inflater = LayoutInflater.from(context)
    private val keysGrid: GridLayout
    private val zeroInclude: View
    private val btnDelete: ImageButton
    private val btnCall: Button

    private val longPressHandler = Handler(Looper.getMainLooper())
    private var repeating = false

    var onKeyPress: ((String) -> Unit)? = null
    var onDelete: (() -> Unit)? = null
    var onCall: (() -> Unit)? = null


    init {
        val root = inflater.inflate(R.layout.view_number_keyboard, this, true)
        keysGrid = root.findViewById(R.id.keys_grid)
        zeroInclude = root.findViewById(R.id.key_zero_include)
        btnDelete = root.findViewById(R.id.btn_delete)
        btnCall = root.findViewById(R.id.btn_call)

        setupKeys()
        setupDelete()
        setupCall()
    }

    private fun makeKey(digit: String, letters: String = ""): View {
        val key = inflater.inflate(R.layout.view_number_key, keysGrid, false)
        val digitTv = key.findViewById<TextView>(R.id.key_digit)
        val lettersTv = key.findViewById<TextView>(R.id.key_letters)

        digitTv.text = digit
        lettersTv.text = letters

        key.setOnClickListener {
            performClickFeedback(key)
            onKeyPress?.invoke(digit)
        }

        key.setOnLongClickListener {
            // optionally long-press for + on 0
            if (digit == "0") {
                performClickFeedback(key)
                onKeyPress?.invoke("+")
                true
            } else false
        }

        return key
    }

    private fun setupKeys() {
        // numeric keys 1-9 with letters like iPhone
        val mapping = listOf(
            "1" to "",
            "2" to "ABC",
            "3" to "DEF",
            "4" to "GHI",
            "5" to "JKL",
            "6" to "MNO",
            "7" to "PQRS",
            "8" to "TUV",
            "9" to "WXYZ"
        )

        mapping.forEach { (digit, letters) ->
            val keyView = makeKey(digit, letters)
            keysGrid.addView(keyView)


        }

        // configure zero key (we inflated it in XML); set digit and letters
        val zeroDigit = zeroInclude.findViewById<TextView>(R.id.key_digit)
        val zeroLetters = zeroInclude.findViewById<TextView>(R.id.key_letters)
        zeroDigit.text = "0"
        zeroLetters.text = "+"
        zeroInclude.setOnClickListener {
            performClickFeedback(zeroInclude)
            onKeyPress?.invoke("0")
        }
        zeroInclude.setOnLongClickListener {
            performClickFeedback(zeroInclude)
            onKeyPress?.invoke("+")
            true
        }
    }

    private fun setupDelete() {
        btnDelete.setOnClickListener {
            performClickFeedback(it)
            onDelete?.invoke()
        }

        // long-press repeating delete
        btnDelete.setOnLongClickListener {
            performClickFeedback(it)
            repeating = true
            longPressHandler.post(object : Runnable {
                override fun run() {
                    if (!repeating) return
                    onDelete?.invoke()
                    longPressHandler.postDelayed(this, 100)
                }
            })
            true
        }

        setOnTouchListener { _, event ->
            // stop repeating when finger lifts -> check ACTION_UP/ACTION_CANCEL
            if (event.action == android.view.MotionEvent.ACTION_UP || event.action == android.view.MotionEvent.ACTION_CANCEL) {
                repeating = false
            }
            false
        }
    }

    private fun setupCall() {
        btnCall.setOnClickListener {
            performClickFeedback(it)
            onCall?.invoke()
        }
    }

    private fun performClickFeedback(view: View) {
        // haptic
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        // click sound
        view.playSoundEffect(SoundEffectConstants.CLICK)
    }

    /** Public helpers to control visibility or labels **/
    fun setCallButtonText(text: String) { btnCall.text = text }
    fun setCallButtonVisible(visible: Boolean) { btnCall.isVisible = visible }
}