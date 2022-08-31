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