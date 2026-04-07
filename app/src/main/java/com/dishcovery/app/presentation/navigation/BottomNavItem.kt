package com.dishcovery.app.presentation.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
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
        route = NavRoutes.Chats.ROUTE,
        title = "Chat",
        icon = Icons.Default.Email
    )

    data object AdminProfile : BottomNavItem(
        route = NavRoutes.AdminProfile.ROUTE,
        title = "Perfil",
        icon = Icons.Default.Person
    )

    data object AdminManagement : BottomNavItem(
        route = NavRoutes.AdminManagement.ROUTE,
        title = "Gestion",
        icon = Icons.Default.Settings
    )

    data object AdminStats : BottomNavItem(
        route = NavRoutes.AdminStats.ROUTE,
        title = "Estadisticas",
        icon = Icons.AutoMirrored.Filled.List
    )



    companion object {
        fun items() = userItems()

        fun userItems() = listOf(Profile, Home, Chat)

        fun adminItems() = listOf(AdminProfile, AdminManagement, AdminStats)
    }
}