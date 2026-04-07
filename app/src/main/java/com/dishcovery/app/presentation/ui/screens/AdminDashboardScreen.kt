package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dishcovery.app.presentation.ui.components.AdminHeader
import com.dishcovery.app.presentation.ui.components.AdminTrendsSection
import com.dishcovery.app.presentation.ui.components.AdminUsersSection
import com.dishcovery.app.presentation.ui.theme.Beige
import com.dishcovery.app.presentation.ui.theme.TextPrimary

@Composable
fun AdminDashboardScreen(
    paddingValues: androidx.compose.foundation.layout.PaddingValues,
    onLogout: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Beige)
            .padding(paddingValues)
    ) {
        AdminHeader(onLogout = onLogout)
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Gestion", style = MaterialTheme.typography.labelLarge, color = TextPrimary.copy(.6f))
            AdminUsersSection()
            AdminTrendsSection()
        }
    }
}