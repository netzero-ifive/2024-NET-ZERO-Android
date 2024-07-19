package com.example.myapplication.ui.screens.scan

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.ui.data.model.Product
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Typography
import com.example.myapplication.ui.viewmodel.ScanResultViewModel
import com.example.myapplication.ui.viewmodel.ScanResultViewModelFactory

class ScanResultActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: ScanResultViewModel by viewModels { ScanResultViewModelFactory() }

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ScanResultPage(onNavigateUp = { finish() }, viewModel)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScanResultPage(onNavigateUp: () -> Unit, viewModel: ScanResultViewModel) {
        val isLoading by viewModel.isLoading.collectAsState()
        val error by viewModel.error.collectAsState()
        val product by viewModel.product.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadProduct(2)
        }
        Scaffold(
            containerColor = Color.White,
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = onNavigateUp) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                } else if (product != null) {
                    ProductDetails(product!!)
                } else if (error != null) {
                    Text(
                        text = error ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun ProductDetails(product: Product) {
        LazyColumn {
            item {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = product.name,
                        style = Typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "$${product.price}",
                        style = Typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            item {
                CollapsibleSection("Source Of Manufacture", product.source_of_manufacture ?: "")
            }

            item {
                CollapsibleSection("Materials", product.materials_ko ?: "")
            }

            item {
                val nutrientDetails = product.nutrients_ko.detail.map { (key, value) ->
                    "$key: ${value.amount} (${value.percent})"
                }.joinToString("\n")
                CollapsibleSection("Nutrients", nutrientDetails)
            }

            item {
                CollapsibleSection("Allergens", product.allergens.joinToString(", "))
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun CollapsibleSection(title: String, content: String) {
        var expanded by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            onClick = { expanded = !expanded },
            colors = CardDefaults.cardColors(
                containerColor = Color.White // 배경색을 흰색으로 설정
            ),
            border = BorderStroke(1.dp, Color.LightGray), // 테두리 추가
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp // 그림자 제거
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = Typography.bodyLarge,
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
                AnimatedVisibility(visible = expanded) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
