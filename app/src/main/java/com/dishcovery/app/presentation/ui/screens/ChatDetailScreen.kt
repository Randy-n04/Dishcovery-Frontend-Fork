package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dishcovery.app.presentation.viewmodel.ChatViewModel
import com.dishcovery.app.presentation.ui.theme.*
import com.dishcovery.app.presentation.ui.components.MessageBubble

@Composable
fun ChatDetailScreen(
    chatId: Long,
    chatName: String,
    chatViewModel: ChatViewModel,
    navController: NavController
) {

    val messages by chatViewModel.messages.collectAsState()
    var draft by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        chatViewModel.loadMessages(chatId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE9E9F0))
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundLight)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }

                Text(
                    text = chatName,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                )

                Icon(Icons.Default.Search, contentDescription = null, tint = Primary)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(Icons.Default.Call, contentDescription = null, tint = Primary)
                Spacer(modifier = Modifier.width(10.dp))
            }
        }


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = false
        ) {
            itemsIndexed(messages) { index, message ->
                MessageBubble(
                    message = message,
                    isFirstChatHighlight = index == 0
                )
            }

            if (messages.isNotEmpty()) {
                item {
                    Text(
                        text = "Today 8:43 AM",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary.copy(alpha = 0.7f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color.Transparent,
                            modifier = Modifier.widthIn(max = 260.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(Color(0xFFF1B96C), Color(0xFFE9664D))
                                        ),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Text(
                                    text = "Sugerencia de IA: Ambos tienen disponibilidad este fin de semana. Recomiendo empezar con una receta sencilla para generar confianza.",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionChip(text = "Traducir", selected = false, modifier = Modifier.weight(1f))
                SuggestionChip(text = "Tips recetas", selected = false, modifier = Modifier.weight(1f))
                SuggestionChip(text = "Planear horarios", selected = true, modifier = Modifier.weight(1.2f))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .height(52.dp)
                    .background(
                        color = Color(0xFFF6F3D8),
                        shape = RoundedCornerShape(26.dp)
                    )
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFFE1DB),
                    modifier = Modifier.size(30.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CameraGlyph(
                            tint = Primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = draft,
                    onValueChange = { draft = it },
                    placeholder = { Text("Mensaje") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
private fun CameraGlyph(
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        // Cuerpo de camara
        val bodyTop = size.height * 0.28f
        drawRoundRect(
            color = tint,
            topLeft = Offset(0f, bodyTop),
            size = Size(size.width, size.height - bodyTop),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(width = 2f)
        )

        // Lente
        drawCircle(
            color = tint,
            radius = size.minDimension * 0.17f,
            center = Offset(size.width * 0.5f, size.height * 0.63f),
            style = Stroke(width = 2f)
        )

        // Visor superior
        drawRoundRect(
            color = tint,
            topLeft = Offset(size.width * 0.15f, size.height * 0.12f),
            size = Size(size.width * 0.28f, size.height * 0.2f),
            cornerRadius = CornerRadius(3f, 3f),
            style = Stroke(width = 2f)
        )
    }
}

@Composable
private fun SuggestionChip(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 2.dp,
        color = if (selected) Color(0xFFEFA053) else Color(0xFFF0ECD5)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (selected) Color.White else Color(0xFFAD6E44),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}