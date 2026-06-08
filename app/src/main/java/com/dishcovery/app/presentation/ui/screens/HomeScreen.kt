package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dishcovery.app.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.dishcovery.app.domain.model.Recipe
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.components.FeedAiSection
import com.dishcovery.app.presentation.ui.components.PopularRecipesList
import com.dishcovery.app.presentation.ui.components.RecipeDetailDialog
import com.dishcovery.app.presentation.ui.components.SavedRecipesList
import com.dishcovery.app.presentation.ui.components.SearchingMatchDialog
import com.dishcovery.app.presentation.ui.layout.HomeLayout
import com.dishcovery.app.presentation.ui.theme.Primary
import com.dishcovery.app.presentation.viewmodel.RecipeViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    recipeViewModel: RecipeViewModel,
    navController: NavHostController,
    userId: Long?

) {
    val popularRecipes by recipeViewModel.popularRecipes.collectAsState()
    val savedRecipes by recipeViewModel.savedRecipes.collectAsState()
    val kitchenCategories by recipeViewModel.kitchenCategories.collectAsState()
    val aiTip by recipeViewModel.aiTip.collectAsState()
    val recommendedRecipes by recipeViewModel.recommendedRecipes.collectAsState()
    val feedMessage by recipeViewModel.feedMessage.collectAsState()
    val homeError by recipeViewModel.homeError.collectAsState()
    val isLoading by recipeViewModel.isLoading.collectAsState()
    val isFeedLoading by recipeViewModel.isFeedLoading.collectAsState()

    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    var selectedRecipeForDialog by remember { mutableStateOf<Recipe?>(null) }
    var showSearchingDialog by remember { mutableStateOf(false) }
    var searchingCategoryName by remember { mutableStateOf("") }
    var searchingCategoryIds by remember { mutableStateOf<List<Long>>(emptyList()) }


    LaunchedEffect(userId) {
        recipeViewModel.loadHomeData(userId)
    }

    LaunchedEffect(showSearchingDialog) {
        if (showSearchingDialog) {
            delay(1200)
            showSearchingDialog = false

            val idsParam = searchingCategoryIds.joinToString(",")
            navController.navigate(NavRoutes.DiscoverPeople.createRoute(idsParam))
        }
    }

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
                homeError?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = it.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FeedAiSection(
                    categories = kitchenCategories,
                    isLoading = isFeedLoading,
                    feedMessage = feedMessage,
                    aiTip = aiTip,
                    recommendedRecipes = recommendedRecipes,
                    onRequestFeed = { categoryId, preference, ingredients ->
                        recipeViewModel.requestAiFeed(categoryId, preference, ingredients)
                    },
                    onRecipeClick = { recipe ->
                        selectedRecipeForDialog = recipe
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.popular_recipes_title),
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
                        text = stringResource(R.string.saved_recipes_title),
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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFFFE1DB),
            shadowElevation = 6.dp,
            modifier = Modifier
                .padding(
                    end = 20.dp,
                    bottom = paddingValues.calculateBottomPadding() + 16.dp
                )
                .size(56.dp)
                .clickable { navController.navigate(NavRoutes.CameraAnalysis.ROUTE) }
        ) {
            Box(contentAlignment = Alignment.Center) {
                CameraGlyph(tint = Primary, modifier = Modifier.size(28.dp))
            }
        }
    }

    if (showSearchingDialog) {
        SearchingMatchDialog(category = searchingCategoryName)
    }

    selectedRecipe?.let { recipe ->
        val updatedRecipe =
            popularRecipes.find { it.id == recipe.id }
                ?: savedRecipes.find { it.id == recipe.id }
                ?: recommendedRecipes.find { it.id == recipe.id }
                ?: recipe

        val isSaved = savedRecipes.any { it.id == updatedRecipe.id }

        RecipeDetailDialog(
            recipe = updatedRecipe,
            isSaved = isSaved,
            onSaveToggle = {
                recipeViewModel.toggleSaveRecipe(updatedRecipe)
            },
            onSearchMatch = {
                searchingCategoryName = updatedRecipe.categories.joinToString(", ") { it.name }
                searchingCategoryIds = updatedRecipe.categories.map { it.id }
                showSearchingDialog = true
            },
            onDismiss = { selectedRecipe = null }
        )
    }

    selectedRecipeForDialog?.let { recipe ->
        val updatedRecipe =
            popularRecipes.find { it.id == recipe.id }
                ?: savedRecipes.find { it.id == recipe.id }
                ?: recommendedRecipes.find { it.id == recipe.id }
                ?: recipe

        val isSaved = savedRecipes.any { it.id == updatedRecipe.id }

        RecipeDetailDialog(
            recipe = updatedRecipe,
            isSaved = isSaved,
            onSaveToggle = {
                recipeViewModel.toggleSaveRecipe(updatedRecipe)
            },
            onSearchMatch = {
                searchingCategoryName = updatedRecipe.categories.joinToString(", ") { it.name }
                searchingCategoryIds = updatedRecipe.categories.map { it.id }
                showSearchingDialog = true
            },
            onDismiss = { selectedRecipeForDialog = null },
            showSearchMatchButton = true
        )
    }
}
