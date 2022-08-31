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

package com.kanbat.ui.point

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
fun AddPointHeader(
    onCreatePointClicked: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCreatePointClicked.invoke() }
    ) {
        val (titleRef, iconRef) = createRefs()

        Text(
            text = stringResource(R.string.str_add_point),
            color = colorResource(R.color.textColorTertiary),
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 0.dp, top = 16.dp, end = 0.dp, bottom = 16.dp)
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
            tint = colorResource(R.color.textColorTertiary),
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