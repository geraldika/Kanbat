package com.kanbat.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch
import java.lang.Exception

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected var binding: VB? = null

    abstract fun getViewBinding(): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getViewBinding().also { binding = it }.root
    }

    protected fun onBackPressed() {
        findNavController().popBackStack()
    }

    protected fun binding(viewBinding: VB.() -> Unit) {
        binding?.apply { viewBinding() }
    }

    protected fun launch(
        tryBlock: suspend (() -> Unit),
        onStart: (() -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null
    ) {
        onStart?.invoke()
        try {
            viewLifecycleOwner.lifecycleScope.launch { tryBlock.invoke() }
        } catch (e: Throwable) {
            onFailure?.invoke(e)
        }
    }
}

