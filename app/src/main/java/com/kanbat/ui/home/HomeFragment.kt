package com.kanbat.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.samples.gridtopager.R
import com.google.samples.gridtopager.databinding.FragmentHomeBinding
import com.kanbat.model.repository.DeskRepository
import com.kanbat.ui.DeskViewPagerAdapter
import com.kanbat.ui.addtask.AddTaskFragment
import com.kanbat.ui.base.BaseFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var deskRepository: DeskRepository

    private val viewPagerAdapter by lazy { DeskViewPagerAdapter(activity as AppCompatActivity) }

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(deskRepository)
        )[HomeViewModel::class.java]

        binding {
            addTaskButton.isVisible = false
            viewPager.adapter = viewPagerAdapter
            TabLayoutMediator(
                tabLayout,
                viewPager
            ) { tab, position ->
                //todo mocked
                tab.text =
                    "${viewPagerAdapter.items[position].id}-${viewPagerAdapter.items[position].title}"
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
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel
                .getAllDesks()
                .collectLatest { desks ->
                    binding {
                        viewPagerAdapter.items = desks
                        binding?.viewPager?.setCurrentItem(0, true)
                        addTaskButton.isVisible = desks.isNotEmpty()
                    }
                }
        }
    }

    @dagger.Module
    class Module

}

//todo
//pin code finger
//flow
//points
//ads
//font

// icons process + filter
// add btn

//add -edit dialog fragment animation

//delete desk task point