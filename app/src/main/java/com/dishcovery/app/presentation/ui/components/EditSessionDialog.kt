package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dishcovery.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSessionDialog(
    session: ExchangeSession?,
    onDismiss: () -> Unit,
    onSave: (ExchangeSession) -> Unit
) {
    var title by remember { mutableStateOf(session?.title ?: "") }
    var instructor by remember { mutableStateOf(session?.instructor ?: "") }
    var date by remember { mutableStateOf(session?.date ?: "") }
    var time by remember { mutableStateOf(session?.time ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onSave(ExchangeSession(title, instructor, date, time))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.save_plan_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button), color = Color.Gray)
            }
        },
        title = {
            Text(
                text = if (session == null) "Agregar Sesión" else "Editar Sesión",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = instructor,
                    onValueChange = { instructor = it },
                    label = { Text("Instructor") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Fecha") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Horario") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White
    )
}
