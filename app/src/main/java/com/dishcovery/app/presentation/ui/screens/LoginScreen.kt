package com.dishcovery.app.presentation.ui.screens

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import com.dishcovery.app.presentation.ui.components.LoginContent
import com.dishcovery.app.presentation.ui.layout.MainLayout
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.dishcovery.app.R

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
        LoginContent(
            username = username,
            password = password,
            onUsernameChange = { username = it },
            onPasswordChange = { password = it },
            onLoginClick = {
                viewModel.login(username, password)
            },
            error = error,
            success = null
        )
    }
}