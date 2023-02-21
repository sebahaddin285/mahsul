package com.stear.mahsul.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stear.mahsul.repository.Repository

class SignUpActivityViewModelFactory (private val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpActivityViewModel(repository) as T
    }

}