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

package com.kanbat.ui.base

import android.os.Bundle
import android.view.*
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
        findNavController().navigateUp()
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
        activity.supportActionBar?.setHomeButtonEnabled(true)
    }
}

