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
                        onBackAction = ::onBackPressed,
                        onAddTaskAction = ::onAddTaskAction
                    )
                }
            }
        }
    }

    private fun onAddTaskAction() {
        setFragmentResult(REQUEST_KEY, bundleOf(HomeFragment.KEY_DESK_ID to deskId))
        onBackPressed()
    }

    companion object {
        fun createArgument(deskId: Long): Bundle = bundleOf(HomeFragment.KEY_DESK_ID to deskId)
    }

    @dagger.Module
    class Module
}