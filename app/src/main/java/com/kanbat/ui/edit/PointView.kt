package com.kanbat.ui.edit

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.google.samples.gridtopager.R
import com.google.samples.gridtopager.databinding.LayoutPointViewBinding
import com.kanbat.model.data.Point
import com.kanbat.model.data.isDone
import com.kanbat.ui.base.BaseFrameLayout

class PointView : BaseFrameLayout<LayoutPointViewBinding> {

    override fun getViewBinding() = LayoutPointViewBinding
        .inflate(LayoutInflater.from(context), this, true)

    var index: Int = 0
    var text: String = ""

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    fun setPoint(point: Point) {
        binding {
            pointIndexTextView.isChecked = point.isDone()
            pointEditText.setText(point.text, TextView.BufferType.EDITABLE)
            pointEditText.isEnabled = false
        }
    }

    fun requestPointViewFocus() {
        binding {
            pointEditText.hint = context?.getString(R.string.str_add_point)
            pointEditText.requestFocus()
        }
    }
}