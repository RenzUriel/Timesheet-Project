package com.example.timesheet.ui.screen.login


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timesheet.data.models.LogEntry
import com.example.timesheet.data.models.User
import com.example.timesheet.data.repository.LogsRepository
import com.example.timesheet.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
class LoginViewModel(
    private val userRepository: UserRepository = UserRepository(),
    private val logsRepository: LogsRepository = LogsRepository(),
) : ViewModel() {

    private val _details = MutableStateFlow<User?>(null)
    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs = _logs.asStateFlow()

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Attempt to login and get the user response
                val user = userRepository.loginUser(email, password)

                Log.d("LoginViewModel", "Login response status: ${user.status}")
                Log.d("LoginViewModel", "Login response token: ${user.response.token}")

                if (user.status == "success") {
                    // Store the user details and token
                    _details.value = user
                    val token = user.token // Fetch the token from user response
                    fetchLogs(token) // Pass the token to fetch logs

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

    private fun fetchLogs(token: String) {
        viewModelScope.launch {
            try {
                // Pass the token to the API to fetch logs
                val logsResponse = logsRepository.getLogsUser(token)
                _logs.value = logsResponse
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 401) {
                    Log.e("LoginViewModel", "Unauthorized - Token might be invalid or expired")
                    // Handle re-login or token refresh here
                }
                Log.d("error", "Failed to fetch logs: ${e.message}")
            }
        }
    }
}
