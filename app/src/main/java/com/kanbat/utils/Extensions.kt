/*
 * Copyright 2022 Yulia Batova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kanbat.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import com.google.samples.gridtopager.R

val HelveticaFont = FontFamily(
    Font(R.font.helvetica, FontWeight.Normal),
    Font(R.font.helvetica_bold, FontWeight.SemiBold),
    Font(R.font.helvetica_bold, FontWeight.ExtraBold)
)

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

fun CharSequence?.orEmptyString(): String = this.or("").toString()

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

fun <E> Iterable<E>.replace(old: E, new: E) = map { if (it == old) new else it }
