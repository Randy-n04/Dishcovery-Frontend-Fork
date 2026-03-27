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
import com.dishcovery.app.presentation.ui.screens.ChatScreen
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.viewmodel.RecipeViewModel
import com.dishcovery.app.presentation.viewmodel.ChatViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    recipeViewModel: RecipeViewModel,
    chatViewModel: ChatViewModel,
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

        // ── Auth ─────────────────────────────────────────────
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

        // ── Home ─────────────────────────────────────────────
        composable(NavRoutes.Home.ROUTE) {
            HomeScreen(paddingValues, recipeViewModel)
        }

        // ── Admin ────────────────────────────────────────────
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

//        // ── Recipes ──────────────────────────────────────────
//        composable(NavRoutes.Recipes.ROUTE) {
//            RecipesScreen(paddingValues)
//        }


        composable(NavRoutes.Chats.ROUTE) {
            ChatScreen(
                paddingValues = paddingValues,
                chatViewModel = chatViewModel,
                authViewModel = authViewModel,
                navController = navController
            )
        }

        // ── Profile ──────────────────────────────────────────
        composable(NavRoutes.Profile.ROUTE) {
            ProfileScreen(
                user = user,
                paddingValues = paddingValues
            )
        }
    }
}