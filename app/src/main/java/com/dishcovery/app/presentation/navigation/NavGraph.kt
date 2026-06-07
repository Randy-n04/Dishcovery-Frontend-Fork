package com.dishcovery.app.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dishcovery.app.domain.model.UserRole
import com.dishcovery.app.presentation.ui.screens.*
import com.dishcovery.app.presentation.viewmodel.AuthViewModel
import com.dishcovery.app.presentation.viewmodel.RecipeViewModel
import com.dishcovery.app.presentation.viewmodel.ChatViewModel
import com.dishcovery.app.presentation.viewmodel.ProfileViewModel
import com.dishcovery.app.presentation.viewmodel.MatchViewModel

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
        composable(NavRoutes.Auth.ROUTE) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    val destination = if (user?.role is UserRole.Admin) NavRoutes.AdminManagement.ROUTE else NavRoutes.Home.ROUTE
                    navController.navigate(destination) {
                        popUpTo(NavRoutes.Auth.ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Home.ROUTE) {
            val recipeViewModel: RecipeViewModel = hiltViewModel()
            HomeScreen(
                paddingValues,
                recipeViewModel,
                navController,
                userId = user?.id
            )
        }

        composable(
            route = NavRoutes.DiscoverPeople.ROUTE,
            arguments = listOf(navArgument("categoryIds") { type = NavType.StringType })
        ) { backStackEntry ->
            val matchViewModel: MatchViewModel = hiltViewModel()
            val categoryIdsArg = backStackEntry.arguments?.getString("categoryIds") ?: ""
            val listIds = categoryIdsArg.split(",").mapNotNull { it.toLongOrNull() }

            DiscoverPeopleScreen(
                paddingValues = paddingValues,
                categoryIds = listIds,
                navController = navController,
                matchViewModel = matchViewModel,
                authViewModel = authViewModel
            )
        }

        composable(
            route = NavRoutes.ExchangePlan.ROUTE,
            arguments = listOf(
                navArgument("planId") { type = NavType.LongType },
                navArgument("isReceived") {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getLong("planId")
            val isReceived = backStackEntry.arguments?.getBoolean("isReceived") ?: true

            ExchangePlanScreen(
                navController = navController,
                paddingValues = paddingValues,
                isReceived = isReceived,
                planId = planId
            )
        }

        composable(NavRoutes.PlanRequests.ROUTE) {
            PlanRequestsScreen(navController = navController)
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

        composable(NavRoutes.Profile.ROUTE) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                paddingValues = paddingValues,
                user = user,
                onBack = { navController.popBackStack() },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Auth.ROUTE) { popUpTo(0) { inclusive = true } }
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

        // Rutas Admin
        composable(NavRoutes.AdminManagement.ROUTE) {
            AdminDashboardScreen(
                paddingValues = paddingValues,
                username = user?.username.orEmpty(),
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Auth.ROUTE) { popUpTo(0) { inclusive = true } }
                }
            )
        }
        
        composable(NavRoutes.AdminStats.ROUTE) {
            AdminStatisticsScreen(
                paddingValues = paddingValues,
                username = user?.username.orEmpty(),
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Auth.ROUTE) { popUpTo(0) { inclusive = true } }
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
                    navController.navigate(NavRoutes.Auth.ROUTE) { popUpTo(0) { inclusive = true } }
                },
                viewModel = profileViewModel
            )
        }
    }
}
