package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dishcovery.app.R

@Composable
fun ActionButtons(onAccept: () -> Unit, onSave: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onSave,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF8B4513))
        ) {
            Text(text = stringResource(R.string.save_plan_button), color = Color(0xFF8B4513))
        }
        Button(
            onClick = onAccept,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
        ) {
            Text(text = stringResource(R.string.accept_plan_button))
        }
    }
}
