package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.model.Recipe

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
    var expanded by remember { mutableStateOf(false) }
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
            // Encabezado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = null,
                    tint = Color(0xFFD84315),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.feed_ai_title),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF1B1B1B)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.feed_ai_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF666666),
                modifier = Modifier.padding(start = 32.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de categoría
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedCategory?.name ?: stringResource(R.string.feed_ai_category_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selectedCategory != null) Color.Black else Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { expanded = true },
                        modifier = Modifier.padding(end = 0.dp)
                    ) {
                        Text("▼", fontSize = 12.sp)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Campos de entrada lado a lado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = preference,
                    onValueChange = { preference = it },
                    label = { Text(stringResource(R.string.feed_ai_preference_label), fontSize = 12.sp) },
                    placeholder = { Text(stringResource(R.string.feed_ai_preference_hint), fontSize = 11.sp) },
                    textStyle = MaterialTheme.typography.bodySmall,
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = ingredients,
                    onValueChange = { ingredients = it },
                    label = { Text(stringResource(R.string.feed_ai_ingredients_label), fontSize = 12.sp) },
                    placeholder = { Text(stringResource(R.string.feed_ai_ingredients_hint), fontSize = 11.sp) },
                    textStyle = MaterialTheme.typography.bodySmall,
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón de generar sugerencias
            Button(
                onClick = {
                    val categoryId = selectedCategory?.id ?: return@Button
                    val parsedIngredients = ingredients
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                        .takeIf { it.isNotEmpty() }

                    onRequestFeed(categoryId, preference.takeIf { it.isNotBlank() }, parsedIngredients)
                },
                enabled = !isLoading && selectedCategory != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD84315),
                    disabledContainerColor = Color(0xFFCCCCCC)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    stringResource(R.string.feed_ai_button),
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Mensaje de error o vacío
            feedMessage?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFF3E0)
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE65100),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Mostrar tip de la IA (cuando venga) asociado a las recomendaciones
            if (!aiTip.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFFA052),
                                    Color(0xFFF2994A),
                                    Color(0xFFF07B3F),
                                    Color(0xFFD84315)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(R.string.ai_tip_icon_desc),
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

            // Recetas recomendadas
            if (recommendedRecipes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.feed_ai_recommended_title),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                        items(recommendedRecipes) { recipe ->
                            RecipeRecommendationCard(recipe = recipe, onClick = { onRecipeClick(recipe) })
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
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Placeholder de imagen
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFE0E0E0)
            ) {
                // En futuro se puede cargar imagen real
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre de receta
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Rating visual (simplificado)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFB74D),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "${(recipe.weight / 100).coerceIn(1, 5)}.0",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}


