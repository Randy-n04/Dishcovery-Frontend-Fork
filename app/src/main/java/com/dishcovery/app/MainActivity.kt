package com.dishcovery.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dishcovery.app.data.datasource.AuthDataSourceImpl
import com.dishcovery.app.data.mapper.UserMapper
import com.dishcovery.app.data.repository.AuthRepositoryImpl
import com.dishcovery.app.presentation.factory.AuthViewModelFactory
import com.dishcovery.app.presentation.ui.screens.LoginScreen
import com.dishcovery.app.presentation.ui.theme.FrontendDishcoveryTheme
import com.dishcovery.app.presentation.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            FrontendDishcoveryTheme {

                val repository = AuthRepositoryImpl(
                    AuthDataSourceImpl(),
                    UserMapper()
                )

                val viewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(repository)
                )

                LoginScreen(viewModel)
            }
        }
    }
}