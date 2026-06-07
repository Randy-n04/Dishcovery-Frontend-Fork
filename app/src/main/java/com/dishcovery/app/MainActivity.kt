package com.dishcovery.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dishcovery.app.domain.model.UserRole
import com.dishcovery.app.presentation.navigation.BottomNavItem
import com.dishcovery.app.presentation.navigation.NavGraph
import com.dishcovery.app.presentation.navigation.NavRoutes
import com.dishcovery.app.presentation.ui.components.BottomNavigationBar
import com.dishcovery.app.presentation.ui.theme.FrontendDishcoveryTheme
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.viewmodel.GlobalErrorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FrontendDishcoveryTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                // ViewModels
                val authViewModel: AuthViewModel = hiltViewModel()
                val globalErrorViewModel: GlobalErrorViewModel = hiltViewModel()
                
                val user by authViewModel.user.collectAsState()
                val globalError by globalErrorViewModel.error.collectAsState()
                
                val snackbarHostState = remember { SnackbarHostState() }

                // Handle global errors
                LaunchedEffect(globalError) {
                    globalError?.let {
                        snackbarHostState.showSnackbar(
                            message = it.message,
                            actionLabel = "Cerrar"
                        )
                        globalErrorViewModel.clearError()
                    }
                }
                
                val isAdmin = user?.role is UserRole.Admin
                val isChatDetailRoute = currentRoute == NavRoutes.ChatDetail.ROUTE ||
                    currentRoute?.startsWith("chat_detail/") == true
                val isCameraRoute = currentRoute == NavRoutes.CameraAnalysis.ROUTE

                val showBottomBar = user != null && !(isChatDetailRoute && !isAdmin) && !isCameraRoute
                val bottomNavItems = if (isAdmin) {
                    BottomNavItem.adminItems()
                } else {
                    BottomNavItem.userItems()
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavigationBar(
                                navController = navController,
                                items = bottomNavItems
                            )
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
