package com.example.myapplication.ui.screens.main

import BottleDropPage
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.components.BottomNavigationBar
import com.example.myapplication.ui.components.CommonTopAppBar
import com.example.myapplication.ui.screens.account.AccountPage
import com.example.myapplication.ui.screens.home.HomePage
import com.example.myapplication.ui.screens.scan.ScanPage
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        var selectedScreen by remember { mutableStateOf("Kiosk") }

        // Observe the NavController's back stack
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Update selectedScreen based on the current route
        LaunchedEffect(currentRoute) {
            currentRoute?.let {
                selectedScreen = when (it) {
                    "home" -> "Kiosk"
                    "scan" -> "Scan"
                    "return" -> "Return"
                    "account" -> "Account"
                    else -> "Kiosk"
                }
            }
        }

        Scaffold(
            topBar = { CommonTopAppBar(title = selectedScreen, Icons.Default.Settings) },
            bottomBar = { BottomNavigationBar(navController, currentRoute) { selectedScreen = it } }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { HomePage() }
                composable("scan") { ScanPage() }
                composable("return") { BottleDropPage()
                }
                composable("account") { AccountPage() }
            }
        }
    }
}


