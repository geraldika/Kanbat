package com.kanbat.ui.addtask

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.samples.gridtopager.R
import com.google.samples.gridtopager.databinding.FragmentAddTaskBinding
import com.kanbat.model.data.Task
import com.kanbat.model.repository.TaskRepository
import com.kanbat.ui.base.BaseDialogFragment
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject

class AddTaskFragment : BaseDialogFragment<FragmentAddTaskBinding>() {

    @Inject
    lateinit var tasksRepository: TaskRepository

    private val deskId by lazy { arguments?.getLong(KEY_DESK_ID) ?: 0L }

    override fun getViewBinding() = FragmentAddTaskBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.SlideAnimation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addTaskViewModel = ViewModelProvider(
            this,
            AddTaskViewModel.Factory(tasksRepository)
        )[AddTaskViewModel::class.java]

        //todo mocked
        addTaskViewModel.addTask(
            Task(
                0,
                deskId,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                3,
                0,
                emptyList(),
                0L,
                0L
            )
        )

        binding {

        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.attributes?.let { params ->
            params.width = ActionBar.LayoutParams.MATCH_PARENT
            params.height = ActionBar.LayoutParams.MATCH_PARENT
            dialog?.window?.attributes = params
        }
    }

    companion object {
        const val KEY_DESK_ID = "KEY_DESK_ID"
    }

    @dagger.Module
    class Module
}