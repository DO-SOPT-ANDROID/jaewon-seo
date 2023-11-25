package com.sopt.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class UniversalViewModelFactory<T>(val creator: () -> T) :
    ViewModelProvider.Factory where T : ViewModel {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return creator() as T
    }
}
