package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dishcovery.app.R
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
            .background(Beige)
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_button_desc))
                }

                Text(
                    text = chatName,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp, vertical = 10.dp)
                )

                Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search_placeholder), tint = Primary)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(Icons.Default.Call, contentDescription = null, tint = Primary)
                Spacer(modifier = Modifier.width(10.dp))
            }
        }

        HorizontalDivider(color = Color(0xFFE8E8E8), thickness = 1.dp)


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
                                    text = stringResource(R.string.ai_suggestion_prefix) + stringResource(R.string.ai_suggestion_chat_detail),
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
                SuggestionChip(text = stringResource(R.string.translate_chip), selected = false, modifier = Modifier.weight(1f))
                SuggestionChip(text = stringResource(R.string.recipe_tips_chip), selected = false, modifier = Modifier.weight(1f))
                SuggestionChip(text = stringResource(R.string.plan_schedules_chip), selected = true, modifier = Modifier.weight(1.2f))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .height(52.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE4E4E4),
                        shape = RoundedCornerShape(26.dp)
                    )
                    .background(
                        color = Color.White,
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
                    placeholder = { Text(stringResource(R.string.message_placeholder), color = Color(0xFF7E7E7E)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
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