package com.fadhil.storyapp.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import com.fadhil.storyapp.R


class MyEditText : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private lateinit var originalDrawable: Drawable

    init {
        originalDrawable = background
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (isFocused && (inputType == (TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD))) {
            if ((text?.length ?: 0) < 8) {
                background = AppCompatResources.getDrawable(context, R.drawable.bg_error)
                error = "Password tidak boleh kurang dari 8 karakter."
            } else {
                error = null
                background = originalDrawable
            }
        }
    }

}