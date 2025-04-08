package com.example.timesheet.ui.screen.homescreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class HomeViewModel : ViewModel() {
    var isClockedIn = mutableStateOf(false)
    var elapsedTime = mutableStateOf(0L)
    var startTime = mutableStateOf(0L)

    fun startClock() {
        isClockedIn.value = true
        startTime.value = System.currentTimeMillis()
    }

    fun stopClock() {
        isClockedIn.value = false
        startTime.value = 0L
        elapsedTime.value = 0L
    }

    fun updateElapsedTime() {
        if (isClockedIn.value) {
            elapsedTime.value = (System.currentTimeMillis() - startTime.value) / 1000
        }
    }
}