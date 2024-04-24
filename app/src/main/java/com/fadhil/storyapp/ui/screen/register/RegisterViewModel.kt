package com.fadhil.storyapp.ui.screen.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fadhil.storyapp.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _name = MutableLiveData<String?>().apply { postValue(null) }
    val name: LiveData<String?> = _name

    private val _email = MutableLiveData<String?>().apply { postValue(null) }
    val email: LiveData<String?> = _email

    private val _password = MutableLiveData<String?>().apply { postValue(null) }
    val password: LiveData<String?> = _password

    val isFormValid: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        isFormValid.addSource(name) { nameValue ->
            val emailValue = email.value.orEmpty()
            val passwordValue = password.value.orEmpty()
            isFormValid.value =
                isNameValid(nameValue) && isEmailValid(emailValue) && isPasswordValid(passwordValue)
        }

        isFormValid.addSource(email) { emailValue ->
            val nameValue = name.value.orEmpty()
            val passwordValue = password.value.orEmpty()
            isFormValid.value =
                isNameValid(nameValue) && isEmailValid(emailValue) && isPasswordValid(passwordValue)
        }

        isFormValid.addSource(password) { passwordValue ->
            val nameValue = name.value.orEmpty()
            val emailValue = email.value.orEmpty()
            isFormValid.value =
                isNameValid(nameValue) && isEmailValid(emailValue) && isPasswordValid(passwordValue)
        }
    }

    private fun isNameValid(name: String?): Boolean {
        return name?.isNotEmpty() == true
    }

    private fun isEmailValid(email: String?): Boolean {
        // Add your email validation logic here
        return email?.isNotEmpty() == true && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    private fun isPasswordValid(password: String?): Boolean {
        // Add your password validation logic here
        return password?.isNotEmpty() == true && password.length >= 8
    }

    fun setName(input: String) {
        _name.postValue(input)
    }

    fun setEmail(input: String) {
        _email.postValue(input)
    }

    fun setPassword(input: String) {
        _password.postValue(input)
    }

    fun register(name: String, email: String, password: String) =
        authUseCase.register(name, email, password).asLiveData()

}