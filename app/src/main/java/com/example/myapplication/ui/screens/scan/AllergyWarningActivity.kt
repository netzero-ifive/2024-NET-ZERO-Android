package com.example.myapplication.ui.screens.scan

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.ui.data.model.NutrientDetail
import com.example.myapplication.ui.data.model.NutrientsKo
import com.example.myapplication.ui.data.model.Product
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.Typography
import org.json.JSONArray
import org.json.JSONObject

class AllergyWarningActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val productsJsonString = intent.getStringExtra("PRODUCTS_JSON") ?: "[]"
        val products = parseProductsJson(productsJsonString)

        Log.d("Rezero", "$products")


        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AllergyWarningScreen(products)
            }
        }
    }


    private fun parseProductsJson(jsonString: String): List<Product> {
        val productsJsonArray = JSONArray(jsonString)
        return List(productsJsonArray.length()) { index ->
            val productJson = productsJsonArray.getJSONObject(index)
            Product(
                name = productJson.getString("name"),
                price = if (productJson.isNull("price")) null else productJson.getInt("price"),
                image = if (productJson.isNull("image")) null else productJson.getString("image"),
                size = productJson.getString("size"),
                materials_ko = if (productJson.isNull("materials_ko")) null else productJson.getString(
                    "materials_ko"
                ),
                nutrients_ko = parseNutrientsKo(productJson.getJSONObject("nutrients_ko")),
                source_of_manufacture = if (productJson.isNull("source_of_manufacture")) null else productJson.getString(
                    "source_of_manufacture"
                ),
                caution = if (productJson.isNull("caution")) null else productJson.getString("caution"),
                allergens = List(productJson.getJSONArray("allergens").length()) { i ->
                    productJson.getJSONArray("allergens").getString(i)
                }
            )
        }
    }

    private fun parseNutrientsKo(nutrientsJson: JSONObject): NutrientsKo {
        val detailJson = nutrientsJson.getJSONObject("detail")
        val detail = mutableMapOf<String, NutrientDetail>()
        detailJson.keys().forEach { key ->
            val nutrientJson = detailJson.getJSONObject(key)
            detail[key] = NutrientDetail(
                amount = nutrientJson.getString("amount"),
                percent = nutrientJson.getString("percent")
            )
        }
        return NutrientsKo(
            serving_size = nutrientsJson.getString("serving_size"),
            calories = nutrientsJson.getString("calories"),
            detail = detail
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    fun AllergyWarningScreen(products: List<Product>) {
        val context = LocalContext.current
        val sharedPreferences = context.getSharedPreferences("allergies_prefs", Context.MODE_PRIVATE)
        val userAllergies =
            sharedPreferences.getStringSet("selected_allergies", setOf())?.toSet() ?: setOf()

        var selectedProduct by remember { mutableStateOf(products.firstOrNull()) }
        var selectedTab by remember { mutableStateOf(0) }

        Log.d("Allergy뮻", "User Allergies: $userAllergies")


        Column(modifier = Modifier.fillMaxSize()
            .background(Color.White)) {
            // 메인 상품 이미지
            Spacer(modifier = Modifier.height(30.dp))
            selectedProduct?.let { product ->
                Log.d("Allergy뮻", "Product: ${product.name}, Allergens: ${product.allergens}")

                AsyncImage(
                    model = product.image,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(20.dp)
                )
            }

            // 제품 리스트 (좌우 스크롤)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(products) { product ->
                    ProductItem(
                        product = product,
                        isSelected = product == selectedProduct,
                        userAllergies = userAllergies,
                        onClick = { selectedProduct = product }
                    )
                }
            }

            // 선택된 제품 정보
            selectedProduct?.let { product ->
                Text(
                    text = product.name,
                    fontSize = 18.sp,
                    style = Typography.bodyLarge,
                    color = if (product.allergens.any { it in userAllergies }) Color.Red else Color.Black,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Text("$${product.price}", fontSize = 18.sp,modifier = Modifier.padding(horizontal = 12.dp))

                // 탭 레이아웃
                Spacer(modifier = Modifier.height(20.dp))
                TabRow(selectedTabIndex = selectedTab, containerColor = Color.White) {
                    listOf(
                        "Product Info",
                        "Material",
                        "Nutrition",
                        "Source",
                        "Allergen"
                    ).forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }

                // 탭 내용
                when (selectedTab) {
                    0 -> ProductInfoTab(product)
                    1 -> MaterialTab(product)
                    2 -> NutritionTab(product)
                    3 -> SourceTab(product)
                    4 -> AllergenTab(product, userAllergies)
                }
            }
        }
    }

    @Composable
    fun ProductItem(
        product: Product,
        isSelected: Boolean,
        userAllergies: Set<String>,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = onClick),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = "Product Thumbnail",
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp)
            )
            Text(
                text = product.name,
                color = if (product.allergens.any { it in userAllergies }) Color.Red else Color.Black,
                fontSize = 12.sp
            )
        }
    }

    @Composable
    fun ProductInfoTab(product: Product) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Name: ${product.name}")
            Text("Price: $${product.price}")
            Text("Size: ${product.size}")
            Text("Caution: ${product.caution ?: "N/A"}")
        }
    }

    @Composable
    fun MaterialTab(product: Product) {
        Text(product.materials_ko ?: "No material information available", modifier = Modifier.padding(12.dp))
    }

    @Composable
    fun NutritionTab(product: Product) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Serving Size: ${product.nutrients_ko.serving_size}")
            Text("Calories: ${product.nutrients_ko.calories}")
            product.nutrients_ko.detail.forEach { (nutrient, detail) ->
                Text("$nutrient: ${detail.amount} (${detail.percent})")
            }
        }
    }

    @Composable
    fun SourceTab(product: Product) {
        Text(product.source_of_manufacture ?: "No source information available", modifier = Modifier.padding(12.dp))
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun AllergenTab(product: Product, userAllergies: Set<String>) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Allergens:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            FlowRow(

            ) {
                product.allergens.forEach { allergen ->
                    AllergenChip(
                        allergen = allergen,
                        isUserAllergy = allergen in userAllergies
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AllergenChip(allergen: String, isUserAllergy: Boolean) {
        val backgroundColor = if (isUserAllergy) Color.Red.copy(alpha = 0.1f) else Color.LightGray
        val textColor = if (isUserAllergy) Color.Red else Color.Black

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = allergen,
                color = textColor,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }

}