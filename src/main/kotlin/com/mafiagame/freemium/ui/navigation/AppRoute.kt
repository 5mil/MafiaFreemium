package com.mafiagame.freemium.ui.navigation

/**
 * Navigation routes for the Mafia Freemium app.
 */
sealed class AppRoute(val route: String) {
    data object Home : AppRoute("home")
    data object Shop : AppRoute("shop")
    data object Setup : AppRoute("setup")
    data object RoleReveal : AppRoute("role_reveal")
    data object Night : AppRoute("night")
    data object Day : AppRoute("day")
    data object Settings : AppRoute("settings")
}