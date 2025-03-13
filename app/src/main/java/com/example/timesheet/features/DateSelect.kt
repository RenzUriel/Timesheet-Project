package com.example.timesheet.features

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermanentDatePicker(onDateSelected: (Long?) -> Unit) {
    val datePickerState = rememberDatePickerState()
    val context = LocalContext.current
    var selectedDateText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display selected date in TextField
        OutlinedTextField(
            value = selectedDateText,
            onValueChange = { selectedDateText = it },
            label = { Text("Selected Date") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        DatePicker(
            state = datePickerState,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = {
                val selectedDate = datePickerState.selectedDateMillis
                selectedDate?.let {
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                    selectedDateText = formattedDate
                    Toast.makeText(context, "Selected Date: $formattedDate", Toast.LENGTH_SHORT).show()
                    onDateSelected(selectedDate)
                } ?: Toast.makeText(context, "No Date Selected", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Confirm Date")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPermanentDatePicker() {
    PermanentDatePicker(
        onDateSelected = { /* Sample action */ }
    )
}