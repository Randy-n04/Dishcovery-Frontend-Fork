package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.model.Recipe
import androidx.compose.ui.text.style.TextAlign
import com.dishcovery.app.presentation.ui.theme.Cafe

@Composable
fun FeedAiSection(
    categories: List<KitchenCategory>,
    isLoading: Boolean,
    feedMessage: String?,
    aiTip: String?,
    recommendedRecipes: List<Recipe>,
    onRequestFeed: (Long, String?, List<String>?) -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember(categories) { mutableStateOf(categories.firstOrNull()) }
    var preference by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }

    LaunchedEffect(categories) {
        if (selectedCategory == null && categories.isNotEmpty()) {
            selectedCategory = categories.first()
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF9E6)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = null,
                    tint = Color(0xFFD84315),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.feed_ai_title),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Categorías",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8B4513)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val selected = selectedCategory?.id == category.id

                    Surface(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(20.dp),
                        color = if (selected) Color(0xFFD84315) else Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Text(
                            text = category.name,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            color = if (selected) Color.White else Color(0xFF444444),
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = preference,
                onValueChange = { if (it.length <= 100) preference = it },
                label = { Text("Preferencia") },
                placeholder = { Text("Ej: algo sin pasta") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                supportingText = { Text("${preference.length}/100") },
                isError = preference.length > 100
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = ingredients,
                onValueChange = { if (it.length <= 100) ingredients = it },
                label = { Text("Ingredientes") },
                placeholder = { Text("Ej: arroz, tomate, pollo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                supportingText = { Text("${ingredients.length}/100") },
                isError = ingredients.length > 100
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val categoryId = selectedCategory?.id ?: return@Button
                    val parsedIngredients = ingredients
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                        .takeIf { it.isNotEmpty() }

                    onRequestFeed(
                        categoryId,
                        preference.takeIf { it.isNotBlank() },
                        parsedIngredients
                    )
                },
                enabled = !isLoading && selectedCategory != null && preference.length <= 100 && ingredients.length <= 100,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD84315)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(stringResource(R.string.feed_ai_button), fontWeight = FontWeight.SemiBold)
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFD84315),
                            modifier = Modifier.size(40.dp),
                            strokeWidth = 3.dp
                        )
                    }
                }
            }

            feedMessage?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = it, color = Color(0xFFE65100))
            }

            if (!aiTip.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                                .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFFFFA052),
                                                    Color(0xFFF2994A),
                                                    Color(0xFFF07B3F),
                                                    Color(0xFFF07B3F)
                                                )
                                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = stringResource(R.string.ai_suggestion_prefix).trimEnd(),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = aiTip,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.95f)
                        )
                    }
                }
            }

            if (recommendedRecipes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.feed_ai_recommended_title),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(recommendedRecipes) { recipe ->
                        RecipeRecommendationCard(
                            recipe = recipe,
                            onClick = { onRecipeClick(recipe) }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun RecipeRecommendationCard(recipe: Recipe, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .size(width = 160.dp, height = 180.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = recipe.iconUrl,
                contentDescription = recipe.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = recipe.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Cafe,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
