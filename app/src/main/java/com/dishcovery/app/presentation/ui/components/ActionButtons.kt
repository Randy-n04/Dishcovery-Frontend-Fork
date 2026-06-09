package com.dishcovery.app.presentation.ui.components

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
    isSaving: Boolean,
    saveSuccess: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onSave: () -> Unit,
    onClose: () -> Unit,
    onChat: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (status == PlanStatus.PENDING || status == PlanStatus.ACCEPTED) {
            SaveProgressButton(
                textDefault = "Guardar Progreso",
                isSaving = isSaving,
                saveSuccess = saveSuccess,
                onSave = onSave
            )
        }

        Button(
            onClick = onChat,
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEFA053),
                disabledContainerColor = Color(0xFFEFA053),
                contentColor = Color.White,
                disabledContentColor = Color.White
            )
        ) {
            Text(text = "Abrir Chat con el Chef")
        }
    }
}

@Composable
private fun SaveProgressButton(
    textDefault: String,
    isSaving: Boolean,
    saveSuccess: Boolean,
    onSave: () -> Unit
) {
    Button(
        onClick = onSave,
        enabled = !isSaving && !saveSuccess,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                saveSuccess -> Color(0xFF66BB6A)
                else -> Color(0xFF8B4513)
            },
            disabledContainerColor = when {
                saveSuccess -> Color(0xFF66BB6A)
                else -> Color(0xFF8B4513)
            },
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(
            text = when {
                isSaving -> "Guardando..."
                saveSuccess -> "Guardado"
                else -> textDefault
            }
        )
    }
}
