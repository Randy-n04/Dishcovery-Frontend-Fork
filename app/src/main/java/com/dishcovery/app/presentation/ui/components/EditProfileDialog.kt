package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.model.Profile
import com.dishcovery.app.presentation.ui.theme.Rojo

@Composable
fun EditProfileDialog(
    profile: Profile,
    categories: List<KitchenCategory>,
    isSaving: Boolean,
    errorFromServer: String? = null,
    saveSuccess: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (
        publicName: String,
        bio: String,
        photoUrl: String,
        offeredSkills: List<KitchenCategory>,
        skillsToLearn: List<KitchenCategory>,
        language: String,
        location: String
    ) -> Unit
) {
    var publicName by remember { mutableStateOf(profile.publicName) }
    // Normalizamos la bio: si viene como mensajes por defecto en inglés o español la dejamos vacía
    val bioNormalized = if (profile.bio.isBlank()) {
        ""
    } else {
        val lower = profile.bio.lowercase().trim()
        when (lower) {
            "no bio available", "bibliografía no disponible", "no bibliografía disponible" -> ""
            else -> profile.bio
        }
    }
    var bio by remember { mutableStateOf(bioNormalized) }
    var photoUrl by remember { mutableStateOf(profile.photoUrl) }
    var language by remember { mutableStateOf(profile.language) }
    var location by remember { mutableStateOf(profile.location) }

    var offeredSkills by remember { mutableStateOf(profile.offeredSkills) }
    var skillsToLearn by remember { mutableStateOf(profile.skillsToLearn) }
    
    // Errores por campo para mostrarlos justo debajo del input correspondiente
    var nameError by remember { mutableStateOf<String?>(null) }
    var languageError by remember { mutableStateOf<String?>(null) }
    var locationError by remember { mutableStateOf<String?>(null) }
    var offeredSkillsError by remember { mutableStateOf<String?>(null) }
    var skillsToLearnError by remember { mutableStateOf<String?>(null) }
    // Estado del botón: 0 idle, 1 saving, 2 saved
    var buttonState by remember { mutableStateOf(0) }

    // Observamos isSaving: cuando pase de true a false y no haya errores mostramos "Guardado" y cerramos
    LaunchedEffect(isSaving, nameError, languageError, locationError, offeredSkillsError, skillsToLearnError, errorFromServer, saveSuccess) {
        if (isSaving) {
            buttonState = 1
        } else {
            if (buttonState == 1) {
                // si no hay errores de campo, consideramos éxito (la verificación final la hace el servidor)
                val hasFieldErrors = listOf(nameError, languageError, locationError, offeredSkillsError, skillsToLearnError).any { it != null }
                // cerrar solo si el ViewModel indicó saveSuccess y no hay errores de campo ni errorFromServer
                if (!hasFieldErrors && saveSuccess && errorFromServer == null) {
                    buttonState = 2
                    kotlinx.coroutines.delay(1500)
                    onDismiss()
                    buttonState = 0
                } else {
                    buttonState = 0
                }
            }
        }
    }

    AlertDialog(
        onDismissRequest = {
            if (!isSaving) onDismiss()
        },
        title = {
            Text(
                text = "Editar perfil",
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
                    value = publicName,
                    onValueChange = { if (it.length <= 30) publicName = it },
                    label = { Text("Nombre público") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("${publicName.length}/30") },
                    isError = publicName.isBlank() || publicName.length > 30
                )
                // Mensaje de error justo debajo del campo
                if (nameError != null) {
                    Text(nameError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                OutlinedTextField(
                    value = bio,
                    onValueChange = { if (it.length <= 200) bio = it },
                    label = { Text("Biografía") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    supportingText = { Text("${bio.length}/200") },
                    isError = bio.length > 200
                )
                // No mostramos mensaje de error para bio; el perfil mostrará "No bibliografía disponible" si queda vacía

                OutlinedTextField(
                    value = language,
                    onValueChange = { if (it.length <= 20) language = it },
                    label = { Text("Idioma") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("${language.length}/20") },
                    isError = language.isBlank() || language.length > 20
                )
                if (languageError != null) {
                    Text(languageError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { if (it.length <= 30) location = it },
                    label = { Text("Ubicación") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("${location.length}/30") },
                    isError = location.isBlank() || location.length > 30
                )
                if (locationError != null) {
                    Text(locationError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                // Mensajes de error para secciones de habilidades se muestran después de cada LazyRow

                Text("Habilidades que ofreces", fontWeight = FontWeight.SemiBold)

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { category ->
                        val selected = offeredSkills.any { it.id == category.id }

                        FilterChip(
                            selected = selected,
                            onClick = {
                                offeredSkills = if (selected) {
                                    offeredSkills.filterNot { it.id == category.id }
                                } else {
                                    offeredSkills + category
                                }
                            },
                            label = { Text(category.name) }
                        )
                    }
                }
                if (offeredSkillsError != null) {
                    Text(offeredSkillsError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Text("Habilidades que quieres aprender", fontWeight = FontWeight.SemiBold)

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { category ->
                        val selected = skillsToLearn.any { it.id == category.id }

                        FilterChip(
                            selected = selected,
                            onClick = {
                                skillsToLearn = if (selected) {
                                    skillsToLearn.filterNot { it.id == category.id }
                                } else {
                                    skillsToLearn + category
                                }
                            },
                            label = { Text(category.name) }
                        )
                    }
                }
                if (skillsToLearnError != null) {
                    Text(skillsToLearnError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                enabled = !isSaving,
                onClick = {
                    // Resetear errores
                    nameError = null
                    languageError = null
                    locationError = null
                    offeredSkillsError = null
                    skillsToLearnError = null

                    // Validaciones por campo (mensajes cortos y directos)
                    if (publicName.isBlank()) {
                        nameError = "Nombre requerido"
                        return@Button
                    }
                    if (publicName.length > 30) {
                        nameError = "Nombre máximo 30"
                        return@Button
                    }

                    if (language.isBlank()) {
                        languageError = "Idioma requerido"
                        return@Button
                    }
                    if (language.length > 20) {
                        languageError = "Idioma máximo 20"
                        return@Button
                    }

                    if (location.isBlank()) {
                        locationError = "Ubicación requerida"
                        return@Button
                    }
                    if (location.length > 30) {
                        locationError = "Ubicación máximo 30"
                        return@Button
                    }

                    if (offeredSkills.isEmpty()) {
                        offeredSkillsError = "Marca al menos una habilidad"
                        return@Button
                    }

                    if (skillsToLearn.isEmpty()) {
                        skillsToLearnError = "Marca al menos una habilidad para aprender"
                        return@Button
                    }

                    // Si pasa todas las validaciones, marcamos botón como guardando y llamamos onSave
                    buttonState = 1
                    onSave(
                        publicName,
                        bio,
                        photoUrl,
                        offeredSkills,
                        skillsToLearn,
                        language,
                        location
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (buttonState == 2) Color(0xFF66BB6A) else Rojo, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                when (buttonState) {
                    1 -> Text("Guardando...")
                    2 -> Text("Guardado", color = Color.White)
                    else -> Text("Guardar")
                }
            }
        },
        dismissButton = {
            TextButton(
                enabled = !isSaving,
                onClick = onDismiss
            ) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}
