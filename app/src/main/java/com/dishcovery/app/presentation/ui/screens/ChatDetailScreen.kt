package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dishcovery.app.domain.model.Message
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

    LaunchedEffect(chatId) {
        chatViewModel.loadMessages(chatId)
    }

    Column(modifier = Modifier.fillMaxSize()) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }

            Surface(
                shape = MaterialTheme.shapes.large,
                color = BackgroundLight
            ) {
                Text(
                    text = chatName,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(Icons.Default.Search, contentDescription = null, tint = Primary)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.Call, contentDescription = null, tint = Primary)
        }


        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = false
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Mensaje") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}