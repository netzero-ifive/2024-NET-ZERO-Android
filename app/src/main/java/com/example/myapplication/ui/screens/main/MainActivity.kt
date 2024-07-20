package com.example.myapplication.ui.screens.main

import BottleDropPage
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import com.example.myapplication.ui.screens.home.HomePage
import com.example.myapplication.ui.screens.scan.ScanPage
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodel.MainViewModel
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.filled.ArrowBack
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.ui.data.repository.DataRepository
import com.example.myapplication.ui.screens.account.AllergiesSelectionScreen
import com.example.myapplication.ui.viewmodel.MainViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: MainViewModel
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory()
        )[MainViewModel::class.java]


        requestLocationPermission(viewModel)



        enableEdgeToEdge()
        setContent {
            MyApplicationTheme() {
                MainScreen(viewModel)
            }
        }
    }

    private fun requestLocationPermission(viewModel: MainViewModel) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            getCurrentLocation(viewModel)
        }
    }

    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            getCurrentLocation(viewModel)
        }
    }

    private fun getCurrentLocation(viewModel: MainViewModel) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    viewModel.fetchBottleReturns(it.latitude, it.longitude)
                    viewModel.fetchKiosks(it.latitude, it.longitude)
                    viewModel.updateCurrentLocation(currentLatLng)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun MainScreen(viewModel: MainViewModel) {
        val navController = rememberNavController()
        var selectedScreen by remember { mutableStateOf("Kiosk") }
        var tapBarIcon by remember { mutableStateOf(Icons.Default.Settings) }

        // Observe the NavController's back stack
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val context = LocalContext.current

        // Update selectedScreen based on the current route
        LaunchedEffect(currentRoute) {
            currentRoute?.let {
                selectedScreen = when (it) {
                    "home" -> "앱"
                    "scan" -> "스캔"
                    "return" -> "매장 안내"
                    "account" -> "개인정보"
                    else -> "앱"
                }
                tapBarIcon = when(it){
                    "home" -> Icons.Default.Settings
                    "scan" -> Icons.Default.ArrowBack
                    "return" -> Icons.Default.ArrowBack
                    "account" -> Icons.Default.ArrowBack
                    else -> Icons.Default.ArrowBack

                }
            }
        }


        Scaffold(
            topBar = { CommonTopAppBar(title = selectedScreen, tapBarIcon) },
            bottomBar = { BottomNavigationBar(navController, currentRoute) { selectedScreen = it } }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { HomePage(viewModel) }
                composable("scan") { ScanPage() }
                composable("return") { BottleDropPage(viewModel)
                }
                composable("account") {
                    AllergiesSelectionScreen(onNavigateUp = { }, context =context )}
            }
        }
    }
}


