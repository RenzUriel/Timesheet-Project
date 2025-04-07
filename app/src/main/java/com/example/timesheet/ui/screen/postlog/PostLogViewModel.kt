package com.example.timesheet.ui.screen.postlog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesheet.data.data_class.UserLogs
import com.example.timesheet.data.repository.LogsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostLogViewModel(
    private val logsRepository: LogsRepository = LogsRepository()
) : ViewModel() {

    private val _logs = MutableStateFlow<List<UserLogs>>(emptyList())
    val logs = _logs.asStateFlow()

    fun fetchLogs(token: String) {
        viewModelScope.launch {
            try {
                val logsResponse = logsRepository.getLogs(token)
                _logs.value = logsResponse
            } catch (e: Exception) {
                Log.e("PostLogViewModel", "Error fetching logs", e)
            }
        }
    }
}
