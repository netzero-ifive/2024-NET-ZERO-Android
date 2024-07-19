package com.example.myapplication.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.myapplication.R

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?, onScreenSelected: (String) -> Unit) {
    NavigationBar(
        containerColor = Color.White
    ) {
        val navigationItems = listOf(
            NavigationItem("home", "Kiosk", R.drawable.home_icon_unselected, R.drawable.home_icon_selected),
            NavigationItem("scan", "Scan", R.drawable.scan_icon_unselected, R.drawable.scan_icon_selected),
            NavigationItem("return", "Return", R.drawable.return_icon_unselected, R.drawable.return_icon_selected),
            NavigationItem("account", "Account", R.drawable.account_icon_unselected, R.drawable.account_icon_selected)
        )

        Log.d("REzero", "Current route: $currentRoute")

        navigationItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(
                            id = if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon
                        ),
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.startDestinationId)
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                    }
                    onScreenSelected(item.title)
                },
                colors = NavigationBarItemDefaults.colors(

                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

data class NavigationItem(
    val route: String,
    val title: String,
    val unselectedIcon: Int,
    val selectedIcon: Int
)