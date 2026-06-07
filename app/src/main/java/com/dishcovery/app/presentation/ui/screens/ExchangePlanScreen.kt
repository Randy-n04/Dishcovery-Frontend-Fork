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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.ExchangeSession
import com.dishcovery.app.domain.model.PlanStatus
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.components.*
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.viewmodel.ExchangePlanViewModel
import com.dishcovery.app.presentation.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangePlanScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    planId: Long?,
    chatId: Long?,
    authViewModel: AuthViewModel,
    viewModel: ExchangePlanViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val user by authViewModel.user.collectAsState()
    val plan by viewModel.plan.collectAsState()
    val partnerProfile by viewModel.partnerProfile.collectAsState()
    val chats by chatViewModel.chats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(planId, chatId, user) {
        user?.let { u ->
            if (planId != null) {
                viewModel.loadPlan(planId, u.id)
            } else if (chatId != null) {
                viewModel.loadPlanByChatId(chatId, u.id)
            }
            chatViewModel.loadChats(u.id)
        }
    }

    val currentPlan = plan

    // Lógica de auto-aceptación si el usuario es el receptor y el plan está PENDING
    LaunchedEffect(currentPlan, user) {
        if (currentPlan != null && user != null) {
            val isCreator = currentPlan.creatorId == user!!.id
            if (!isCreator && currentPlan.status == PlanStatus.PENDING) {
                viewModel.acceptPlan { }
            }
        }
    }

    // Determinar si el usuario actual es el "receptor" (compañero)
    val effectivelyIsReceived = currentPlan?.let { it.creatorId != user?.id } ?: false

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
                    IconButton(onClick = { 
                        navController.navigate(NavRoutes.Home.ROUTE) {
                            popUpTo(NavRoutes.Home.ROUTE) { inclusive = true }
                        }
                    }) {
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
                // Filtro de seguridad que transforma mensajes técnicos en amigables
                val displayError = formatErrorMessage(errorMessage!!)
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = displayError,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { 
                            user?.let { u ->
                                if (planId != null) viewModel.loadPlan(planId, u.id)
                                else if (chatId != null) viewModel.loadPlanByChatId(chatId, u.id)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B4513))
                    ) {
                        Text(stringResource(R.string.retry_button))
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
                        text = stringResource(R.string.no_plans_available),
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
                            partnerName = partnerProfile?.publicName ?: currentPlan.partnerName,
                            partnerImageUrl = partnerProfile?.photoUrl ?: currentPlan.partnerImageUrl
                        )
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
                            canEdit = false, 
                            onEditClick = { },
                            onToggleCompletion = {
                                viewModel.toggleSessionCompletion(index)
                            }
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
                                status = currentPlan.status,
                                isReceived = effectivelyIsReceived,
                                onAccept = { },
                                onReject = {
                                    viewModel.rejectPlan {
                                        navController.navigate(NavRoutes.Home.ROUTE) {
                                            popUpTo(NavRoutes.Home.ROUTE) { inclusive = true }
                                        }
                                    }
                                },
                                onSave = {
                                    viewModel.savePlan()
                                },
                                onClose = {
                                    navController.navigate(NavRoutes.Home.ROUTE) {
                                        popUpTo(NavRoutes.Home.ROUTE) { inclusive = true }
                                    }
                                },
                                onChat = {
                                    val associatedChat = chats.find { it.planId == currentPlan.id || it.id == chatId }
                                    if (associatedChat != null) {
                                        navController.navigate(NavRoutes.ChatDetail.createRoute(associatedChat.id, associatedChat.name))
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

/**
 * Filtro final de seguridad para evitar mensajes técnicos de Java/GSON en la UI.
 */
private fun formatErrorMessage(rawError: String): String {
    val lowError = rawError.lowercase()
    return if (lowError.contains("end of input") || 
               lowError.contains("path $") || 
               lowError.contains("malformed") || 
               lowError.contains("expected")) {
        "El servidor envió información incompleta. Por favor, intenta de nuevo en unos momentos."
    } else {
        rawError
    }
}
