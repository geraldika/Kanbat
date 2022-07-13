package com.kanbat.ui.desk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.samples.gridtopager.databinding.LayoutTaskHolderBinding
import com.kanbat.model.data.Task

class TasksAdapter : PagingDataAdapter<Task, TaskHolder>(TASKS_COMPARATOR) {

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder(
            LayoutTaskHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    companion object {
        private val TASKS_COMPARATOR = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
                oldItem == newItem
        }
    }
}
