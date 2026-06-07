package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dishcovery.app.domain.model.ExchangePlan
import com.dishcovery.app.domain.model.PlanStatus
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.theme.Cafe
import com.dishcovery.app.presentation.ui.theme.Primary
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.viewmodel.ExchangePlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanRequestsScreen(
    navController: NavController,
    viewModel: ExchangePlanViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val user by authViewModel.user.collectAsState(initial = null)
    val plans by viewModel.allPlans.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState(initial = false)
    val errorMessage by viewModel.errorMessage.collectAsState(initial = null)

    LaunchedEffect(user) {
        viewModel.loadAllPlans()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buzón de Planes", color = Cafe, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Rojo)
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        viewModel.loadAllPlans() 
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar", tint = Cafe)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFFBE6))
            )
        },
        containerColor = Color(0xFFFFFBE6)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading && plans.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Primary)
            } else if (errorMessage != null && plans.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = errorMessage!!, color = Color.Red, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadAllPlans() }, colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
                        Text("Reintentar")
                    }
                }
            } else if (plans.isEmpty()) {
                Text(
                    text = "No tienes solicitudes de intercambio aún.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            } else {
                val receivedPlans = plans.filter { it.isReceived }
                val sentPlans = plans.filter { !it.isReceived }

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (receivedPlans.isNotEmpty()) {
                        item {
                            Text(
                                "Solicitudes Recibidas",
                                style = MaterialTheme.typography.titleMedium,
                                color = Cafe,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        items(receivedPlans) { plan ->
                            RequestItem(plan) {
                                navController.navigate(NavRoutes.ExchangePlan.createRoute(planId = plan.id))
                            }
                        }
                    }

                    if (sentPlans.isNotEmpty()) {
                        item {
                            Text(
                                "Solicitudes Enviadas",
                                style = MaterialTheme.typography.titleMedium,
                                color = Cafe,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                        items(sentPlans) { plan ->
                            RequestItem(plan) {
                                navController.navigate(NavRoutes.ExchangePlan.createRoute(planId = plan.id))
                            }
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun RequestItem(plan: ExchangePlan, onClick: () -> Unit) {
    val statusText = when (plan.status) {
        PlanStatus.PENDING -> if (plan.isReceived) "Pendiente de tu aprobación" else "Esperando respuesta de ${plan.partnerName}"
        PlanStatus.ACCEPTED -> "Intercambio en curso"
        PlanStatus.REJECTED -> "Solicitud rechazada"
        PlanStatus.COMPLETED -> "Intercambio completado"
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = plan.partnerImageUrl, contentDescription = null,
                modifier = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFFFFD1DC)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = plan.partnerName, fontWeight = FontWeight.Bold, color = Cafe)
                Text(text = statusText, fontSize = 12.sp, color = if (plan.status == PlanStatus.REJECTED) Rojo else Color.Gray)
            }
            Text(text = "Ver Detalle >", fontSize = 12.sp, color = Rojo, fontWeight = FontWeight.Medium)
        }
    }
}
