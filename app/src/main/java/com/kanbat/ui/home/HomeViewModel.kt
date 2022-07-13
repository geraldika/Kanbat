package com.kanbat.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kanbat.model.repository.DeskRepository

class HomeViewModel(
    private val deskRepository: DeskRepository
) : ViewModel() {

    class Factory(
        private val deskRepository: DeskRepository,

        ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(deskRepository) as T
        }
    }

    fun getAllDesks() = deskRepository.getAllDesks()
}