package com.dishcovery.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.dishcovery.app.data.datasource.AuthDataSourceImpl
import com.dishcovery.app.data.datasource.ChatDataSourceImpl
import com.dishcovery.app.data.mapper.ChatMapper
import com.dishcovery.app.data.mapper.RecipeMapper
import com.dishcovery.app.data.mapper.UserMapper
import com.dishcovery.app.data.repository.AuthRepositoryImpl
import com.dishcovery.app.data.repository.ChatRepositoryImpl
import com.dishcovery.app.data.repository.RecipeRepositoryImpl
import com.dishcovery.app.domain.model.UserRole
import com.dishcovery.app.presentation.factory.AuthViewModelFactory
import com.dishcovery.app.presentation.factory.ChatViewModelFactory
import com.dishcovery.app.presentation.factory.RecipeViewModelFactory
import com.dishcovery.app.presentation.navigation.NavGraph
import com.dishcovery.app.presentation.ui.components.BottomNavigationBar
import com.dishcovery.app.presentation.ui.theme.FrontendDishcoveryTheme
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.viewmodel.ChatViewModel
import com.dishcovery.app.presentation.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FrontendDishcoveryTheme {
                // Auth Dependencies
                val authRepository = AuthRepositoryImpl(
                    AuthDataSourceImpl(),
                    UserMapper()
                )
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(authRepository)
                )

                // Recipe Dependencies
                val recipeRepository = RecipeRepositoryImpl(RecipeMapper())
                val recipeViewModel: RecipeViewModel = viewModel(
                    factory = RecipeViewModelFactory(recipeRepository)
                )

                // Chat Dependencies
                val chatMapper = ChatMapper()
                val chatRepository = ChatRepositoryImpl(
                    ChatDataSourceImpl(chatMapper),
                    chatMapper
                )
                val chatViewModel: ChatViewModel = viewModel(
                    factory = ChatViewModelFactory(chatRepository)
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
                        recipeViewModel = recipeViewModel,
                        chatViewModel = chatViewModel,
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}
