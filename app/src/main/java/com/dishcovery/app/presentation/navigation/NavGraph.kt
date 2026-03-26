// presentation/navigation/NavGraph.kt
package com.dishcovery.app.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dishcovery.app.domain.model.UserRole
import com.dishcovery.app.presentation.ui.screens.AdminDashboardScreen
import com.dishcovery.app.presentation.ui.screens.HomeScreen
import com.dishcovery.app.presentation.ui.screens.LoginScreen
import com.dishcovery.app.presentation.ui.screens.ProfileScreen
import com.dishcovery.app.presentation.ui.screens.RecipesScreen
import com.dishcovery.app.presentation.viewmodel.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    paddingValues: PaddingValues
) {
    val user by authViewModel.user.collectAsState()

    val startDestination = when {
        user == null                 -> NavRoutes.Auth.ROUTE
        user?.role is UserRole.Admin -> NavRoutes.AdminDashboard.ROUTE
        else                         -> NavRoutes.Home.ROUTE
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ── Auth ────────────────────────────────────────────────────────────
        composable(NavRoutes.Auth.ROUTE) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    val destination = if (user?.role is UserRole.Admin) {
                        NavRoutes.AdminDashboard.ROUTE
                    } else {
                        NavRoutes.Home.ROUTE
                    }
                    navController.navigate(destination) {
                        popUpTo(NavRoutes.Auth.ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // ── User / Home ────────────────────────────────────────────────────
        composable(NavRoutes.Home.ROUTE) {
            HomeScreen(paddingValues)
        }

        // ── Admin ───────────────────────────────────────────────────────────
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
