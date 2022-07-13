package com.kanbat.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.drawable(resId: Int) = ContextCompat.getDrawable(this, resId)
