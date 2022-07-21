package com.kanbat.ui.desk

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.samples.gridtopager.databinding.LayoutTaskHolderBinding
import com.kanbat.model.data.Task
import com.kanbat.utils.UiUtils
import com.kanbat.utils.isNotEmptyOr

class TaskHolder(
    private val binding: LayoutTaskHolderBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Task) {
        with(binding) {
            val backgroundColor = UiUtils.generateColor(itemView.context, "${item.id}")
            containerLayout.setBackgroundColor(backgroundColor)

            item.title.isNotEmptyOr({
                titleTextView.isVisible = true
                titleTextView.text = it
            }, {
                titleTextView.isVisible = false
            })

            item.text.isNotEmptyOr({
                descriptionTextView.isVisible = true
                descriptionTextView.text = it
            }, {
                descriptionTextView.isVisible = false
            })

            //  taskProgressView.setProgressDegree(120)
        }
    }
}