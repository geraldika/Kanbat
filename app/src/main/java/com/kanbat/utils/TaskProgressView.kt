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

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.google.samples.gridtopager.R

class TaskProgressView : View {

    var arcColor = context.color(R.color.colorInProgress)

    private var arcDegree = 0
    private val halfWidth by lazy { width / 2f }
    private val halfStrokeWidth by lazy { circleStrokeWidth / 2 }

    private var circleStrokeWidth =
        context.resources.getDimensionPixelSize(R.dimen.size_2dp).toFloat()
        set(value) {
            if (field != value) {
                field = value
                backgroundPaint.strokeWidth = field
                arcPaint.strokeWidth = field
            }
        }

    private val backgroundPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.color(R.color.colorInactiveIcons)
            style = Paint.Style.STROKE
            strokeWidth = circleStrokeWidth
        }
    }

    private val arcPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = arcColor
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = circleStrokeWidth
        }
    }

    private val valueAnimator by lazy {
        ValueAnimator().apply {
            duration = ANIMATION_DURATION
            addUpdateListener {
                arcDegree = it.getAnimatedValue(PROPERTY_DEGREE) as Int
                invalidate()
            }
        }
    }

    private val doneImage by lazy { context.drawable(R.drawable.ic_done) }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(
            halfWidth,
            halfWidth,
            halfWidth - halfStrokeWidth,
            backgroundPaint
        )

        canvas.drawArc(
            halfStrokeWidth,
            halfStrokeWidth,
            width - halfStrokeWidth,
            width - halfStrokeWidth,
            -90f,
            arcDegree.toFloat(),
            false,
            arcPaint
        )

        val clipBounds = canvas.clipBounds
        doneImage?.bounds = Rect(
            clipBounds.left,
            clipBounds.top,
            clipBounds.right,
            clipBounds.bottom
        )
        doneImage?.draw(canvas)
    }

    fun setProgressDegree(degree: Int) {
        val propertyValue = PropertyValuesHolder.ofInt(PROPERTY_DEGREE, 0, degree)
        valueAnimator.setValues(propertyValue)
        valueAnimator.start()
    }

    companion object {
        private const val PROPERTY_DEGREE = "PROPERTY_DEGREE"
        private const val ANIMATION_DURATION = 100L
    }
}