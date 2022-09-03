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

package com.kanbat.ui.desk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.google.samples.gridtopager.databinding.LayoutTaskHolderBinding
import com.kanbat.model.TaskComposite

class TasksAdapter(
   val  deskId: Long,
    private val onItemClickListener: ((TaskComposite) -> Unit)
) : PagingDataAdapter<TaskComposite, TaskHolder>(TASKS_COMPARATOR) {

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onItemClickListener) }
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
        private val TASKS_COMPARATOR = object : DiffUtil.ItemCallback<TaskComposite>() {
            override fun areItemsTheSame(oldItem: TaskComposite, newItem: TaskComposite): Boolean =
                oldItem.task.id == newItem.task.id

            override fun areContentsTheSame(
                oldItem: TaskComposite,
                newItem: TaskComposite
            ): Boolean =
                oldItem == newItem
        }
    }
}
