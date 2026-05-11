package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dishcovery.app.R
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangePlanScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    isReceived: Boolean = true // true if we received it (can edit), false if we sent it (readonly)
) {
    // 1. Estado dinámico de las sesiones
    val sessions = remember {
        mutableStateListOf(
            ExchangeSession("Pasta italiana nivel principiante", "Javier", "Marzo 15, 2026", "6:00 PM - 7:30 PM"),
            ExchangeSession("Tacos tradicionales y salsas", "ti", "Marzo 16, 2026", "6:00 PM - 7:30 PM")
        )
    }

    // Sugerencias de la IA (Simulado)
    val aiSuggestions = listOf(
        ExchangeSession("Postres Gourmet Italianos", "Javier", "Marzo 20, 2026", "4:00 PM - 6:00 PM"),
        ExchangeSession("Maridaje de Vinos", "ti", "Marzo 22, 2026", "7:00 PM - 8:30 PM")
    )

    var suggestionsText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedSessionIndex by remember { mutableIntStateOf(-1) }
    val sessionToEdit = if (selectedSessionIndex != -1) sessions.getOrNull(selectedSessionIndex) else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.exchange_plan_title),
                        color = Color(0xFF8B4513),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFFBE6))
            )
        },
        containerColor = Color(0xFFFFFBE6)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { PartnerCard() }
            item { AISuggestionBanner() }
            item { ProgressSection() }

            // Lista de sesiones dinámicas
            itemsIndexed(sessions) { index, session ->
                SessionCard(
                    session = session,
                    canEdit = isReceived, // Solo se puede editar si es recibido
                    onEditClick = {
                        selectedSessionIndex = index
                        showDialog = true
                    }
                )
            }

            // Solo mostramos el botón de generar si es un plan recibido (editable)
            if (isReceived) {
                item {
                    GenerateSessionButton(onGenerate = {
                        val next = aiSuggestions.getOrNull(sessions.size - 2) ?: aiSuggestions.random()
                        sessions.add(next)
                    })
                }
            }

            // Sección de sugerencias del usuario
            item {
                SuggestionsSection(
                    text = suggestionsText, 
                    onValueChange = { suggestionsText = it },
                    enabled = isReceived // Solo editable si es recibido
                )
            }
            
            item {
                ActionButtons(
                    onAccept = {
                        navController.navigate(NavRoutes.Chats.ROUTE) {
                            popUpTo(NavRoutes.Home.ROUTE)
                        }
                    },
                    onSave = { /* Simulación de guardado */ },
                    isReceived = isReceived
                )
            }
        }
    }

    if (showDialog && isReceived) {
        EditSessionDialog(
            session = sessionToEdit,
            onDismiss = { showDialog = false },
            onSave = { updated ->
                if (selectedSessionIndex != -1) sessions[selectedSessionIndex] = updated
                else sessions.add(updated)
                showDialog = false
            }
        )
    }
}
