package com.example.myapplication.ui.screens.account

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Typography


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllergiesSelectionScreen(onNavigateUp: () -> Unit, context: Context) {
    var showDialog by remember { mutableStateOf(false) }
    val selectedAllergies = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        loadAllergies(context, selectedAllergies)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }
            },
            title = { Text(text = "성공", style = Typography.bodyLarge) },
            text = { Text("정보가 성공적으로 저장되었습니다.", style = Typography.bodySmall) },
            modifier = Modifier.padding(16.dp)
        )
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "다음 알레르기 목록에서 선택하여 경험을 맞춤화하는 데 도움을 주시기 바랍니다.",
                style = Typography.bodySmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            val allergies = listOf(
                "알류", "우유", "메밀", "땅콩", "대두", "밀", "잣", "호두",
                "게", "새우", "오징어", "고등어", "조개류", "복숭아", "토마토",
                "닭고기", "돼지고기", "쇠고기", "아황산류"
            )


            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(allergies) { allergy ->
                        AllergyItem(
                            allergy = allergy,
                            isSelected = allergy in selectedAllergies,
                            onSelectionChanged = { isSelected ->
                                if (isSelected) {
                                    selectedAllergies.add(allergy)
                                } else {
                                    selectedAllergies.remove(allergy)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    saveAllergies(context, selectedAllergies)
                    showDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("저장", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(5.dp))
            }

        }
    }


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AllergyItem(
    allergy: String,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectionChanged(!isSelected) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = allergy,
            style = Typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = isSelected,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF4CAF50),
                uncheckedColor = Color.LightGray
            )
        )
    }
}

private fun saveAllergies(context: Context, selectedAllergies: List<String>) {
    val sharedPreferences = context.getSharedPreferences("allergies_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putStringSet("selected_allergies", selectedAllergies.toSet())
    editor.apply()
}

private fun loadAllergies(context: Context, selectedAllergies: MutableList<String>) {
    val sharedPreferences = context.getSharedPreferences("allergies_prefs", Context.MODE_PRIVATE)
    val allergiesSet = sharedPreferences.getStringSet("selected_allergies", emptySet())
    if (allergiesSet != null) {
        selectedAllergies.addAll(allergiesSet)
    }
}