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

package com.kanbat.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.samples.gridtopager.R
import com.google.samples.gridtopager.databinding.FragmentHomeBinding
import com.kanbat.model.repository.DeskRepository
import com.kanbat.ui.DeskViewPagerAdapter
import com.kanbat.ui.addtask.AddTaskFragment
import com.kanbat.ui.base.BaseFragment
import com.kanbat.viewmodel.HomeViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var deskRepository: DeskRepository

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
            HomeViewModel.Factory(deskRepository)
        )[HomeViewModel::class.java]
    }
    private val viewPagerAdapter by lazy { DeskViewPagerAdapter(activity as AppCompatActivity) }

    private var selectedDeskId = 0L

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            addTaskButton.isVisible = false
            viewPager.adapter = viewPagerAdapter
            viewPager.isSaveEnabled = false
            TabLayoutMediator(
                tabLayout,
                viewPager
            ) { tab, position ->
                tab.text = viewPagerAdapter.items[position].title
                tab.view.setOnLongClickListener {
                    DeleteDeskDialog.show(requireActivity()) {
                        viewModel.onDeleteDeskClicked(
                            viewPagerAdapter.items.get(position)
                        )
                    }
                    true
                }
            }.apply {
                attach()
            }
            viewPager.post { viewPager.currentItem = getSelectedAdapterPosition() }

            addTaskButton.setOnClickListener {
                setOnSelectedDeskListener()
                this@HomeFragment.findNavController().navigate(
                    R.id.navigation_add_task,
                    AddTaskFragment.createArgument(viewPagerAdapter.items[viewPager.currentItem].id)
                )
            }

            createDeskButton.setOnClickListener {
                setOnSelectedDeskListener()
                this@HomeFragment.findNavController().navigate(R.id.navigation_create_desk)
            }

            settingsButton.setOnClickListener {
                //todo settings, about, donate
            }

            launch({
                viewModel.desksUiState.collectLatest { desks ->
                    binding {
                        emptyDeskLayout.containerLayout.isVisible = desks.isEmpty()
                        viewPagerAdapter.items = desks
                        binding?.viewPager?.setCurrentItem(getSelectedAdapterPosition(), true)
                        addTaskButton.isVisible = desks.isNotEmpty()
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        binding {
            viewPager.adapter = null
        }
        super.onDestroyView()
    }

    private fun getSelectedAdapterPosition(): Int {
        val position = viewPagerAdapter.items.indexOfFirst { it.id == selectedDeskId }
        return 0.coerceAtLeast(position)
    }

    private fun setOnSelectedDeskListener() {
        setFragmentResultListener(REQUEST_KEY) { _, result ->
            (result[KEY_DESK_ID] as? Long)?.let { deskId ->
                selectedDeskId = deskId
            }
        }
    }

    companion object {
        const val KEY_DESK_ID = "KEY_DESK_ID"
        const val REQUEST_KEY = "TASK_REQUEST_KEY"
    }

    @dagger.Module
    class Module
}

//todo filter by states, filter icons
//archive -> recreate buttons flow
//pin code finger
//ads
//add -edit dialog fragment animation
//build variants
//notifications
//license
//open links
//desk settings: change order & delete desk
//proguard