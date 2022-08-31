package com.kanbat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kanbat.model.PointItem
import com.kanbat.model.data.Desk
import com.kanbat.model.data.Point
import com.kanbat.model.data.Task
import com.kanbat.model.data.TaskState
import com.kanbat.model.repository.DeskRepository
import com.kanbat.model.repository.PointRepository
import com.kanbat.model.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddTaskViewModel(
    private val deskId: Long,
    private val deskRepository: DeskRepository,
    private val taskRepository: TaskRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private val deskState: StateFlow<Desk?> = deskRepository
        .getDeskById(deskId)
        .catch { }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val deskUiState get() = deskState.filterNotNull()

    private var isTaskValidState = MutableStateFlow(false)
    val isTaskValidUiState get() = isTaskValidState

    private var isTaskAddedState = MutableStateFlow(false)
    val isTaskAddedUiState get() = isTaskAddedState

    private val pointsState: MutableStateFlow<List<PointItem>> =
        MutableStateFlow(emptyList())
    val pointsUiState: StateFlow<List<PointItem>> = pointsState

    private var taskText: String = ""
        set(value) {
            field = value
            validateTask()
        }

    private var idEditPoint: Long = 0L

    fun onTaskTextChanged(text: String) {
        taskText = text
        validateTask()
    }

    fun onAddTaskClicked() {
        viewModelScope.launch {
            if (isTaskValidState.value) {
                val taskId = taskRepository.addTask(
                    Task(
                        id = 0L,
                        deskId = deskId,
                        title = "",
                        text = taskText,
                        state = TaskState.InProgress.state,
                        priority = 0,
                        timeCreatedAt = System.currentTimeMillis(),
                        timeDoneAt = 0L
                    )
                )
                pointRepository.insertPoints(pointsState.value.map {
                    it.point.copy(
                        id = 0L,
                        taskId = taskId
                    )
                })
                isTaskAddedState.value = taskId > 0L
            } else {
                //todo in the future add popup
                isTaskAddedState.value = true
            }
        }
    }

    private fun validateTask() {
        isTaskValidState.value = taskText.isNotEmpty()
    }

    fun onDonePointClicked(pointItem: PointItem) {
        viewModelScope.launch {
            idEditPoint = 0L
            val point = pointItem.point
            pointsState.value = pointsState.value.map { item ->
                PointItem(
                    point = point,
                    isEditMode = point.id == idEditPoint
                )
            }
        }
    }

    fun onEditPointClicked(pointItem: PointItem) {
        idEditPoint = pointItem.point.id
        pointsState.value = pointsState.value.map { item ->
            PointItem(
                point = item.point,
                isEditMode = item.point.id == idEditPoint
            )
        }
    }

    fun onCreatePointClicked() {
        if (idEditPoint == 0L) { //todo bool
            idEditPoint = 1L
            pointsState.value = pointsState.value
                .map { item ->
                    PointItem(
                        point = item.point,
                        isEditMode = false
                    )
                }
                .toMutableList()
                .apply {
                    add(
                        0,
                        PointItem(
                            point = Point(
                                pointsState.value.size.toLong() + 1,
                                0L,
                                "",
                                System.currentTimeMillis(),
                                0L
                            ),
                            isEditMode = true
                        )
                    )
                }
        }
    }

    fun onUpdatePointClicked(pointItem: PointItem) {
        viewModelScope.launch {
            idEditPoint = 0L
            pointsState.value = pointsState.value.map { item ->
                if (item.point.id == pointItem.point.id) pointItem.copy(isEditMode = false) else item
            }
        }
    }

    fun onDeletePointClicked(pointItem: PointItem) {
        viewModelScope.launch {
            idEditPoint = 0L
            pointsState.value = pointsState.value
                .mapNotNull { item -> if (item.point.id == pointItem.point.id) null else item }
        }
    }

    class Factory(
        private val deskId: Long,
        private val deskRepository: DeskRepository,
        private val taskRepository: TaskRepository,
        private val pointRepository: PointRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddTaskViewModel(deskId, deskRepository, taskRepository, pointRepository) as T
        }
    }
}
