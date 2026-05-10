package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dishcovery.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualMatchSection(
    modifier: Modifier = Modifier,
    onMatchClick: (recipeCategory: String, profileCategory: String) -> Unit
) {
    val learnCategories = listOf(
        stringResource(R.string.cat_italian),
        stringResource(R.string.cat_mexican),
        stringResource(R.string.cat_vegan),
        stringResource(R.string.cat_desserts),
        stringResource(R.string.cat_healthy)
    )
    val profileCategories = listOf(
        stringResource(R.string.prof_beginner),
        stringResource(R.string.prof_amateur),
        stringResource(R.string.prof_chef_pro),
        stringResource(R.string.prof_student)
    )

    var selectedLearnCategory by remember { mutableStateOf(learnCategories[0]) }
    var selectedProfileCategory by remember { mutableStateOf(profileCategories[1]) }

    Column(modifier = modifier.padding(16.dp)) {
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 24.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Text(
            text = stringResource(R.string.manual_match_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- SELECCIÓN DE CATEGORÍA DE RECETA ---
        Text(
            text = stringResource(R.string.manual_match_learn_question),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            learnCategories.forEach { category ->
                FilterChip(
                    selected = selectedLearnCategory == category,
                    onClick = { selectedLearnCategory = category },
                    label = { Text(category) },
                    leadingIcon = if (selectedLearnCategory == category) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SELECCIÓN DE PERFIL ---
        Text(
            text = stringResource(R.string.manual_match_profile_question),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            profileCategories.forEach { category ->
                FilterChip(
                    selected = selectedProfileCategory == category,
                    onClick = { selectedProfileCategory = category },
                    label = { Text(category) },
                    leadingIcon = if (selectedProfileCategory == category) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onMatchClick(selectedLearnCategory, selectedProfileCategory) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(stringResource(R.string.find_chef_button))
        }
    }
}
