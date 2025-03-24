package com.example.timesheet.ui.screen.login


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesheet.data.User
import com.example.timesheet.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository = UserRepository(),
) : ViewModel() {

    private val _details = MutableStateFlow<User?>(null)

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val user = userRepository.loginUser(email, password)

                Log.d("LoginViewModel", "Login response status: ${user.status}")
                Log.d("LoginViewModel", "Login response token: ${user.response.token}")

                if (user.status == "success") {
                    _details.value = user
                    onSuccess()
                } else {
                    onFailure("Login failed: ${user.status}")
                    Log.d("LoginViewModel", "Login failed with status: ${user.status}")
                }
            } catch (e: Exception) {
                onFailure("Incorrect Password or Email")
                Log.e("LoginViewModel", "Login exception: ${e.message}")
            }
        }
    }




}