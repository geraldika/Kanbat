package com.kanbat.ui.base

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.viewbinding.ViewBinding

abstract class BaseFrameLayout<VB : ViewBinding> : FrameLayout {

    protected var binding: VB? = null

    constructor(context: Context) : super(context) {
        initLayout()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        loadAttrs(attrs)
        initLayout()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        loadAttrs(attrs)
        initLayout()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        loadAttrs(attrs)
        initLayout()
    }

    abstract fun getViewBinding(): VB

    protected open fun loadAttrs(attrs: AttributeSet?) = Unit

    @CallSuper
    protected open fun initLayout() {
        getViewBinding().apply {
            binding = this
        }.root
    }

    override fun onDetachedFromWindow() {
        binding = null
        super.onDetachedFromWindow()
    }

    protected fun binding(viewBinding: VB.() -> Unit) {
        binding?.apply { viewBinding() }
    }
}