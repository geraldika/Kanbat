package com.kanbat.ui.addtask

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.google.samples.gridtopager.R
import com.google.samples.gridtopager.databinding.FragmentAddTaskBinding
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.TaskRepository
import com.kanbat.ui.base.BaseFragment
import com.kanbat.utils.or
import com.kanbat.viewmodel.AddTaskViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

//task points numbers and validation
//fragment transition
class AddTaskFragment : BaseFragment<FragmentAddTaskBinding>() {

    @Inject
    lateinit var deskRepository: DeskRepository

    @Inject
    lateinit var tasksRepository: TaskRepository

    private val deskId by lazy { arguments?.getLong(KEY_DESK_ID).or(0L) }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            AddTaskViewModel.Factory(deskId, deskRepository, tasksRepository)
        )[AddTaskViewModel::class.java]
    }

    private val addTaskWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.taskText = s.toString().trim()
        }
    }

    private val pointEditTexts = mutableListOf<EditText>()

    override fun getViewBinding() = FragmentAddTaskBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            launch({
                viewModel.desk.collectLatest {
                    toolbarTitleView.text = it.title
                }
            })

            launch({
                viewModel.isTaskAdded.collectLatest { isTaskAdded ->
                    if (isTaskAdded) {
                        setFragmentResult(REQUEST_KEY, bundleOf(KEY_DESK_ID to deskId))
                        onBackPressed()
                    }
                }
            })
            launch({
                viewModel.isEnabled.collectLatest { isEnabled ->
                    addTaskButton.isEnabled = isEnabled
                }
            })

            taskInputEditText.addTextChangedListener(addTaskWatcher)
            addPointButton.setOnClickListener { addNewPointEditText() }
            addTaskButton.setOnClickListener { viewModel.onAddTaskClicked(pointEditTexts.map { it.text.toString() }) }
            backButton.setOnClickListener { onBackPressed() }
        }
    }

    private fun addNewPointEditText() {
        binding {
            if (pointEditTexts.isEmpty() || pointEditTexts.last().text.isNotEmpty()) {
                val pointEditText = EditText(context)
                pointEditText.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                pointsContainerLayout.addView(pointEditText)
                pointEditTexts.add(pointEditText)
                pointEditText.requestFocus()
            } else {
                val emptyEditText = pointEditTexts.last()
                emptyEditText.hint = context?.getString(R.string.str_add_point)
                emptyEditText.requestFocus()
            }
        }
    }

    companion object {
        const val KEY_DESK_ID = "KEY_DESK_ID"
        const val REQUEST_KEY = "ADD_TASK_REQUEST_KEY"

        fun createArgument(deskId: Long): Bundle = bundleOf(KEY_DESK_ID to deskId)
    }

    @dagger.Module
    class Module
}