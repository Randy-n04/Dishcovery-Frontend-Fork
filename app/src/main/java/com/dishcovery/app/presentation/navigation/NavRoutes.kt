package com.dishcovery.app.presentation.navigation

sealed class NavRoutes {
    data object Auth : NavRoutes() { const val ROUTE = "auth" }
    data object Home : NavRoutes() { const val ROUTE = "home" }
    data object Chats : NavRoutes() { const val ROUTE = "chats" }
    data object Profile : NavRoutes() { const val ROUTE = "profile" }
    data object AdminProfile : NavRoutes() { const val ROUTE = "admin_profile" }
    data object AdminManagement : NavRoutes() { const val ROUTE = "admin_management" }
    data object AdminStats : NavRoutes() { const val ROUTE = "admin_stats" }
    data object ChatDetail : NavRoutes() {
        const val ROUTE = "chat_detail/{chatId}/{chatName}"
        fun createRoute(id: Long, name: String) = "chat_detail/$id/$name"
    }
    data object DiscoverPeople : NavRoutes() {
        const val ROUTE = "discover_people/{categoryIds}"
        fun createRoute(categoryIds: String) = "discover_people/$categoryIds"
    }

    data object ExchangePlan : NavRoutes() {
        const val ROUTE = "exchange_plan/{planId}?isReceived={isReceived}"

        fun createRoute(
            planId: Long,
            isReceived: Boolean
        ): String = "exchange_plan/$planId?isReceived=$isReceived"
    }
    data object PlanRequests : NavRoutes() { const val ROUTE = "plan_requests" }
}
