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

package com.kanbat.ui.addtask

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.samples.gridtopager.R
import com.kanbat.ui.edit.EditPointView
import com.kanbat.ui.point.PointView
import com.kanbat.viewmodel.AddTaskViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@Composable
fun AddPointsView(
    viewModel: AddTaskViewModel,
    onBackAction: (() -> Unit),
    onAddTaskAction: (() -> Unit)
) {
    val points = viewModel.pointsUiState.collectAsState()
    val isEnabled = viewModel.isTaskValidUiState.collectAsState().value
    val isTaskAdded = viewModel.isTaskAddedUiState.collectAsState().value

    if (isTaskAdded) onAddTaskAction.invoke()

    val lazyListState: LazyListState = rememberLazyListState()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 2.dp)
    ) {
        val (appBarRes, editTextRef, pointsRef) = createRefs()

        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                .constrainAs(appBarRes) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
            title = {
                Text(
                    text = viewModel.deskUiState.collectAsState(null).value?.title.orEmpty(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            backgroundColor = colorResource(R.color.backgroundPrimary),
            navigationIcon = {
                Icon(
                    painterResource(R.drawable.ic_back),
                    contentDescription = "",
                    tint = colorResource(R.color.colorIconsSecondary),
                    modifier = Modifier
                        .padding(start = 4.dp, top = 0.dp, end = 8.dp, bottom = 0.dp)
                        .clickable {
                            viewModel.onAddTaskClicked()
                        }
                )
            },
            actions = {
                Button(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 0.dp, end = 16.dp, bottom = 0.dp),
                    enabled = isEnabled,
                    onClick = { viewModel.onAddTaskClicked() },
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(
                        1.dp,
                        colorResource(if (isEnabled) R.color.colorAccent else R.color.colorAccentAlpha15)
                    ),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.backgroundPrimary),
                        contentColor = colorResource(R.color.backgroundPrimary),
                        disabledBackgroundColor = colorResource(R.color.backgroundPrimary)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.str_save_button),
                        color = colorResource(if (isEnabled) R.color.colorAccent else R.color.colorAccentAlpha15)
                    )
                }
            }
        )

        val textState = remember { mutableStateOf(TextFieldValue("")) }
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(true) {
            focusRequester.requestFocus()
        }

        TextField(
            modifier = Modifier
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 16.dp)
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .constrainAs(editTextRef) {
                    start.linkTo(parent.start)
                    top.linkTo(appBarRes.bottom)
                    end.linkTo(parent.end)
                },
            value = textState.value,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {}
            ),
            onValueChange = { newValue ->
                textState.value = newValue
                viewModel.onTaskTextChanged(newValue.text)
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.str_hint_input_task),
                    color = colorResource(R.color.textColorTertiary)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 16.dp)
                //  .fillMaxSize()
                .focusRequester(focusRequester)
                .constrainAs(pointsRef) {
                    start.linkTo(parent.start)
                    top.linkTo(editTextRef.bottom)
                    end.linkTo(parent.end)
                }
        ) {
            item {
                AddCheckListHeader(
                    isEnabled = isEnabled,
                    onCreatePointClicked = { viewModel.onCreatePointClicked() }
                )
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
