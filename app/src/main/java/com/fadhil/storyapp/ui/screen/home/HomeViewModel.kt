package com.fadhil.storyapp.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fadhil.storyapp.domain.usecase.AuthUseCase
import com.fadhil.storyapp.domain.usecase.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val settingUseCase: SettingUseCase
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    fun setUsername(input: String) {
        _username.postValue(input)
    }

    fun getThemeSetting(): LiveData<Boolean> = settingUseCase.getThemeSetting().asLiveData()

    fun logout() = authUseCase.logout().asLiveData()

}