package com.kanbat.ui.base

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding> : Fragment(), MenuProvider {

    protected var binding: VB? = null

    abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getViewBinding().also { binding = it }.root.apply {
            val menuHost: MenuHost = requireActivity()
            menuHost.addMenuProvider(this@BaseFragment, viewLifecycleOwner, Lifecycle.State.RESUMED)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) = Unit

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false

    protected open fun onBackPressed() {
        val isCanGoBack = findNavController().navigateUp()
        Log.d("TestB", "onBackPressed  $isCanGoBack $this")

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

    protected open fun initToolbar(toolbar: Toolbar) {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.let { actionBar ->
            actionBar.setHomeButtonEnabled(true)
        }
    }
}

