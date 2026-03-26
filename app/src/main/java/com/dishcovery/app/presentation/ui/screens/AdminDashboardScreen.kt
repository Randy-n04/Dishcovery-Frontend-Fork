package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dishcovery.app.presentation.ui.components.AdminHeader
import com.dishcovery.app.presentation.ui.components.AdminStatsSection
import com.dishcovery.app.presentation.ui.components.AdminTrendsSection
import com.dishcovery.app.presentation.ui.components.AdminUsersSection
import com.dishcovery.app.presentation.ui.theme.Beige
import com.dishcovery.app.presentation.ui.theme.Naranja
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary



@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onLogout: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(Beige).padding(paddingValues)) {
        AdminHeader(onLogout = onLogout)
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Sistema", style = MaterialTheme.typography.labelLarge, color = TextPrimary.copy(.6f))
            AdminUsersSection()
            AdminTrendsSection()
            AdminStatsSection()
        }
    }
}