package com.dishcovery.app.presentation.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * BottomNavItem represents items in the bottom navigation bar
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Profile : BottomNavItem(
        route = NavRoutes.Profile.ROUTE,
        title = "Perfil",
        icon = Icons.Default.Person
    )

    data object Home : BottomNavItem(
        route = NavRoutes.Home.ROUTE,
        title = "Inicio",
        icon = Icons.Default.Home
    )

    data object Chat : BottomNavItem(
        route = NavRoutes.Recipes.ROUTE,
        title = "Chat",
        icon = Icons.Default.Email
    )



    companion object {
        fun items() = listOf(Profile, Home, Chat)
    }
}