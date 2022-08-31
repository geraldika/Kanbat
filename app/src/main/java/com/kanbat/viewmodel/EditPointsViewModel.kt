package com.kanbat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kanbat.model.PointItem
import com.kanbat.model.data.EMPTY_POINT
import com.kanbat.model.repository.PointRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditPointsViewModel(
    private val taskId: Long,
    private val pointRepository: PointRepository
) : BaseViewModel() {

    private var idEditPoint: Long = 0L

    private val _pointsUiState: MutableStateFlow<List<PointItem>> =
        MutableStateFlow(emptyList())
    val pointsUiState: StateFlow<List<PointItem>> = _pointsUiState

    init {
        launchSafe {
            pointRepository
                .getPointsTaskById(taskId)
                .collect { points ->
                    _pointsUiState.value = points.map { point ->
                        PointItem(
                            point = point,
                            isEditMode = point.id == idEditPoint
                        )
                    }
                }
        }
    }

    fun onDonePointClicked(pointItem: PointItem) {
        viewModelScope.launch {
            idEditPoint = 0L
            val point = pointItem.point
            val timeDoneAt = if (point.timeDoneAt > 0) 0L else System.currentTimeMillis()
            pointRepository.insertOrUpdatePoint(pointItem.point.copy(timeDoneAt = timeDoneAt))
        }
    }

    fun onEditPointClicked(pointItem: PointItem) {
        idEditPoint = pointItem.point.id
        _pointsUiState.value = _pointsUiState.value.map { item ->
            PointItem(
                point = item.point,
                isEditMode = item.point.id == idEditPoint
            )
        }
    }

    fun onCreatePointClicked() {
        idEditPoint = 0L
        _pointsUiState.value = _pointsUiState.value
            .map { item ->
                PointItem(
                    point = item.point,
                    isEditMode = false
                )
            }
            .toMutableList()
            .apply {
                if (isEmpty() || first().point.id != 0L) {
                    add(
                        0,
                        PointItem(
                            point = EMPTY_POINT,
                            isEditMode = true
                        )
                    )
                }
            }
    }

    fun onUpdatePointClicked(pointItem: PointItem) {
        viewModelScope.launch {
            idEditPoint = 0L
            pointRepository.insertOrUpdatePoint(pointItem.point.copy(taskId = taskId))
        }
    }

    fun onDeletePointClicked(pointItem: PointItem) {
        viewModelScope.launch {
            idEditPoint = 0L
            if (pointItem.point.id == 0L) {
                _pointsUiState.value = _pointsUiState.value
                    .map { item ->
                        PointItem(
                            point = item.point,
                            isEditMode = false
                        )
                    }
                    .toMutableList()
                    .apply { remove(firstOrNull { it.point.id == EMPTY_POINT.id }) }
            } else {
                pointRepository.deletePoint(pointItem.point.copy(taskId = taskId))
            }
        }
    }

    class Factory(
        private val taskId: Long,
        private val pointRepository: PointRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditPointsViewModel(taskId, pointRepository) as T
        }
    }
}