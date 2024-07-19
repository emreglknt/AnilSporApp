package com.example.recipeapppaparaproject.presentation.AuthViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapppaparaproject.data.repo.AuthRepository
import com.example.recipeapppaparaproject.data.repo.Result
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {


    private val _loginResult =
        MutableStateFlow<com.example.recipeapppaparaproject.data.repo.Result<FirebaseUser?>?>(null)
         val loginResult: MutableStateFlow<Result<FirebaseUser?>?> = _loginResult

    private val _registerResult =
        MutableStateFlow<com.example.recipeapppaparaproject.data.repo.Result<FirebaseUser?>?>(null)
    val registerResult: MutableStateFlow<com.example.recipeapppaparaproject.data.repo.Result<FirebaseUser?>?> =
        _registerResult


    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _loginResult.value = result
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signup(email, password)
            _registerResult.value = result
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }


}