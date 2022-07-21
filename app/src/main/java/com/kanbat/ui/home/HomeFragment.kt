package com.kanbat.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            addTaskButton.isVisible = false

            val viewPagerAdapter = DeskViewPagerAdapter(activity as AppCompatActivity)
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

            addTaskButton.setOnClickListener {
                val id = viewPagerAdapter.items[viewPager.currentItem].id
                val bundle = Bundle()
                bundle.putLong(AddTaskFragment.KEY_DESK_ID, id)
                this@HomeFragment.findNavController().navigate(R.id.navigation_add_task, bundle)
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
//filter by states
//pin code finger
//ads
// icons process + filter
//add -edit dialog fragment animation