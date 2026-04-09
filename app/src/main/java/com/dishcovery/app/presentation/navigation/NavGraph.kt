package com.dishcovery.app.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dishcovery.app.domain.model.UserRole
import com.dishcovery.app.presentation.ui.screens.AdminDashboardScreen
import com.dishcovery.app.presentation.ui.screens.AdminStatisticsScreen
import com.dishcovery.app.presentation.ui.screens.HomeScreen
import com.dishcovery.app.presentation.ui.screens.LoginScreen
import com.dishcovery.app.presentation.ui.screens.ProfileScreen
import com.dishcovery.app.presentation.ui.screens.ChatScreen
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.viewmodel.RecipeViewModel
import com.dishcovery.app.presentation.viewmodel.ChatViewModel
import com.dishcovery.app.presentation.viewmodel.ProfileViewModel
import com.dishcovery.app.presentation.ui.screens.ChatDetailScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    paddingValues: PaddingValues
) {
    val user by authViewModel.user.collectAsState()

    val startDestination = when {
        user == null                 -> NavRoutes.Auth.ROUTE
        user?.role is UserRole.Admin -> NavRoutes.AdminManagement.ROUTE
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
                        NavRoutes.AdminManagement.ROUTE
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
            val recipeViewModel: RecipeViewModel = hiltViewModel()
            HomeScreen(paddingValues, recipeViewModel)
        }

        // ── Admin ────────────────────────────────────────────
        composable(NavRoutes.AdminManagement.ROUTE) {
            AdminDashboardScreen(
                paddingValues = paddingValues,
                username = user?.username.orEmpty(),
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Auth.ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.AdminStats.ROUTE) {
            AdminStatisticsScreen(
                paddingValues = paddingValues,
                username = user?.username.orEmpty(),
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Auth.ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.AdminProfile.ROUTE) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                paddingValues = paddingValues,
                user = user,
                onBack = { navController.popBackStack() },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Auth.ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = profileViewModel
            )
        }

        // Legacy route kept for compatibility.
        composable(NavRoutes.AdminDashboard.ROUTE) {
            AdminDashboardScreen(
                paddingValues = paddingValues,
                username = user?.username.orEmpty(),
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Auth.ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }



        composable(NavRoutes.Chats.ROUTE) {
            val chatViewModel: ChatViewModel = hiltViewModel()
            ChatScreen(
                paddingValues = paddingValues,
                chatViewModel = chatViewModel,
                authViewModel = authViewModel,
                navController = navController
            )
        }

        // ── Profile ──────────────────────────────────────────
        composable(NavRoutes.Profile.ROUTE) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                paddingValues = paddingValues,
                user          = user,
                onBack        = { navController.popBackStack() },
                onLogout      = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Auth.ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = profileViewModel
            )
        }


        composable(NavRoutes.ChatDetail.ROUTE) { backStackEntry ->
            val chatViewModel: ChatViewModel = hiltViewModel()
            val chatId = backStackEntry.arguments?.getString("chatId")?.toLong() ?: 0
            val chatName = backStackEntry.arguments?.getString("chatName") ?: ""

            ChatDetailScreen(
                chatId = chatId,
                chatName = chatName,
                currentUserId = user?.id ?: 2L,
                chatViewModel = chatViewModel,
                navController = navController
            )
        }
    }

}
