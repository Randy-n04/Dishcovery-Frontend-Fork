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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.theme.Cafe
import com.dishcovery.app.presentation.ui.theme.Rojo

data class PlanRequest(
    val id: Long,
    val userName: String,
    val userImageUrl: String,
    val isReceived: Boolean,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanRequestsScreen(navController: NavController) {
    val requests = listOf(
        PlanRequest(1, "Maria Lopez", "https://api.dicebear.com/7.x/avataaars/svg?seed=Maria", true, "Pendiente de tu aprobación"),
        PlanRequest(2, "Javier Rodriguez", "https://api.dicebear.com/7.x/avataaars/svg?seed=Javier", false, "Esperando respuesta de Javier")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buzón de Planes", color = Cafe, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Rojo)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFFBE6))
            )
        },
        containerColor = Color(0xFFFFFBE6)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Solicitudes Recibidas", style = MaterialTheme.typography.titleMedium, color = Cafe, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            }
            items(requests.filter { it.isReceived }) { request ->
                RequestItem(request) {
                    navController.navigate(
                        NavRoutes.ExchangePlan.createRoute(
                            planId = request.id,
                            isReceived = true
                        )
                    )
                }
            }
            item {
                Text("Solicitudes Enviadas", style = MaterialTheme.typography.titleMedium, color = Cafe, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
            }
            items(requests.filter { !it.isReceived }) { request ->
                RequestItem(request) {
                    navController.navigate(
                        NavRoutes.ExchangePlan.createRoute(
                            planId = request.id,
                            isReceived = false
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun RequestItem(request: PlanRequest, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = request.userImageUrl, contentDescription = null,
                modifier = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFFFFD1DC)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = request.userName, fontWeight = FontWeight.Bold, color = Cafe)
                Text(text = request.status, fontSize = 12.sp, color = Color.Gray)
            }
            Text(text = "Ver Plan >", fontSize = 12.sp, color = Rojo, fontWeight = FontWeight.Medium)
        }
    }
}
