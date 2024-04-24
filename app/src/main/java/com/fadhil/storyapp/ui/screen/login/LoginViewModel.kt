package com.fadhil.storyapp.ui.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fadhil.storyapp.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _email = MutableLiveData<String?>().apply { postValue(null) }
    val email: LiveData<String?> = _email

    private val _password = MutableLiveData<String?>().apply { postValue(null) }
    val password: LiveData<String?> = _password

    val isFormValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        isFormValid.addSource(email) { emailValue ->
            val passwordValue = password.value.orEmpty()
            isFormValid.value = isEmailValid(emailValue) && isPasswordValid(passwordValue)
        }

        isFormValid.addSource(password) { passwordValue ->
            val emailValue = email.value.orEmpty()
            isFormValid.value = isEmailValid(emailValue) && isPasswordValid(passwordValue)
        }
    }

    private fun isEmailValid(email: String?): Boolean {
        // Add your email validation logic here
        return email?.isNotEmpty() == true && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String?): Boolean {
        // Add your password validation logic here
        return password?.isNotEmpty() == true && password.length >= 8
    }


    fun setEmail(input: String) {
        _email.postValue(input)
    }

    fun setPassword(input: String) {
        _password.postValue(input)
    }

    fun login(email: String, password: String) = authUseCase.login(email, password).asLiveData()

}