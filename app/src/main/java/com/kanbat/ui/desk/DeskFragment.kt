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

package com.kanbat.ui.desk

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.samples.gridtopager.R
import com.google.samples.gridtopager.databinding.FragmentDeskBinding
import com.kanbat.model.data.TaskState
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.TaskRepository
import com.kanbat.ui.base.BaseFragment
import com.kanbat.ui.edit.EditTaskFragment
import com.kanbat.utils.or
import com.kanbat.viewmodel.DeskViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class DeskFragment : BaseFragment<FragmentDeskBinding>() {

    @Inject
    lateinit var deskRepository: DeskRepository

    @Inject
    lateinit var tasksRepository: TaskRepository

    private val deskId by lazy { arguments?.getLong(KEY_DESK_ID).or(0L) }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            DeskViewModel.Factory(deskId, deskRepository, tasksRepository)
        )[DeskViewModel::class.java]
    }

    private val adapter by lazy { TasksAdapter(onItemClickListener = viewModel::onTaskClicked) }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun getViewBinding() = FragmentDeskBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            initToolbar(toolbar)

            val gridLayoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.adapter = adapter

            launch({
                viewModel
                    .taskItemsUiState
                    .collectLatest { data ->
                        adapter.submitData(data)
                    }
            })

            adapter.addLoadStateListener { loadState ->
                emptyTasksLayout.containerLayout.isVisible =
                    loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached && adapter.itemCount < 1
            }
        }

        launch({
            viewModel
                .deckUiState
                .collectLatest {
                    binding {
                        toolbarTitleView.text = it.title
                    }
                }
        })

        launch({
            viewModel
                .selectedTaskUiState
                .collectLatest { task ->
                    findNavController()
                        .navigate(
                            R.id.navigation_edit_task,
                            EditTaskFragment.createArgument(deskId, task.id)
                        )
                }
        })
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_filter_tasks, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.inProgressItemView -> {
                viewModel.onFilterTasksClicked(TaskState.InProgress)
                true
            }
            R.id.completedTasksItemView -> {
                viewModel.onFilterTasksClicked(TaskState.Completed)
                true
            }
            R.id.archivedTasksItemView -> {
                viewModel.onFilterTasksClicked(TaskState.Archived)
                true
            }
            else -> false
        }
    }

    companion object {
        private const val KEY_DESK_ID = "KEY_DESK_ID"

        fun newInstance(id: Long) = DeskFragment().apply {
            arguments = bundleOf(KEY_DESK_ID to id)
        }
    }

    @dagger.Module
    class Module
}