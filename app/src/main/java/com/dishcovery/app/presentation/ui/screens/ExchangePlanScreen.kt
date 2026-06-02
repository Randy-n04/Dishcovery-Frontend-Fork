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
import com.dishcovery.app.domain.model.ExchangePlan
import com.dishcovery.app.domain.model.ExchangeSession
import com.dishcovery.app.domain.model.PlanStatus
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangePlanScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    isReceived: Boolean = true
) {
    // 1. Estado del plan usando la Clase de Dominio
    var plan by remember {
        mutableStateOf(
            ExchangePlan(
                id = 1,
                partnerId = 101,
                partnerName = "Javier Rodriguez",
                partnerImageUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=Javier",
                aiSuggestion = "Balancea 2 sesiones de intercambio con dificultad progresiva. Cada sesión construida se basa en las habilidades adquiridas en la anterior.",
                sessions = listOf(
                    ExchangeSession(1, "Pasta italiana nivel principiante", "Javier", "Marzo 15, 2026", "6:00 PM - 7:30 PM"),
                    ExchangeSession(2, "Tacos tradicionales y salsas", "ti", "Marzo 16, 2026", "6:00 PM - 7:30 PM")
                ),
                status = PlanStatus.PENDING
            )
        )
    }

    // Biblioteca de sugerencias para el simulador de IA
    val aiLibrary = listOf(
        ExchangeSession(0, "Postres Gourmet Italianos", "Javier", "Marzo 20, 2026", "4:00 PM - 6:00 PM"),
        ExchangeSession(0, "Maridaje de Vinos", "ti", "Marzo 22, 2026", "7:00 PM - 8:30 PM")
    )

    var showDialog by remember { mutableStateOf(false) }
    var selectedSessionIndex by remember { mutableIntStateOf(-1) }
    val sessionToEdit = if (selectedSessionIndex != -1) plan.sessions.getOrNull(selectedSessionIndex) else null

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
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { 
                PartnerCard(
                    partnerName = plan.partnerName, 
                    partnerImageUrl = plan.partnerImageUrl
                ) 
            }
            
            item { AISuggestionBanner(suggestion = plan.aiSuggestion) }

            item { 
                ProgressSection(
                    progress = plan.progress, 
                    total = plan.sessions.size
                ) 
            }

            itemsIndexed(plan.sessions) { index, session ->
                SessionCard(
                    session = session,
                    canEdit = isReceived,
                    onEditClick = {
                        selectedSessionIndex = index
                        showDialog = true
                    }
                )
            }

            if (isReceived) {
                item {
                    GenerateSessionButton(onGenerate = {
                        val next = aiLibrary.random()
                        plan = plan.copy(sessions = plan.sessions + next)
                    })
                }
            }

            item {
                SuggestionsSection(
                    text = plan.userNote, 
                    onValueChange = { plan = plan.copy(userNote = it) },
                    enabled = isReceived
                )
            }
            
            item {
                ActionButtons(
                    onAccept = {
                        navController.navigate(NavRoutes.Chats.ROUTE) {
                            popUpTo(NavRoutes.Home.ROUTE)
                        }
                    },
                    onSave = { /* Update API mock */ },
                    isReceived = isReceived
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showDialog && isReceived) {
        EditSessionDialog(
            session = sessionToEdit,
            onDismiss = { showDialog = false },
            onSave = { updated ->
                val newList = plan.sessions.toMutableList()
                if (selectedSessionIndex != -1) {
                    newList[selectedSessionIndex] = updated
                } else {
                    newList.add(updated)
                }
                plan = plan.copy(sessions = newList)
                showDialog = false
            }
        )
    }
}
