package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.dishcovery.app.presentation.ui.theme.Rojo

@Composable
fun EditAccountDialog(
    currentUsername: String,
    isSaving: Boolean,
    errorFromServer: String? = null,
    onDismiss: () -> Unit,
    onSave: (
        username: String?,
        currentPassword: String,
        newPassword: String?
    ) -> Unit
) {
    var username by remember { mutableStateOf(currentUsername) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    val displayError = localError ?: errorFromServer

    AlertDialog(
        onDismissRequest = {
            if (!isSaving) onDismiss()
        },
        title = {
            Text(
                text = "Ajustes de cuenta",
                fontWeight = FontWeight.Bold,
                color = Rojo
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { if (it.length <= 12) username = it },
                    label = { Text("Nombre de usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("${username.length}/12") },
                    isError = username.isNotBlank() && (username.length < 6 || username.length > 12)
                )

                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { if (it.length <= 12) currentPassword = it },
                    label = { Text("Contraseña actual") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { if (it.length <= 12) newPassword = it },
                    label = { Text("Nueva contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { if (it.length <= 12) confirmPassword = it },
                    label = { Text("Confirmar nueva contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                displayError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = !isSaving,
                onClick = {
                    localError = null

                    // Validaciones de longitud: username y passwords entre 6 y 12 caracteres
                    if (username.isNotBlank() && (username.length < 6 || username.length > 12)) {
                        localError = "El nombre de usuario debe tener entre 6 y 12 caracteres."
                        return@Button
                    }

                    if (currentPassword.isBlank()) {
                        localError = "Debes escribir tu contraseña actual."
                        return@Button
                    }

                    if (currentPassword.length < 6 || currentPassword.length > 12) {
                        localError = "La contraseña debe tener entre 6 y 12 caracteres."
                        return@Button
                    }

                    if (newPassword.isNotBlank()) {
                        if (newPassword.length < 6 || newPassword.length > 12) {
                            localError = "La nueva contraseña debe tener entre 6 y 12 caracteres."
                            return@Button
                        }

                        if (newPassword != confirmPassword) {
                            localError = "La nueva contraseña no coincide."
                            return@Button
                        }
                    }

                    onSave(
                        username.takeIf { it.isNotBlank() && it != currentUsername },
                        currentPassword,
                        newPassword.takeIf { it.isNotBlank() }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Rojo),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (isSaving) "Guardando..." else "Guardar")
            }
        },
        dismissButton = {
            TextButton(
                enabled = !isSaving,
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}
