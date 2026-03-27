package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dishcovery.app.presentation.viewmodel.ChatViewModel
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.ui.components.ChatItem

@Composable
fun ChatScreen(
    paddingValues: PaddingValues,
    chatViewModel: ChatViewModel,
    authViewModel: AuthViewModel
) {
    val user by authViewModel.user.collectAsState()
    val chats by chatViewModel.chats.collectAsState()

    LaunchedEffect(user) {
        user?.let {
            chatViewModel.loadChats(it.id) // 🔥 AQUÍ
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Text(
            text = "Chats",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(chats) {
                ChatItem(it)
            }
        }
    }
}