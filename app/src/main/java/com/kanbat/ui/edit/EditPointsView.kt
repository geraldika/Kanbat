package com.kanbat.ui.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.kanbat.ui.point.AddPointHeader
import com.kanbat.ui.point.PointView
import com.kanbat.viewmodel.EditPointsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun EditPointsView(
    viewModel: EditPointsViewModel
) {
    val points = viewModel.pointsUiState.collectAsState()
    val lazyListState: LazyListState = rememberLazyListState()
    Box {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                AddPointHeader(onCreatePointClicked = { viewModel.onCreatePointClicked() })
            }
            itemsIndexed(points.value) { index, pointItem ->
                if (pointItem.isEditMode) {
                    EditPointView(
                        index = index,
                        pointItem = pointItem,
                        onUpdatePointClicked = viewModel::onUpdatePointClicked,
                        onDeletePointClicked = viewModel::onDeletePointClicked
                    )
                } else {
                    PointView(
                        index = index,
                        pointItem = pointItem,
                        onDonePointClicked = viewModel::onDonePointClicked,
                        onEditPointClicked = viewModel::onEditPointClicked
                    )
                }
            }
        }
    }
}
