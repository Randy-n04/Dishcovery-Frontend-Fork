package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dishcovery.app.presentation.ui.components.LoginContent
import com.dishcovery.app.presentation.ui.layout.MainLayout
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val user by viewModel.user.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(user) {
        if (user != null) {
            onLoginSuccess()
        }
    }

    MainLayout {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val errorText = when (error) {
                "BLOCKED_USER" -> stringResource(R.string.blocked_user_error)
                "INVALID_CREDENTIALS" -> stringResource(R.string.invalid_credentials_error)
                else -> stringResource(R.string.invalid_credentials_error)
            }

            if (error != null) {
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
                )
            }
            LoginContent(
                username = username,
                password = password,
                onUsernameChange = { username = it },
                onPasswordChange = { password = it },

                onLoginClick = {
                    viewModel.login(username, password)
                },
                error = null,
                success = null

            )
        }
    }
}