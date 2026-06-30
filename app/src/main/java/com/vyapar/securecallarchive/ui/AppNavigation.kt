package com.vyapar.securecallarchive.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vyapar.securecallarchive.ui.screens.firstlaunch.FirstLaunchScreen
import com.vyapar.securecallarchive.ui.screens.library.LibraryScreen
import com.vyapar.securecallarchive.ui.screens.login.LoginScreen
import com.vyapar.securecallarchive.ui.screens.playback.PlaybackScreen
import com.vyapar.securecallarchive.ui.screens.settings.SettingsScreen
import hilt.androidx.navigation.compose.hiltViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val isLoggedIn by mainViewModel.isLoggedIn.collectAsStateWithLifecycle(initial = false)
    val hasCompletedSetup by mainViewModel.hasCompletedSetup.collectAsStateWithLifecycle(initial = false)

    val startDestination = when {
        !isLoggedIn -> "login"
        !hasCompletedSetup -> "first_launch"
        else -> "library"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("first_launch") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("first_launch") {
            FirstLaunchScreen(
                onSetupComplete = {
                    navController.navigate("library") {
                        popUpTo("first_launch") { inclusive = true }
                    }
                }
            )
        }

        composable("library") {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        onLibraryClick = {},
                        onSettingsClick = { navController.navigate("settings") }
                    )
                }
            ) { padding ->
                LibraryScreen(
                    onPlaybackClick = { recordingId ->
                        navController.navigate("playback/$recordingId")
                    }
                )
            }
        }

        composable(
            "playback/{recordingId}",
            arguments = listOf(navArgument("recordingId") { type = NavType.LongType })
        ) { backStackEntry ->
            val recordingId = backStackEntry.arguments?.getLong("recordingId") ?: 0L
            PlaybackScreen(
                recordingId = recordingId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable("settings") {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    BottomNavigationBar(
                        selectedSettingsTab = true,
                        onLibraryClick = { 
                            navController.navigate("library") {
                                popUpTo("settings") { inclusive = true }
                            }
                        },
                        onSettingsClick = {}
                    )
                }
            ) { padding ->
                SettingsScreen(
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo("library") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    selectedSettingsTab: Boolean = false,
    onLibraryClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    androidx.compose.material3.NavigationBar {
        androidx.compose.material3.NavigationBarItem(
            icon = { androidx.compose.material.icons.Icons.Default.Home },
            label = { androidx.compose.material3.Text("Library") },
            selected = !selectedSettingsTab,
            onClick = onLibraryClick
        )
        androidx.compose.material3.NavigationBarItem(
            icon = { androidx.compose.material.icons.Icons.Default.Settings },
            label = { androidx.compose.material3.Text("Settings") },
            selected = selectedSettingsTab,
            onClick = onSettingsClick
        )
    }
}
