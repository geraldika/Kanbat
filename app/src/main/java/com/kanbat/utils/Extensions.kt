package com.kanbat.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

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

fun Collection<Any>.isNotEmptyOr(block: (Collection<Any>) -> Unit, blockElse: () -> Unit) {
    if (this.isNotEmpty()) block.invoke(this) else blockElse.invoke()
}

fun <T> T?.or(value: T) = this ?: value

fun Activity.showKeyboard() {
    currentFocus?.apply {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Activity.hideKeyboard() {
    currentFocus?.windowToken?.let {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(it, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}
