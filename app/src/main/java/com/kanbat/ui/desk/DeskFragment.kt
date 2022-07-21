package com.kanbat.ui.desk

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.samples.gridtopager.databinding.FragmentDeskBinding
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.TaskRepository
import com.kanbat.ui.base.BaseFragment
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

    private val adapter by lazy { TasksAdapter() }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun getViewBinding() = FragmentDeskBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            val gridLayoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.adapter = adapter

            filterButton.setOnClickListener {}
        }

        launch({
            viewModel
                .desks
                .collectLatest(adapter::submitData)
        })

        launch({
            viewModel
                .desk
                .collectLatest {
                    binding {
                        toolbarTitleView.text = it.title
                    }
                }
        })
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