package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.dishcovery.app.R
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary
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
    val chatsError by chatViewModel.chatsError.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        Text(
            text = stringResource(R.string.chats_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )

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

        if (chatsError != null) {
            Text(
                text = chatsError.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }


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