package com.fadhil.storyapp.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import kotlin.math.roundToInt

object ConvertUtils {

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

    fun parseColor(context: Context, colorStr: String?): Int {
        return try {
            Color.parseColor(colorStr)
        } catch (e: IllegalArgumentException) {
            Color.parseColor("#5cb85c")
        }
    }
}