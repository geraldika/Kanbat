package com.kanbat.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.samples.gridtopager.R

fun Context.color(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.drawable(resId: Int) = ContextCompat.getDrawable(this, resId)

fun Context.toast(message: Int): Toast = Toast
    .makeText(this.applicationContext, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

 fun String.isNotEmptyOr(block: (String) -> Unit, blockElse: () -> Unit) {
     if (this.isNotEmpty()) block.invoke(this) else blockElse.invoke()
}

fun <T> T?.or(value: T) = this ?: value
