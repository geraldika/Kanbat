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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val deskRepository: DeskRepository
) : ViewModel() {

    private val desksState: StateFlow<List<Desk>?> = deskRepository
        .getAllDesks()
        .catch { }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val desksUiState get() = desksState.filterNotNull()

    fun onDeleteDeskClicked(desk: Desk) {
        viewModelScope.launch {
            deskRepository.deleteDesk(desk)
        }
    }

    class Factory(
        private val deskRepository: DeskRepository,

        ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(deskRepository) as T
        }
    }
}