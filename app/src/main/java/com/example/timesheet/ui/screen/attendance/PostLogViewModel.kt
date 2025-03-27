package com.example.timesheet.ui.screen.attendance

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesheet.data.models.LogEntry
import com.example.timesheet.data.repository.LogsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

class PostLogViewModel(private val logsRepository: LogsRepository) : ViewModel() {

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private var token: String? = null

    fun setToken(token: String) {
        this.token = token
        fetchLogs(token)
    }

    fun fetchLogs(token: String? = this.token) {
        val authToken = token ?: run {
            _errorMessage.value = "Token is missing"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val result = logsRepository.getLogsUser(authToken)
                _logs.value = result

                if (result.isEmpty()) {
                    _errorMessage.value = "No logs found."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch logs: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
