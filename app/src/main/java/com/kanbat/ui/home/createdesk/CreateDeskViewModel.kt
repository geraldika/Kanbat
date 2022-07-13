package com.kanbat.ui.home.createdesk

import androidx.lifecycle.*
import com.kanbat.model.data.Desk
import com.kanbat.model.repository.DeskRepository

class CreateDeskViewModel(private val deskRepository: DeskRepository) :
    ViewModel() {

    class Factory(private val deskRepository: DeskRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateDeskViewModel(deskRepository) as T
        }
    }

    fun getAllDesks() = deskRepository.getAllDesks()

    suspend fun createDesk(desk: Desk) = deskRepository.insertTask(desk)
}