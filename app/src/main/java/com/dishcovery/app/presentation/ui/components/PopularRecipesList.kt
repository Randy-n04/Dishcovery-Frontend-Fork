package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dishcovery.app.domain.model.Recipe

@Composable
fun PopularRecipesList(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(recipes) { recipe ->
            PopularRecipeCard(
                title = recipe.name,
                description = recipe.description,
                likes = recipe.weight,
                imageUrl = recipe.iconUrl,
                onClick = { onRecipeClick(recipe) }
            )
        }
    }
}
