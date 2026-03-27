package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dishcovery.app.domain.model.Recipe
import com.dishcovery.app.presentation.ui.components.AITipBanner
import com.dishcovery.app.presentation.ui.components.BackPressToExit
import com.dishcovery.app.presentation.ui.components.PopularRecipesList
import com.dishcovery.app.presentation.ui.components.RecipeDetailDialog
import com.dishcovery.app.presentation.ui.components.SavedRecipesList
import com.dishcovery.app.presentation.ui.layout.HomeLayout
import com.dishcovery.app.presentation.viewmodel.RecipeViewModel

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    recipeViewModel: RecipeViewModel
) {

    BackPressToExit()
    val popularRecipes by recipeViewModel.popularRecipes.collectAsState()
    val savedRecipes by recipeViewModel.savedRecipes.collectAsState()
    val isLoading by recipeViewModel.isLoading.collectAsState()
    
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }

    HomeLayout(
        modifier = Modifier.padding(paddingValues)
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
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
                    recipes = popularRecipes,
                    onRecipeClick = { selectedRecipe = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (savedRecipes.isNotEmpty()) {
                    Text(
                        text = "Recetas Guardadas",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    SavedRecipesList(
                        recipes = savedRecipes,
                        onRecipeClick = { selectedRecipe = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    selectedRecipe?.let { recipe ->
        val isSaved = savedRecipes.any { it.id == recipe.id }
        RecipeDetailDialog(
            recipe = recipe,
            isSaved = isSaved,
            onSaveToggle = { recipeViewModel.toggleSaveRecipe(recipe) },
            onDismiss = { selectedRecipe = null }
        )
    }
}
