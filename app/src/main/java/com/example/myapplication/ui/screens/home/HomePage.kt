package com.example.myapplication.ui.screens.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.ui.components.CommonTopAppBar
import com.example.myapplication.ui.screens.scan.AllergyWarningActivity

@Composable
fun HomePage() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Main content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            // Image and text overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Barcode Scanner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        "Scan a barcode for allergy information",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Text(
                        "We're committed to providing you with the most up-to-date product information.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List items
            ListItem(
                headlineContent = { Text("Allergies") },
                trailingContent = { Icon(Icons.Default.KeyboardArrowRight, contentDescription = null) },
                modifier = Modifier.clickable {
                    val intent = Intent(context, AllergiesSelectionActivity::class.java)
                    context.startActivity(intent)

                }
            )
            ListItem(
                headlineContent = { Text("Plastic Bottles") },
                trailingContent = { Icon(Icons.Default.KeyboardArrowRight, contentDescription = null) }
            )
        }
    }
}
