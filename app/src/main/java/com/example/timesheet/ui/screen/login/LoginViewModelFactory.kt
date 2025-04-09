package com.example.timesheet.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timesheet.TokenManager

class LoginViewModelFactory(private val tokenManager: TokenManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(tokenManager = tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}