package com.kanbat.ui.edit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.samples.gridtopager.R
import com.kanbat.model.PointItem
import com.kanbat.utils.HelveticaFont

@ExperimentalFoundationApi
@Composable
fun EditPointView(
    index: Int,
    pointItem: PointItem,
    onUpdatePointClicked: (PointItem) -> Unit,
    onDeletePointClicked: (PointItem) -> Unit
) {
    val point = pointItem.point

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, top = 2.dp, end = 0.dp, bottom = 2.dp)
    ) {
        val (indexRef, editTextRef, saveButtonRef, deleteButtonRef) = createRefs()
        val textState = remember { mutableStateOf(TextFieldValue(point.text)) }
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(pointItem.isEditMode) {
            focusRequester.requestFocus()
        }

        Text(
            text = "${index + 1}. ",
            color = colorResource(R.color.textColorPrimary),
            fontSize = 14.sp,
            fontFamily = HelveticaFont,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .constrainAs(indexRef) {
                    width = Dimension.preferredWrapContent
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        BasicTextField(
            modifier = Modifier
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
                .focusRequester(focusRequester)
                .constrainAs(editTextRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(indexRef.end)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
            value = textState.value,
            onValueChange = { newValue ->
                textState.value = newValue
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onUpdatePointClicked.invoke(
                        PointItem(
                            point = point.copy(text = textState.value.text),
                            isEditMode = false
                        )
                    )
                }
            )
        )

        Icon(
            modifier = Modifier
                .size(48.dp)
                .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                .constrainAs(saveButtonRef) {
                    end.linkTo(deleteButtonRef.start)
                    top.linkTo(editTextRef.bottom)
                }
                .clickable {
                    val text = textState.value.text
                    if (text.isNotEmpty()) {
                        onUpdatePointClicked.invoke(
                            PointItem(
                                point = point.copy(text = text),
                                isEditMode = false
                            )
                        )
                    }
                },
            painter = painterResource(R.drawable.ic_save),
            tint = colorResource(R.color.colorIconsPrimary),
            contentDescription = ""
        )

        Icon(
            modifier = Modifier
                .size(48.dp)
                .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                .constrainAs(deleteButtonRef) {
                    end.linkTo(parent.end)
                    top.linkTo(saveButtonRef.top)
                }
                .clickable {
                    onDeletePointClicked.invoke(
                        PointItem(
                            point = point.copy(text = textState.value.text),
                            isEditMode = false
                        )
                    )
                },
            painter = painterResource(R.drawable.ic_delete),
            tint = colorResource(R.color.colorIconsPrimary),
            contentDescription = ""
        )
    }
}