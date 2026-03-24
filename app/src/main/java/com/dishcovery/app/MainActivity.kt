package com.dishcovery.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.dishcovery.app.data.datasource.AuthDataSourceImpl
import com.dishcovery.app.data.mapper.UserMapper
import com.dishcovery.app.data.repository.AuthRepositoryImpl
import com.dishcovery.app.domain.model.UserRole
import com.dishcovery.app.presentation.factory.AuthViewModelFactory
import com.dishcovery.app.presentation.navigation.NavGraph
import com.dishcovery.app.presentation.ui.components.BottomNavigationBar
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

                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(repository)
                )

                val navController = rememberNavController()
                val user by authViewModel.user.collectAsState()
                val showBottomBar = user?.role is UserRole.User

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { paddingValues ->
                    NavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}