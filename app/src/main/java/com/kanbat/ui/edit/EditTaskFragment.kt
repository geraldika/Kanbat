package com.kanbat.ui.edit

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.samples.gridtopager.R
import com.google.samples.gridtopager.databinding.FragmentEditTaskBinding
import com.kanbat.model.data.TaskState
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.TaskRepository
import com.kanbat.ui.base.BaseFragment
import com.kanbat.utils.color
import com.kanbat.utils.hideKeyboard
import com.kanbat.utils.or
import com.kanbat.utils.showKeyboard
import com.kanbat.viewmodel.EditTaskViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class EditTaskFragment : BaseFragment<FragmentEditTaskBinding>(), View.OnClickListener {

    @Inject
    lateinit var deskRepository: DeskRepository

    @Inject
    lateinit var tasksRepository: TaskRepository

    private val deskId by lazy { arguments?.getLong(KEY_DESK_ID).or(0L) }
    private val taskId by lazy { arguments?.getLong(KEY_TASK_ID).or(0L) }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            EditTaskViewModel.Factory(deskId, taskId, deskRepository, tasksRepository)
        )[EditTaskViewModel::class.java]
    }

    private val addTaskWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            viewModel.taskText = s.toString().trim()
        }
    }

    private var pointViews = mutableListOf<PointView>()

    override fun getViewBinding() = FragmentEditTaskBinding.inflate(layoutInflater)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding {
            launch({
                viewModel.isEditMode.collectLatest { isEditMode ->
                    setStateFabsVisibility(!isEditMode)
                    taskInputEditText.isEnabled = isEditMode
                    addPointButton.isVisible = isEditMode
                    if (isEditMode) {
                        taskInputEditText.requestFocus()
                        taskInputEditText.setSelection(taskInputEditText.length())
                        requireActivity().showKeyboard()
                    } else {
                        requireActivity().hideKeyboard()
                    }
                    editTaskLayout.isVisible = !isEditMode
                }
            })

            launch({
                viewModel.isChangeStateMode.collectLatest { isChangeStateMode ->
                    archiveTextView.isVisible = isChangeStateMode
                    completeTextView.isVisible = isChangeStateMode
                    if (isChangeStateMode) {
                        completeFab.show()
                        archiveFab.show()
                    } else {
                        completeFab.hide()
                        archiveFab.hide()
                    }
                }
            })

            launch({
                viewModel.desk.collectLatest {
                    toolbarTitleView.text = it.title
                }
            })

            launch({
                viewModel.task.collectLatest { task ->
                    taskInputEditText.setText(task.text, TextView.BufferType.EDITABLE)
                    taskStateImageView.setColorFilter(
                        requireActivity().color(TaskState.getTaskStateByType(task.state).color),
                        android.graphics.PorterDuff.Mode.MULTIPLY
                    )
                    task.points.forEach { point ->
                        pointViews.clear()
                        createPointView().setPoint(point)
                    }
                }
            })

            launch({
                viewModel.isTaskEdited.collectLatest { isTaskAdded ->
                    if (isTaskAdded) {
                        onBackPressed()
                    }
                }
            })
            launch({
                viewModel.isEnabled.collectLatest { isEnabled ->
                }
            })

            taskInputEditText.addTextChangedListener(addTaskWatcher)

            addPointButton.setOnClickListener(this@EditTaskFragment)
            taskLayout.setOnClickListener(this@EditTaskFragment)

            changeStateFab.setOnClickListener(this@EditTaskFragment)
            completeFab.setOnClickListener(this@EditTaskFragment)
            archiveFab.setOnClickListener(this@EditTaskFragment)

            backButton.setOnClickListener(this@EditTaskFragment)

            editTaskLayout.setOnLongClickListener {
                viewModel.onEditModeChanged(true)
                true
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.addPointButton -> addNewPoint()
            R.id.taskLayout -> viewModel.onEditModeChanged(false)
            R.id.changeStateFab -> viewModel.onStateChangeClicked()
            R.id.completeFab -> viewModel.onCompleteTaskClicked()
            R.id.archiveFab -> viewModel.onArchiveTaskClicked()
            R.id.backButton -> onBackPressed()
        }
    }

//    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menuInflater.inflate(R.menu.menu_task_change_state, menu)
//    }
//
//    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//        return when (menuItem.itemId) {
//            R.id.completeItemView -> {
//                true
//            }
//            else -> false
//        }
//    }

    private fun createPointView(): PointView {
        val pointView = PointView(requireContext())
        pointView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        binding?.pointsContainerLayout?.addView(pointView)
        pointViews.add(pointView)
        return pointView
    }

    private fun addNewPoint() {
        binding {
            if (pointViews.isEmpty() || pointViews.last().text.isNotEmpty()) {
                createPointView().requestFocus()
            } else {
                pointViews.last().requestPointViewFocus()
            }
        }
    }

    private fun setStateFabsVisibility(isVisible: Boolean) {
        binding {
            changeStateFab.isVisible = isVisible
            completeFab.isVisible = isVisible
            archiveFab.isVisible = isVisible
        }
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
//todo bts state
//color icons
// add checkpoints