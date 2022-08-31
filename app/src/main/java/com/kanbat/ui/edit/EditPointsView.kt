/*
 * Copyright 2022 Yulia Batova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    viewModel: EditPointsViewModel,
    onEditModeChangeListener: ((Boolean) -> Unit)
) {
    val points = viewModel.pointsUiState.collectAsState()
    onEditModeChangeListener.invoke(points.value.any { it.isEditMode })
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
