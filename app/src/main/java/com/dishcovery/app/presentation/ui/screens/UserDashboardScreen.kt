package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun UserDashboardScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hola Usuario 👨‍🍳")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onLogout) {
                Text("Cerrar Sesión")
            }
        }
    }
}