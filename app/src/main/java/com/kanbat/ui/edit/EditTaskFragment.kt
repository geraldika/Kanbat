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

package com.kanbat.ui.edit

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.samples.gridtopager.R
import com.google.samples.gridtopager.databinding.FragmentEditTaskBinding
import com.kanbat.model.data.TaskState
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.PointRepository
import com.kanbat.model.repository.TaskRepository
import com.kanbat.ui.base.BaseFragment
import com.kanbat.ui.home.HomeFragment
import com.kanbat.utils.*
import com.kanbat.viewmodel.EditPointsViewModel
import com.kanbat.viewmodel.EditTaskViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class EditTaskFragment : BaseFragment<FragmentEditTaskBinding>(), View.OnClickListener {

    @Inject
    lateinit var deskRepository: DeskRepository

    @Inject
    lateinit var tasksRepository: TaskRepository

    @Inject
    lateinit var pointRepository: PointRepository

    private val deskId by lazy { arguments?.getLong(KEY_DESK_ID).or(0L) }
    private val taskId by lazy { arguments?.getLong(KEY_TASK_ID).or(0L) }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            EditTaskViewModel.Factory(deskId, taskId, deskRepository, tasksRepository)
        )[EditTaskViewModel::class.java]
    }

    private val pointsVewModel by lazy {
        ViewModelProvider(
            this,
            EditPointsViewModel.Factory(taskId, pointRepository)
        )[EditPointsViewModel::class.java]
    }

    private val addTaskWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.taskText = s.toString().trim()
        }
    }

    private var taskState: TaskState = TaskState.InProgress

    override fun getViewBinding() = FragmentEditTaskBinding.inflate(layoutInflater)

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
                    EditPointsView(pointsVewModel) { setTaskMenuEnabled(!it) }
                }
            }
            initToolbar(toolbar)

            taskInputEditText.setOnLongClickListener {
                viewModel.onEditModeChanged()
                true
            }
            taskInputEditText.addTextChangedListener(addTaskWatcher)

            launch({
                viewModel.isEditModeUiState.collectLatest { isEditMode ->
                    setTaskMenuEnabled(!isEditMode)
                    with(taskInputEditText) {
                        if (isEditMode) {
                            requireActivity().showKeyboard()
                            requestFocus()
                            setSelection(length())
                        } else {
                            requireActivity().hideKeyboard()
                            clearFocus()
                        }
                    }
                }
            })

            launch({
                viewModel.isTaskMenuVisibleUiState.collectLatest { (taskState, isVisible) ->
                    if (isVisible) showTaskMenu(taskState) else hideTaskMenu()
                }
            })

            launch({
                viewModel.deskUiState.collectLatest {
                    toolbarTitleView.text = it.title
                }
            })

            launch({
                viewModel.taskUiState.collectLatest { task ->
                    taskInputEditText.setText(task.text, TextView.BufferType.EDITABLE)
                    taskStateImageView.setColorFilter(
                        requireActivity().color(TaskState.getTaskStateByType(task.state).color)
                    )
                    taskState = TaskState.getTaskStateByType(task.state)
                }
            })

            launch({
                viewModel.isOnFinishEditUiState.collectLatest {
                    setFragmentResult(
                        HomeFragment.REQUEST_KEY,
                        bundleOf(HomeFragment.KEY_DESK_ID to deskId)
                    )
                    findNavController().navigateUp()
                }
            })

            taskLayout.setOnClickListener(this@EditTaskFragment)
            changeStateFab.setOnClickListener(this@EditTaskFragment)
            completeFab.setOnClickListener(this@EditTaskFragment)
            inProgressFab.setOnClickListener(this@EditTaskFragment)
            archiveFab.setOnClickListener(this@EditTaskFragment)
            taskStateImageView.setOnClickListener(this@EditTaskFragment)
            backButton.setOnClickListener(this@EditTaskFragment)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.taskLayout -> viewModel.onEditModeChanged()
            R.id.changeStateFab -> viewModel.onTaskMenuClicked()
            R.id.inProgressFab -> viewModel.onChangeTaskStateClicked(TaskState.InProgress)
            R.id.completeFab -> viewModel.onChangeTaskStateClicked(TaskState.Completed)
            R.id.archiveFab -> viewModel.onChangeTaskStateClicked(TaskState.Archived)
            R.id.taskStateImageView -> onTaskStateClicked()
            R.id.backButton -> onBackPressed()
        }
    }

    override fun onPause() {
        viewModel.onEditModeChanged()
        super.onPause()
    }

    override fun onBackPressed() {
        viewModel.onBackAction()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_edit_task, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.deleteItemView -> {
                viewModel.onDeleteTaskClicked()
                true
            }
            else -> false
        }
    }

    private fun setTaskMenuEnabled(isEnabled: Boolean) {
        binding?.changeStateFab?.isVisible = isEnabled
        if (!isEnabled) hideTaskMenu()
    }

    private fun showTaskMenu(state: TaskState) {
        binding {
            when (state) {
                TaskState.InProgress -> {
                    completeFab.show()
                    archiveFab.show()
                    inProgressFab.isVisible = false
                    inProgressTextView.isVisible = false
                    completeTextView.isVisible = true
                    archiveTextView.isVisible = true
                }
                TaskState.Completed -> {
                    inProgressFab.show()
                    archiveFab.show()
                    completeFab.isVisible = false
                    inProgressTextView.isVisible = true
                    completeTextView.isVisible = false
                    archiveTextView.isVisible = true
                }
                TaskState.Archived -> {
                    inProgressFab.show()
                    completeFab.show()
                    archiveFab.isVisible = false
                    inProgressTextView.isVisible = true
                    completeTextView.isVisible = true
                    archiveTextView.isVisible = false
                }
            }
        }
    }

    private fun hideTaskMenu() {
        binding {
            inProgressTextView.isVisible = false
            completeTextView.isVisible = false
            archiveTextView.isVisible = false
            inProgressFab.hide()
            completeFab.hide()
            archiveFab.hide()
        }
    }

    private fun onTaskStateClicked() {
        requireActivity().toast(taskState.title)
    }

    companion object {
        const val KEY_DESK_ID = "KEY_DESK_ID"
        const val KEY_TASK_ID = "KEY_TASK_ID"

        fun createArgument(
            deskId: Long,
            taskId: Long
        ): Bundle = bundleOf(
            KEY_DESK_ID to deskId,
            KEY_TASK_ID to taskId
        )
    }

    @dagger.Module
    class Module
}

//todo listen in mvvm
//todo delete point
//todo onbackpressed
//todo last symbol
//todo bts state - in progress!
//scroll to desk from edit
//on back finish edit
//change blue color
//open links
//points edit
//click on create points list
