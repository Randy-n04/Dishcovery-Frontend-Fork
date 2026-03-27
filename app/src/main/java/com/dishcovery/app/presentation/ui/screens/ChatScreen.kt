package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.viewmodel.ChatViewModel
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.ui.components.ChatItem

@Composable
fun ChatScreen(
    paddingValues: PaddingValues,
    chatViewModel: ChatViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val user by authViewModel.user.collectAsState()
    val chats by chatViewModel.chats.collectAsState()

    var search by remember { mutableStateOf("") }


    LaunchedEffect(user) {
        user?.let {
            chatViewModel.loadChats(it.id)
        }
    }


    val filteredChats = chats.filter {
        it.name.contains(search, ignoreCase = true) ||
                it.lastMessage.contains(search, ignoreCase = true)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        Text(
            text = "Chats",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    navController.navigate(NavRoutes.Home.ROUTE) {
                        popUpTo(NavRoutes.Home.ROUTE) { inclusive = true }
                    }
                }
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Buscar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
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
    }
}