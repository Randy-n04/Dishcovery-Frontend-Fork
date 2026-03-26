package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dishcovery.app.presentation.ui.components.AITipBanner
import com.dishcovery.app.presentation.ui.components.PopularRecipesList
import com.dishcovery.app.presentation.ui.components.SavedRecipesList
import com.dishcovery.app.presentation.ui.layout.HomeLayout

@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    HomeLayout(
        modifier = Modifier.padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Recetas Guardadas",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            SavedRecipesList(
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
