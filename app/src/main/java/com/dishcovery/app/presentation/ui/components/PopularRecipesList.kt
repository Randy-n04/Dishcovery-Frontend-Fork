package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PopularRecipesList(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(5) { index ->
            PopularRecipeCard(
                title = if (index == 0) "Lasaña" else "Receta Popular $index",
                description = "Cocina italiana. La lasaña es un tipo de pasta. Se suele servir en láminas superpuestas.",
                likes = 234 + (index * 10)
            )
        }
    }
}
