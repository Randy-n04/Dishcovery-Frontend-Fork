package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dishcovery.app.R

@Composable
fun SuggestionsSection(text: String, onValueChange: (String) -> Unit) {
    Column {
        Text(
            text = stringResource(R.string.suggestions_label),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B4513),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.suggestions_hint)) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF8B4513),
                unfocusedBorderColor = Color.LightGray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* Process mock */ },
            modifier = Modifier.align(Alignment.End),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
        ) {
            Text(text = stringResource(R.string.process_plan_button))
        }
    }
}
