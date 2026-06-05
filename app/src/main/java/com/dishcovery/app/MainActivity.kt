package com.dishcovery.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
                
                // We keep AuthViewModel at the Activity level to manage global session state (user login/logout)
                val authViewModel: AuthViewModel = hiltViewModel()
                val user by authViewModel.user.collectAsState()
                
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
