package com.example.timesheet.ui.screen.login


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesheet.TokenManager
import com.example.timesheet.data.data_class.User
import com.example.timesheet.data.data_class.UserLogs
import com.example.timesheet.data.repository.LogsRepository
import com.example.timesheet.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository = UserRepository(),
    private val logsRepository: LogsRepository = LogsRepository(),
    private val tokenManager: TokenManager

) : ViewModel() {

    private val _details = MutableStateFlow<User?>(null)
    val details = _details.asStateFlow()

    private val _logs = MutableStateFlow<List<UserLogs>>(emptyList())
    val logs = _logs.asStateFlow()

    fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val user = userRepository.loginUser (email, password)

                if (user.status == "success") {
                    _details.value = user
                    fetchLogs(user.response.token)
                    tokenManager.saveToken(user.response.token) // Save the token
                    onSuccess(user.response.token)
                } else {
                    onFailure("Login failed: ${user.status}")
                }
            } catch (e: Exception) {
                onFailure("Incorrect Password or Email")
            }
        }
    }
    private fun fetchLogs(token: String) {
        viewModelScope.launch {
            try {
                val logsResponse = logsRepository.getLogs(token)
                _logs.value = logsResponse
            } catch (e: Exception) {
                Log.e("LoginViewmodel", " fetching logs error", e)
            }
        }
    }


}