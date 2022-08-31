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

package com.kanbat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kanbat.model.data.Desk
import com.kanbat.model.repository.DeskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateDeskViewModel(private val deskRepository: DeskRepository) :
    ViewModel() {

    var deskTitle: String = ""
        set(value) {
            field = value
            validateDesk()
        }

    private var isDeskValidState = MutableStateFlow(false)
    val isDeskValidUiState get() = isDeskValidState

    private var isDeckCreatedState = MutableStateFlow(false)
    val isDeskCreatedUiState get() = isDeckCreatedState

    fun onCreateDeskClicked() {
        viewModelScope.launch {
            isDeckCreatedState.value = deskRepository.insertTask(Desk(0L, deskTitle)) > 0
        }
    }

    private fun validateDesk() {
        isDeskValidState.value = deskTitle.isNotEmpty()
    }

    class Factory(
        private val deskRepository: DeskRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateDeskViewModel(deskRepository) as T
        }
    }
}