//package com.example.myapplication.ui.screens.home
//
//import android.content.Context
//import android.os.Build
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.myapplication.ui.theme.MyApplicationTheme
//import com.example.myapplication.ui.theme.Typography
//
//class AllergiesSelectionActivity : ComponentActivity() {
//    @RequiresApi(Build.VERSION_CODES.Q)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        enableEdgeToEdge()
//        setContent {
//            MyApplicationTheme {
//                AllergiesSelectionScreen(onNavigateUp = { finish() }, context = this)
//            }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.Q)
//    @OptIn(ExperimentalMaterial3Api::class)
//    @Composable
//    fun AllergiesSelectionScreen(onNavigateUp: () -> Unit, context: Context) {
//        var showDialog by remember { mutableStateOf(false) }
//        val selectedAllergies = remember { mutableStateListOf<String>() }
//
//        LaunchedEffect(Unit) {
//            loadAllergies(context, selectedAllergies)
//        }
//
//        if (showDialog) {
//            AlertDialog(
//                onDismissRequest = { showDialog = false },
//                confirmButton = {
//                    Button(
//                        onClick = { showDialog = false }
//                    ) {
//                        Text("OK")
//                    }
//                },
//                title = { Text(text = "Success", style = Typography.bodyLarge) },
//                text = { Text("Allergies saved successfully.", style = Typography.bodySmall) },
//                modifier = Modifier.padding(16.dp)
//            )
//        }
//
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Allergies", style = Typography.bodyLarge) },
//                    navigationIcon = {
//                        IconButton(onClick = onNavigateUp) {
//                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
//                        }
//                    }
//                )
//            }
//        ) { innerPadding ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding)
//                    .padding(horizontal = 16.dp)
//            ) {
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    "Please choose from the following allergies to help us personalize your experience.",
//                    style = Typography.bodySmall
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                val allergies = listOf(
//                    "알류", "우유", "메밀", "땅콩", "대두", "밀", "잣", "호두",
//                    "게", "새우", "오징어", "고등어", "조개류", "복숭아", "토마토",
//                    "닭고기", "돼지고기", "쇠고기", "아황산류"
//                )
//
//
//                Box(modifier = Modifier.weight(1f)) {
//                    LazyColumn(
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        items(allergies) { allergy ->
//                            AllergyItem(
//                                allergy = allergy,
//                                isSelected = allergy in selectedAllergies,
//                                onSelectionChanged = { isSelected ->
//                                    if (isSelected) {
//                                        selectedAllergies.add(allergy)
//                                    } else {
//                                        selectedAllergies.remove(allergy)
//                                    }
//                                }
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = {
//                        saveAllergies(context, selectedAllergies)
//                        showDialog = true
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    shape = RoundedCornerShape(8.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
//                ) {
//                    Text("Save", fontSize = 18.sp)
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.Q)
//    @Composable
//    fun AllergyItem(
//        allergy: String,
//        isSelected: Boolean,
//        onSelectionChanged: (Boolean) -> Unit
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { onSelectionChanged(!isSelected) },
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = allergy,
//                style = Typography.bodySmall,
//                modifier = Modifier.weight(1f)
//            )
//            Checkbox(
//                checked = isSelected,
//                onCheckedChange = null,
//                colors = CheckboxDefaults.colors(
//                    checkedColor = Color(0xFF4CAF50),
//                    uncheckedColor = Color.LightGray
//                )
//            )
//        }
//    }
//
//    private fun saveAllergies(context: Context, selectedAllergies: List<String>) {
//        val sharedPreferences = context.getSharedPreferences("allergies_prefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putStringSet("selected_allergies", selectedAllergies.toSet())
//        editor.apply()
//    }
//
//    private fun loadAllergies(context: Context, selectedAllergies: MutableList<String>) {
//        val sharedPreferences = context.getSharedPreferences("allergies_prefs", Context.MODE_PRIVATE)
//        val allergiesSet = sharedPreferences.getStringSet("selected_allergies", emptySet())
//        if (allergiesSet != null) {
//            selectedAllergies.addAll(allergiesSet)
//        }
//    }
//}
