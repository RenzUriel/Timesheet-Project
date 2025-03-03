package com.example.timesheet.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.timesheet.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityToggle: (() -> Unit)? = null,
    imeAction: ImeAction
) {
    //    Text(
//        text = label,
//        style = MaterialTheme.typography.labelLarge,
//        fontWeight = FontWeight.Bold,
//        modifier = Modifier.fillMaxWidth()
//    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Enter ${label ?: "input"}", color = Color(0xFF1E1E1E) ) }, // Set label color to #1E1E1E
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color(0xFFF1F4FF),
            focusedBorderColor = Color(0xFF6973C4),
            unfocusedBorderColor = Color(0xFF6973C4),
            focusedLabelColor = Color(0xFF6973C4),
            unfocusedLabelColor = Color(0xFF6973C4)
        ),
        shape = RoundedCornerShape(10.dp), // Rounded border
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword && onVisibilityToggle != null) {
            {
                val icon = if (passwordVisible) R.drawable.visibility else R.drawable.visibilityoff
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    modifier = Modifier.clickable { onVisibilityToggle() }.size(20.dp)
                )
            }
        } else null
    )
}
