package com.kanbat.ui.addtask

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.samples.gridtopager.R

@ExperimentalFoundationApi
@Composable
fun AddCheckListHeader(
    isEnabled: Boolean,
    onCreatePointClicked: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCreatePointClicked.invoke() }
    ) {
        val (titleRef, iconRef) = createRefs()

        Text(
            text = stringResource(R.string.str_add_points_to_check_list),
            color = colorResource(if (isEnabled) R.color.colorAccent else R.color.textColorTertiary),
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 0.dp, top = 12.dp, end = 0.dp, bottom = 12.dp)
                .constrainAs(titleRef) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(iconRef.start, margin = 8.dp)
                }
        )

        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = "",
            tint = colorResource(if (isEnabled) R.color.colorAccent else R.color.textColorTertiary),
            modifier = Modifier
                .size(20.dp)
                .constrainAs(iconRef) {
                    start.linkTo(titleRef.end, margin = 8.dp)
                    end.linkTo(parent.end)
                    top.linkTo(titleRef.top)
                    bottom.linkTo(titleRef.bottom)
                }
        )
    }
}