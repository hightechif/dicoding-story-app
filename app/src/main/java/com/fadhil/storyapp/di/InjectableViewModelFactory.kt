package com.fadhil.storyapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject


class InjectableViewModelFactory<VM> @Inject constructor(private val viewModel: Lazy<VM>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModel.value as T
    }
}