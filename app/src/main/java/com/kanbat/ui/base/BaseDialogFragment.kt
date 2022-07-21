package com.kanbat.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {

    protected open var binding: VB? = null

    abstract fun getViewBinding(): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            getViewBinding().also { binding = it }.root
        } catch (ex: Exception) {
            throw ex
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    protected fun onBackPressed() {
        findNavController().popBackStack()
    }

    protected fun binding(block: VB.() -> Unit) {
        binding?.apply { block() }
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