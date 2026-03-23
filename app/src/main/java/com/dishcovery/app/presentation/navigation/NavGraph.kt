// presentation/navigation/NavGraph.kt
package com.dishcovery.app.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dishcovery.app.presentation.ui.screens.AdminDashboardScreen
import com.dishcovery.app.presentation.ui.screens.HomeScreen
import com.dishcovery.app.presentation.ui.screens.LoginScreen
import com.dishcovery.app.presentation.ui.screens.ProfileScreen
import com.dishcovery.app.presentation.ui.screens.RecipesScreen
import com.dishcovery.app.presentation.ui.screens.UserDashboardScreen
import com.dishcovery.app.presentation.viewmodel.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    paddingValues: PaddingValues
) {
    val user by authViewModel.user.collectAsState()
    val isAuthenticated = user != null
    val isAdmin = user?.username == "admin"

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) {
            if (isAdmin) NavRoutes.AdminDashboard.ROUTE else NavRoutes.UserDashboard.ROUTE
        } else {
            NavRoutes.Auth.ROUTE
        }
    ) {
        // Auth screen
        composable(NavRoutes.Auth.ROUTE) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(
                        if (user?.username == "admin") {
                            NavRoutes.AdminDashboard.ROUTE
                        } else {
                            NavRoutes.UserDashboard.ROUTE
                        }
                    ) {
                        popUpTo(NavRoutes.Auth.ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // User Dashboard screen
        composable(NavRoutes.UserDashboard.ROUTE) {
            UserDashboardScreen(
                navController = navController,
                paddingValues = paddingValues,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Auth.ROUTE) {
                        popUpTo(NavRoutes.UserDashboard.ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // Admin Dashboard screen
        composable(NavRoutes.AdminDashboard.ROUTE) {
            AdminDashboardScreen(
                navController = navController,
                paddingValues = paddingValues,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Auth.ROUTE) {
                        popUpTo(NavRoutes.AdminDashboard.ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // Home screen (bottom navigation destination)
        composable(NavRoutes.Home.ROUTE) {
            HomeScreen(paddingValues)
        }

        // Recipes screen (bottom navigation destination)
        composable(NavRoutes.Recipes.ROUTE) {
            RecipesScreen(paddingValues)
        }

        // Profile screen (bottom navigation destination)
        composable(NavRoutes.Profile.ROUTE) {
            ProfileScreen(
                user = user,
                paddingValues = paddingValues
            )
        }
    }
}