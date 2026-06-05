package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.dishcovery.app.R
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.theme.Cafe
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary
import com.dishcovery.app.presentation.viewmodel.ChatViewModel
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.ui.components.ChatItem
import com.dishcovery.app.presentation.ui.components.ScreenErrorState
import com.dishcovery.app.presentation.ui.components.ScreenLoadingState

@Composable
fun ChatScreen(
    paddingValues: PaddingValues,
    chatViewModel: ChatViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val user by authViewModel.user.collectAsState()
    val chats by chatViewModel.chats.collectAsState()
    val chatsError by chatViewModel.chatsError.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val isLoadingChats by chatViewModel.isLoadingChats.collectAsState()

    var search by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let {
            chatViewModel.loadChats(it.id)
        }
    }

    DisposableEffect(lifecycleOwner, user) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                user?.let { chatViewModel.loadChats(it.id) }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val filteredChats = chats.filter {
        it.name.contains(search, ignoreCase = true) ||
                it.lastMessage.contains(search, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Text(
                text = stringResource(R.string.chats_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Cafe,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
            )

            // Sección de Solicitudes de Planes
            PlanRequestsEntry(onClick = {
                navController.navigate(NavRoutes.PlanRequests.ROUTE)
            })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.size(36.dp)
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(NavRoutes.Home.ROUTE) {
                                popUpTo(NavRoutes.Home.ROUTE) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_desc),
                            tint = Rojo
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                TextField(
                    value = search,
                    onValueChange = { search = it },
                    placeholder = { Text(stringResource(R.string.search_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_placeholder),
                            tint = Rojo
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedPlaceholderColor = TextPrimary.copy(alpha = 0.6f),
                        unfocusedPlaceholderColor = TextPrimary.copy(alpha = 0.6f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(filteredChats) { chat ->
                    ChatItem(
                        chat = chat,
                        onClick = {
                            navController.navigate(
                                NavRoutes.ChatDetail.createRoute(chat.id, chat.name)
                            )
                        }
                    )
                }
            }
            if (isLoadingChats) {
                ScreenLoadingState()
            } else if (chatsError != null) {
                ScreenErrorState(message = chatsError.orEmpty())
            }
        }

    }
}

@Composable
fun PlanRequestsEntry(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBE6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF8B4513).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color(0xFF8B4513)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Buzón de Planes",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513)
                )
                Text(
                    text = "Tienes solicitudes pendientes",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Badge(containerColor = Rojo) {
                Text("2", color = Color.White)
            }
        }
    }
}
