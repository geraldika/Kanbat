package com.kanbat.ui.desk

import androidx.recyclerview.widget.RecyclerView
import com.google.samples.gridtopager.databinding.LayoutTaskHolderBinding
import com.kanbat.model.data.Task
import com.kanbat.utils.UiUtils

class TaskHolder(
    private val binding: LayoutTaskHolderBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Task) {
        with(binding) {
             val backgroundColor = UiUtils.generateColor(itemView.context, "${item.id}")
             containerLayout.setBackgroundColor(backgroundColor)

            titleTextView.text = item.title
            pointsCountTextView.text = item.deskId.toString() + item.text

            taskProgressView.setProgressDegree(120)
        }
    }
}