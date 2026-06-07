package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.Message
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.viewmodel.ChatViewModel
import com.dishcovery.app.presentation.ui.theme.*
import com.dishcovery.app.presentation.ui.components.MessageBubble
import com.dishcovery.app.presentation.ui.components.MessageContextMenu
import com.dishcovery.app.presentation.ui.components.ScreenErrorState
import com.dishcovery.app.presentation.ui.components.ScreenLoadingState

@Composable
fun ChatDetailScreen(
    chatId: Long,
    chatName: String,
    currentUserId: Long,
    chatViewModel: ChatViewModel,
    navController: NavController
) {

    val messages by chatViewModel.messages.collectAsState()
    val messagesError by chatViewModel.messagesError.collectAsState()
    val sendError by chatViewModel.sendError.collectAsState()

    var draft by remember { mutableStateOf("") }
    var editingMessageId by remember { mutableStateOf<Long?>(null) }
    var wasLoading by remember { mutableStateOf(false) }

    var selectedMessage by remember { mutableStateOf<Message?>(null) }
    var showMenuAbove by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }

    val isLoadingMessage by chatViewModel.isLoadingMessage.collectAsState()
    val isLoadingMessages by chatViewModel.isLoadingMessages.collectAsState()

    LaunchedEffect(chatId) {
        chatViewModel.loadMessages(chatId)
    }

    LaunchedEffect(sendError) {
        if (sendError == null && editingMessageId != null) {
            editingMessageId = null
            draft = ""
        }
    }

    LaunchedEffect(isLoadingMessage) {
        if (wasLoading && !isLoadingMessage && sendError == null) {
            draft = ""
            focusManager.clearFocus()
        }
        wasLoading = isLoadingMessage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Beige)
    ) {

        // Header
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
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_desc)
                    )
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

        Box(modifier = Modifier.weight(1f)) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                itemsIndexed(messages) { index, message ->

                    val isSelected = selectedMessage?.id == message.id
                    val isMe = message.senderId == currentUserId

                    val menuActions = rememberMenuActions(
                        message = message,
                        isMe = isMe,
                        clipboardManager = clipboardManager,
                        onDone = { selectedMessage = null },
                        onEdit = {
                            draft = message.message
                            editingMessageId = message.id
                            selectedMessage = null
                        }
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                    ) {
                        if (isSelected && showMenuAbove) {
                            MessageContextMenu(
                                visible = true,
                                showAbove = true,
                                actions = menuActions,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        MessageBubble(
                            message = message,
                            currentUserId = currentUserId,
                            isFirstChatHighlight = index == 0,
                            screenHeightPx = screenHeightPx,
                            onLongClick = { yFraction ->
                                selectedMessage = if (selectedMessage?.id == message.id) null else message
                                showMenuAbove = yFraction > 0.5f
                            }
                        )

                        if (isSelected && !showMenuAbove) {
                            MessageContextMenu(
                                visible = true,
                                showAbove = false,
                                actions = menuActions,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

            }

            if (isLoadingMessages) {
                ScreenLoadingState()
            } else if (messagesError != null) {
                ScreenErrorState(message = messagesError.orEmpty())
            }

            DropdownMenu(
                expanded = showOptions,
                onDismissRequest = { showOptions = false }
            ) {

                selectedMessage?.let { message ->

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.copy_message)) },
                        onClick = {
                            clipboardManager.setText(AnnotatedString(message.message))
                            showOptions = false
                        }
                    )

                    if (message.senderId == currentUserId) {

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.edit_message)) },
                            onClick = {
                                draft = message.message
                                editingMessageId = message.id
                                showOptions = false
                            }
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {

            if (editingMessageId != null) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFE1DB))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cancel_button),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                editingMessageId = null
                                draft = ""
                            },
                        tint = Primary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(R.string.edit_message),
                        style = MaterialTheme.typography.labelMedium,
                        color = Primary
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SuggestionChip(text = stringResource(R.string.recipe_tips_chip), selected = false, modifier = Modifier.weight(1f))
                
                SuggestionChip(
                    text = stringResource(R.string.plan_schedules_chip), 
                    selected = false,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.navigate(NavRoutes.ExchangePlan.createRoute(chatId = chatId))
                    }
                )
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
                    .background(Color.White, RoundedCornerShape(26.dp))
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically) {

                  Surface(
                      shape    = CircleShape,
                      color    = Color(0xFFFFE1DB),
                      modifier = Modifier
                          .size(30.dp)
                          .clickable { navController.navigate(NavRoutes.CameraAnalysis.ROUTE) }
                  ) {
                      Box(contentAlignment = Alignment.Center) {
                          CameraGlyph(tint = Primary, modifier = Modifier.size(18.dp))
                      }
                  }

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = draft,
                    onValueChange = {
                        draft = it
                        if (sendError != null) chatViewModel.clearSendError()
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.message_placeholder),
                            color = Color(0xFF7E7E7E)
                        )
                    },
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

                TextButton(
                    onClick = {
                        val textToSend = draft.trim()
                        if (textToSend.isNotEmpty()) {
                            if (editingMessageId != null) {
                                chatViewModel.editMessage(editingMessageId!!, textToSend, chatId)
                                editingMessageId = null
                            } else {
                                chatViewModel.sendMessage(chatId, currentUserId, textToSend)
                            }
                        }
                    }
                ) {
                    if (isLoadingMessage) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = Primary
                        )
                    } else {
                        Text(
                            text =
                                if (editingMessageId != null)
                                    stringResource(R.string.update_button_label)
                                else
                                    stringResource(R.string.send_button_label),
                            color = Primary
                        )
                    }
                }
            }

            if (sendError != null) {

                Text(
                    text = sendError.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp)
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.clickable { onClick() },
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

@Composable
private fun rememberMenuActions(
    message: Message,
    isMe: Boolean,
    clipboardManager: ClipboardManager,
    onDone: () -> Unit,
    onEdit: () -> Unit
): List<Pair<String, () -> Unit>> {
    val labelCopy = stringResource(R.string.copy_message)
    val labelEdit = stringResource(R.string.edit_message)

    return remember(message.id, isMe) {
        buildList {
            add(labelCopy to {
                clipboardManager.setText(AnnotatedString(message.message))
                onDone()
            })
            if (isMe) {
                add(labelEdit to onEdit)
            }
        }
    }
}
