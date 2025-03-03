package com.example.timesheet.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ClockInOutViewModel : ViewModel() {
    private var _elapsedTime = 0L
    val elapsedTime: Long get() = _elapsedTime

    private var _isClockedIn = false
    val isClockedIn: Boolean get() = _isClockedIn

    private var job: Job? = null

    fun toggleClockInOut() {
        if (_isClockedIn) {
            stopTimer()
        } else {
            startTimer()
        }
        _isClockedIn = !_isClockedIn
    }

    private fun startTimer() {
        job?.cancel()
        job = viewModelScope.launch {
            while (true) {
                delay(1000)
                _elapsedTime++
            }
        }
    }

    private fun stopTimer() {
        job?.cancel()
    }
}

