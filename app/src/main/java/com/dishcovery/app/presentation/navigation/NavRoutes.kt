package com.dishcovery.app.presentation.navigation

/**
 * NavRoutes defines all possible destinations
 * Contains all navigation route constants for the application.
 *
 * Using a sealed class with objects ensures type safety and prevents errors from misspelled route strings.
 */
sealed class NavRoutes {
    /**
     * Defines the route for the authentication screen.
     * This is the initial screen where users log in.
     */
    data object Auth : NavRoutes() {
        const val ROUTE = "auth"
    }

    /**
     * Defines the route for the user dashboard.
     * This screen is shown to regular users after login.
     */
    data object UserDashboard : NavRoutes() {
        const val ROUTE = "user_dashboard"
    }

    /**
     * Defines the route for the admin dashboard.
     * This screen is shown to admin users after login.
     */
    data object AdminDashboard : NavRoutes() {
        const val ROUTE = "admin_dashboard"
    }

    /**
     * Defines the route for the home screen.
     * This is one of the bottom navigation destinations.
     */
    data object Home : NavRoutes() {
        const val ROUTE = "home"
    }

    /**
     * Defines the route for the recipes screen.
     * This is one of the bottom navigation destinations.
     */
    data object Chats : NavRoutes() {
        const val ROUTE = "chats"
    }

    /**
     * Defines the route for the profile screen.
     * This is one of the bottom navigation destinations.
     */
    data object Profile : NavRoutes() {
        const val ROUTE = "profile"
    }
}