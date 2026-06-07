package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dishcovery.app.domain.model.PlanStatus

@Composable
fun ActionButtons(
    status: PlanStatus,
    isReceived: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onSave: () -> Unit,
    onClose: () -> Unit,
    onChat: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Botón principal de comunicación (siempre visible para facilitar el flujo)
        Button(
            onClick = onChat,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFA053))
        ) {
            Text(text = "Abrir Chat con el Chef", color = Color.White)
        }

        if (status == PlanStatus.PENDING) {
            if (isReceived) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Red)
                    ) {
                        Text(text = "Rechazar", color = Color.Red)
                    }
                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text(text = "Aceptar", color = Color.White)
                    }
                }
                
                Button(
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
                ) {
                    Text(text = "Guardar Propuesta", color = Color.White)
                }
            } else {
                Text(
                    text = "Esperando respuesta del otro chef...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        } else if (status == PlanStatus.ACCEPTED) {
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
            ) {
                Text(text = "Actualizar Progreso", color = Color.White)
            }
        }

        OutlinedButton(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFF8B4513))
        ) {
            Text(text = if (status == PlanStatus.PENDING && !isReceived) "Cerrar" else "Volver", color = Color(0xFF8B4513))
        }
    }
}
