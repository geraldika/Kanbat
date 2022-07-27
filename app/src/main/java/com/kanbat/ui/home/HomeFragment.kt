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

    private var selectedPageIndex = 0

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
            }.apply {
                attach()
            }
            viewPager.post { viewPager.currentItem = selectedPageIndex }

            addTaskButton.setOnClickListener {
                setFragmentResultListener(AddTaskFragment.REQUEST_KEY) { _, result ->
                    (result[AddTaskFragment.KEY_DESK_ID] as? Long)?.let { deskId ->
                        val pageIndex = deskId.toInt() - 1
                        selectedPageIndex = if (pageIndex < viewPagerAdapter.items.size) {
                            pageIndex
                        } else {
                            0
                        }
                    }
                }
                val b =
                    AddTaskFragment.createArgument(viewPagerAdapter.items[viewPager.currentItem].id)

                this@HomeFragment.findNavController().navigate(
                    R.id.navigation_add_task,
                    AddTaskFragment.createArgument(viewPagerAdapter.items[viewPager.currentItem].id)
                )
            }

            createDeskButton.setOnClickListener {
                this@HomeFragment.findNavController().navigate(R.id.navigation_create_desk)
            }

            launch({
                viewModel.desks.collectLatest { desks ->
                    binding {
                        viewPagerAdapter.items = desks
                        binding?.viewPager?.setCurrentItem(0, true)
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

    @dagger.Module
    class Module

}

//todo delete desk
//todo delete task
//todo delete point
//todo points view
//filter by states, filter icons
//pin code finger
//ads
// icons process + filter
//add -edit dialog fragment animation
//build variants
//card colors depends on status