package com.kanbat.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.samples.gridtopager.R

object UiUtils {

    fun generateColor(context: Context,text: String): Int {
        val hashCode = text.hashCode()
        val defaultColor = ContextCompat.getColor(context, R.color.colorAccent)
        val colors = context.resources.obtainTypedArray(R.array. colorsTasksBackground)
        val color = hashCode % colors?.length()
        return colors.getColor(color, defaultColor)
    }
}