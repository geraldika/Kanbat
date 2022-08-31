package com.kanbat.ui.point

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.samples.gridtopager.R
import com.kanbat.model.PointItem
import com.kanbat.model.data.isDone
import com.kanbat.utils.HelveticaFont

@ExperimentalFoundationApi
@Preview
@Composable
fun PointView(
    index: Int,
    pointItem: PointItem,
    onDonePointClicked: (PointItem) -> Unit,
    onEditPointClicked: (PointItem) -> Unit
) {
    val point = pointItem.point

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, top = 4.dp, end = 0.dp, bottom = 4.dp)
            .combinedClickable(
                onLongClick = { onEditPointClicked(pointItem) },
                onClick = { onDonePointClicked(pointItem) }
            )
    ) {
        val (indexRef, titleRef) = createRefs()

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

        Text(
            text = point.text,
            color = colorResource(R.color.textColorSecondary),
            style = TextStyle(
                fontFamily = HelveticaFont,
                textDecoration = if (point.isDone()) TextDecoration.LineThrough else null
            ),
            fontSize = 14.sp,
            lineHeight = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(titleRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(indexRef.end)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        )
    }
}