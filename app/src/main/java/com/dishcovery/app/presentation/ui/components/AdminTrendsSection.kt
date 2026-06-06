package com.dishcovery.app.presentation.ui.components


import android.R.attr.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.presentation.ui.theme.Naranja
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary
import com.dishcovery.app.presentation.viewmodel.AdminCategoryViewModel
import kotlinx.coroutines.delay


@Composable
fun AdminTrendsSection(
    viewModel: AdminCategoryViewModel = hiltViewModel()
) {
    val categories  by viewModel.categories.collectAsState()
    val isLoading   by viewModel.isLoading.collectAsState()
    val error       by viewModel.error.collectAsState()
    val success     by viewModel.success.collectAsState()

    var expanded        by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<KitchenCategory?>(null) }
    var sevenDayActive  by remember { mutableStateOf(true) }

    // Feedback de éxito
    LaunchedEffect(success) {
        if (success) {
            delay(2000)
            viewModel.resetSuccess()
        }
    }

    AdminSection(stringResource(R.string.add_trend_title)) {

        // Dropdown de categorías
        Text(
            stringResource(R.string.enter_name_label),
            fontSize = 13.sp,
            color = TextPrimary.copy(.7f)
        )
        Box {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .border(1.dp, TextPrimary.copy(.2f), RoundedCornerShape(6.dp))
                    .clickable { expanded = true }
                    .padding(12.dp, 10.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(
                    text = selectedCategory?.name
                        ?: stringResource(R.string.no_trends_available),
                    color = if (selectedCategory != null) TextPrimary
                    else TextPrimary.copy(.4f),
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextPrimary.copy(.4f)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (categories.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.no_trends_available), fontSize = 13.sp) },
                        onClick = { expanded = false }
                    )
                } else {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name, fontSize = 14.sp) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // Toggle 7 días
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.activate_7_days_label),
                fontSize = 13.sp,
                color = TextPrimary.copy(.6f)
            )
            Switch(
                checked = sevenDayActive,
                onCheckedChange = { sevenDayActive = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor   = Color.White,
                    checkedTrackColor   = Rojo,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = TextPrimary.copy(.3f)
                )
            )
        }

        // Feedback
        AnimatedVisibility(visible = success) {
            Text(
                stringResource(R.string.trend_success),
                color = Color(0xFF4CAF50),
                fontSize = 13.sp
            )
        }
        if (error != null) {
            Text(error!!, color = Rojo, fontSize = 13.sp)
        }

        // Botón Agregar
        Button(
            onClick = {
                selectedCategory?.id?.let { id ->
                    val days = if (sevenDayActive) 7 else 30
                    viewModel.setTrending(id, days)
                }
            },
            enabled  = selectedCategory != null && !isLoading,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape    = RoundedCornerShape(10.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = Rojo)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text(
                    stringResource(R.string.add_button),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}