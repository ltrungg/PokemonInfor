@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.pokedex.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState   // <-- THÊM DÒNG NÀY
import com.example.pokedex.ui.detail.DetailScreen
import com.example.pokedex.ui.favorites.FavoritesScreen
import com.example.pokedex.ui.list.ListScreen

@Composable
fun NavGraph() {
    val nav = rememberNavController()

    // Quan sát backstack hiện tại
    val backStackEntryState = nav.currentBackStackEntryAsState()
    val route = backStackEntryState.value?.destination?.route

    val bottomRoutes = listOf("list", "favorites")
    val showBottomBar = route in bottomRoutes || route == null

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = route == "list" || route == null,
                        onClick = {
                            nav.navigate("list") {
                                popUpTo(nav.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = route == "favorites",
                        onClick = {
                            nav.navigate("favorites") {
                                popUpTo(nav.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
                        label = { Text("Favorites") }
                    )
                }
            }
        }
    ) { inner ->
        NavHost(
            navController = nav,
            startDestination = "list",
            modifier = Modifier.padding(bottom = inner.calculateBottomPadding())
        ) {
            composable("list") {
                ListScreen(
                    onOpen = { id -> nav.navigate("detail/$id") },
                    onOpenFav = {
                        nav.navigate("favorites") {
                            popUpTo(nav.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(
                route = "detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                DetailScreen(id = id, onBack = { nav.popBackStack() })
            }
            composable("favorites") {
                FavoritesScreen(
                    onOpen = { id -> nav.navigate("detail/$id") },
                    onBack = { nav.popBackStack() }
                )
            }
        }
    }
}
