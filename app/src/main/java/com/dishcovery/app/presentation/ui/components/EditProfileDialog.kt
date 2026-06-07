package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var bio by remember { mutableStateOf(profile.bio) }
    var photoUrl by remember { mutableStateOf(profile.photoUrl) }
    var language by remember { mutableStateOf(profile.language) }
    var location by remember { mutableStateOf(profile.location) }

    var offeredSkills by remember { mutableStateOf(profile.offeredSkills) }
    var skillsToLearn by remember { mutableStateOf(profile.skillsToLearn) }
    
    var localError by remember { mutableStateOf<String?>(null) }

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
                    onValueChange = { if (it.length <= 200) publicName = it },
                    label = { Text("Nombre público") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("${publicName.length}/200") },
                    isError = publicName.length > 200
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { if (it.length <= 200) bio = it },
                    label = { Text("Biografía") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    supportingText = { Text("${bio.length}/200") },
                    isError = bio.length > 200
                )

                OutlinedTextField(
                    value = language,
                    onValueChange = { if (it.length <= 200) language = it },
                    label = { Text("Idioma") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("${language.length}/200") },
                    isError = language.length > 200
                )

                OutlinedTextField(
                    value = location,
                    onValueChange = { if (it.length <= 200) location = it },
                    label = { Text("Ubicación") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("${location.length}/200") },
                    isError = location.length > 200
                )

                localError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

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
            }
        },
        confirmButton = {
            Button(
                enabled = !isSaving,
                onClick = {
                    if (publicName.length > 200 || bio.length > 200 || language.length > 200 || location.length > 200) {
                        localError = "Los campos no deben superar los 200 caracteres."
                    } else {
                        onSave(
                            publicName,
                            bio,
                            photoUrl,
                            offeredSkills,
                            skillsToLearn,
                            language,
                            location
                        )
                    }
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
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}
