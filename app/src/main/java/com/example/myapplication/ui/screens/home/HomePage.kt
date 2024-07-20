package com.example.myapplication.ui.screens.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.Typography
import com.example.myapplication.ui.viewmodel.MainViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomePage(viewModel: MainViewModel) {
    val context = LocalContext.current
    val allergens by viewModel.allergens.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllergens()
    }

    Log.d("ReZero", "$allergens")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Circular percentage indicator
        CircularPercentageIndicator(percentage = 75f)

        Spacer(modifier = Modifier.height(20.dp))

        Text("오늘의 탄소 중립 실천", modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(20.dp))

        // List items
        ListItem(
            headlineContent = { Text("알러지", style = Typography.bodySmall) },
            trailingContent = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.White),
            modifier = Modifier
                .clickable {
                    // Handle click
                }
                .fillMaxWidth()
        )
        ListItem(
            headlineContent = { Text("플라스틱 페트병", style = Typography.bodySmall) },
            trailingContent = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.White),
            modifier = Modifier
                .clickable {
                    // Handle click
                }
                .fillMaxWidth()
        )
    }
}

@Composable
fun CircularPercentageIndicator(
    percentage: Float,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF59F28C),
    strokeWidth: Float = 30f // 두께를 30f로 설정
) {
    val sweepAngle = 360 * percentage / 100
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(200.dp) // 크기를 200dp로 조정
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 바깥 회색 원 그리기
            drawCircle(
                color = Color.LightGray,
                radius = size.minDimension / 2,
                style = Stroke(width = strokeWidth)
            )
            // 퍼센트에 해당하는 부분 그리기
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(
                    (size.width - size.minDimension) / 2,
                    (size.height - size.minDimension) / 2
                ),
                size = Size(size.minDimension, size.minDimension),
                style = Stroke(width = strokeWidth)
            )
        }
        Text(
            text = "${percentage.toInt()}%",
            style = MaterialTheme.typography.bodyLarge,
            color = color
        )
    }


}
