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

package com.kanbat.ui.addtask

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.google.samples.gridtopager.databinding.FragmentAddTaskBinding
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.PointRepository
import com.kanbat.model.repository.TaskRepository
import com.kanbat.ui.base.BaseFragment
import com.kanbat.ui.home.HomeFragment
import com.kanbat.ui.home.HomeFragment.Companion.REQUEST_KEY
import com.kanbat.utils.or
import com.kanbat.viewmodel.AddTaskViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class AddTaskFragment : BaseFragment<FragmentAddTaskBinding>() {

    @Inject
    lateinit var deskRepository: DeskRepository

    @Inject
    lateinit var tasksRepository: TaskRepository

    @Inject
    lateinit var pointRepository: PointRepository

    private val deskId by lazy { arguments?.getLong(HomeFragment.KEY_DESK_ID).or(0L) }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            AddTaskViewModel.Factory(deskId, deskRepository, tasksRepository, pointRepository)
        )[AddTaskViewModel::class.java]
    }

    override fun getViewBinding() = FragmentAddTaskBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @OptIn(
        ExperimentalComposeUiApi::class,
        ExperimentalFoundationApi::class,
        ExperimentalMaterialApi::class,
        ExperimentalCoroutinesApi::class
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            composeView.setContent {
                MaterialTheme {
                    AddPointsView(
                        viewModel = viewModel,
                        onAddTaskAction = ::onBackPressed
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        setFragmentResult(REQUEST_KEY, bundleOf(HomeFragment.KEY_DESK_ID to deskId))
        super.onBackPressed()
    }

    companion object {
        fun createArgument(deskId: Long): Bundle = bundleOf(HomeFragment.KEY_DESK_ID to deskId)
    }

    @dagger.Module
    class Module
}