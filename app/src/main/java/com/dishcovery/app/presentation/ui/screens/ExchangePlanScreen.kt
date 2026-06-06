package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.ExchangeSession
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.components.*
import com.dishcovery.app.presentation.viewmodel.ExchangePlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangePlanScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    isReceived: Boolean = true,
    planId: Long? = null,
    viewModel: ExchangePlanViewModel = hiltViewModel()
) {
    val plan by viewModel.plan.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedSessionIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(planId) {
        viewModel.loadPlan(planId)
    }

    val currentPlan = plan
    val sessionToEdit: ExchangeSession? =
        if (selectedSessionIndex != -1) currentPlan?.sessions?.getOrNull(selectedSessionIndex) else null

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

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF8B4513))
                }
            }

            errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = errorMessage ?: "Error desconocido",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.loadPlan(planId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            currentPlan == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay planes de intercambio disponibles.",
                        color = Color.Gray
                    )
                }
            }

            else -> {
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
                            partnerName = currentPlan.partnerName,
                            partnerImageUrl = currentPlan.partnerImageUrl
                        )
                    }

                    if (!currentPlan.aiSuggestion.isNullOrBlank()) {
                        item {
                            AISuggestionBanner(suggestion = currentPlan.aiSuggestion)
                        }
                    }

                    item {
                        ProgressSection(
                            progress = currentPlan.progress,
                            total = if (currentPlan.totalSessions > 0) {
                                currentPlan.totalSessions
                            } else {
                                currentPlan.sessions.size
                            }
                        )
                    }

                    itemsIndexed(currentPlan.sessions) { index, session ->
                        SessionCard(
                            session = session,
                            canEdit = isReceived && !isSaving,
                            onEditClick = {
                                selectedSessionIndex = index
                                showDialog = true
                            }
                        )
                    }

                    item {
                        SuggestionsSection(
                            text = currentPlan.userNote,
                            onValueChange = { viewModel.updateUserNote(it) },
                            enabled = isReceived && !isSaving
                        )
                    }

                    item {
                        if (isSaving) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFF8B4513))
                            }
                        } else {
                            ActionButtons(
                                onAccept = {
                                    if (isReceived) {
                                        viewModel.acceptPlan {
                                            navController.navigate(NavRoutes.Chats.ROUTE) {
                                                popUpTo(NavRoutes.Home.ROUTE)
                                            }
                                        }
                                    } else {
                                        navController.popBackStack()
                                    }
                                },
                                onSave = {
                                    viewModel.savePlan()
                                },
                                isReceived = isReceived
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    if (showDialog && isReceived) {
        EditSessionDialog(
            session = sessionToEdit,
            onDismiss = { showDialog = false },
            onSave = { updated ->
                if (selectedSessionIndex != -1) {
                    viewModel.updateSession(selectedSessionIndex, updated)
                }
                showDialog = false
            }
        )
    }
}