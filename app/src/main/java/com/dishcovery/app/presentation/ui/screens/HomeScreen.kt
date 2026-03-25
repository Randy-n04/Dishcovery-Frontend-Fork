package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dishcovery.app.presentation.ui.components.HomeHeader
import com.dishcovery.app.presentation.ui.components.AITipBanner
import com.dishcovery.app.presentation.ui.components.PopularRecipesList

@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        HomeHeader()

        AITipBanner()

        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Recetas Populares",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        PopularRecipesList(
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Recetas Favoritas",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
