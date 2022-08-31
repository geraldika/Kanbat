package com.kanbat.ui.desk

import android.graphics.PorterDuff
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.samples.gridtopager.databinding.LayoutTaskHolderBinding
import com.kanbat.model.TaskComposite
import com.kanbat.model.data.TaskState
import com.kanbat.model.data.isDone
import com.kanbat.utils.UiUtils
import com.kanbat.utils.color
import com.kanbat.utils.isNotEmptyOr

class TaskHolder(
    private val binding: LayoutTaskHolderBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: TaskComposite, onItemClickListener: ((TaskComposite) -> Unit)) {
        with(binding) {
            val task = item.task
            val backgroundColor = UiUtils.generateColor(itemView.context, "${task.id}")
            containerLayout.setBackgroundColor(backgroundColor)
            taskTextView.text = task.text

            taskStateView.setColorFilter(
                itemView.context.color(TaskState.getTaskStateByType(task.state).color),
                PorterDuff.Mode.SRC_IN
            )

            val points = item.points
            points.isNotEmptyOr({
                pointCountTextView.isVisible = true
                val pointDone = points.filter { it.isDone() }.size
                pointCountTextView.text = "$pointDone/${points.size}"
            }, {
                pointCountTextView.isVisible = false
            })
            itemView.setOnClickListener { onItemClickListener.invoke(item) }
        }
    }
}