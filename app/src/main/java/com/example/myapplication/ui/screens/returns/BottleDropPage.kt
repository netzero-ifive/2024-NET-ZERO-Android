import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.ui.data.model.BottleReturn
import com.example.myapplication.ui.data.model.Kiosk
import com.example.myapplication.ui.screens.scan.AllergyWarningActivity
import com.example.myapplication.ui.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

enum class LocationType {
    BOTTLE_RETURN, KIOSK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleDropPage(viewModel: MainViewModel) {
    val currentLocation by viewModel.currentLocation.collectAsState()
    val bottomReturns by viewModel.bottleReturns.collectAsState()
    val kiosks by viewModel.kiosks.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        currentLocation?.let {
            position = CameraPosition.fromLatLngZoom(it, 12f)
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedLocationType by remember { mutableStateOf<LocationType>(LocationType.BOTTLE_RETURN) }
    var selectedButton by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        MapSection(cameraPositionState, bottomReturns, kiosks, selectedLocationType)
        Row(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = {
                    selectedLocationType = LocationType.BOTTLE_RETURN
                    showBottomSheet = true
                    selectedButton = 0
                    scope.launch {
                        sheetState.partialExpand()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == 0) Color(0xFF59F28C) else Color.White,
                    contentColor = if (selectedButton == 0) Color.White else Color.Black
                ),
                border = BorderStroke(1.dp, Color(0xFF59F28C)),
                modifier = Modifier.padding(16.dp)
            ) {
                Text("매장 안내")
            }

            Button(
                onClick = {
                    selectedLocationType = LocationType.KIOSK
                    showBottomSheet = true
                    selectedButton = 1
                    scope.launch {
                        sheetState.partialExpand()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == 1) Color(0xFF59F28C) else Color.White,
                    contentColor = if (selectedButton == 1) Color.White else Color.Black
                ),
                border = BorderStroke(1.dp, Color(0xFF59F28C)),
                modifier = Modifier.padding(16.dp)
            ) {
                Text("자판기 위치")
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            scrimColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    if (selectedLocationType == LocationType.BOTTLE_RETURN) "매장 안내" else "자판기 위치",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    when (selectedLocationType) {
                        LocationType.BOTTLE_RETURN -> {
                            items(bottomReturns) { bottleReturn ->
                                LocationItem(bottleReturn, cameraPositionState) {
                                    scope.launch {
                                        sheetState.partialExpand()
                                    }
                                }
                            }
                        }
                        LocationType.KIOSK -> {
                            items(kiosks) { kiosk ->
                                KioskItem(kiosk, cameraPositionState, viewModel) {
                                    scope.launch {
                                        sheetState.partialExpand()
                                    }
                                }
                            }
                        }
                        null -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun MapSection(
    cameraPositionState: CameraPositionState,
    bottomReturns: List<BottleReturn>,
    kiosks: List<Kiosk>,
    selectedLocationType: LocationType?
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            scrollGesturesEnabled = true,
            zoomGesturesEnabled = true,
            tiltGesturesEnabled = true,
        )
    ) {
        when (selectedLocationType) {
            LocationType.BOTTLE_RETURN -> {
                bottomReturns.forEach { bottleReturn ->
                    val latLng = LatLng(bottleReturn.latitude, bottleReturn.longitude)
                    Marker(
                        state = MarkerState(position = latLng),
                        title = "Bottle Return",
                        snippet = bottleReturn.address
                    )
                }
            }
            LocationType.KIOSK -> {
                kiosks.forEach { kiosk ->
                    val latLng = LatLng(kiosk.latitude, kiosk.longitude)
                    Marker(
                        state = MarkerState(position = latLng),
                        title = "Kiosk",
                        snippet = kiosk.address
                    )
                }
            }
            null -> {}
        }
    }
}

@Composable
fun LocationItem(
    bottleReturn: BottleReturn,
    cameraPositionState: CameraPositionState,
    onItemClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = bottleReturn.image,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(bottleReturn.address, fontWeight = FontWeight.Bold)
            Text(bottleReturn.formatted_distance, color = Color.Gray)
        }
        Spacer(modifier = Modifier.width(16.dp))
        TextButton(
            onClick = {
                scope.launch {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newCameraPosition(
                            CameraPosition(LatLng(bottleReturn.latitude, bottleReturn.longitude), 15f, 0f, 0f)
                        ),
                        durationMs = 1000
                    )
                }
                onItemClick()
            },
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.White,
                containerColor = Color(0xFFF5F2F0)
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text("Directions", color = Color.Black, modifier = Modifier.padding(horizontal = 7.dp))
        }
    }
}

@Composable
fun KioskItem(
    kiosk: Kiosk,
    cameraPositionState: CameraPositionState,
    viewModel: MainViewModel,
    onItemClick: () -> Unit,

) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .clickable {
                val intent = Intent(context, AllergyWarningActivity::class.java).apply {
                    val productsJsonArray = JSONArray()

                    kiosk.products.forEach { product ->
                        val productJson = JSONObject().apply {
                            put("name", product.name)
                            put("price", product.price ?: JSONObject.NULL)
                            put("image", product.image ?: JSONObject.NULL)
                            put("size", product.size)
                            put("materials_ko", product.materials_ko ?: JSONObject.NULL)
                            put("nutrients_ko", JSONObject().apply {
                                put("serving_size", product.nutrients_ko.serving_size)
                                put("calories", product.nutrients_ko.calories)
                                put("detail", JSONObject().apply {
                                    product.nutrients_ko.detail.forEach { (key, value) ->
                                        put(key, JSONObject().apply {
                                            put("amount", value.amount)
                                            put("percent", value.percent)
                                        })
                                    }
                                })
                            })
                            put("source_of_manufacture", product.source_of_manufacture ?: JSONObject.NULL)
                            put("caution", product.caution ?: JSONObject.NULL)
                            put("allergens", JSONArray(product.allergens))
                        }
                        productsJsonArray.put(productJson)
                    }

                    putExtra("PRODUCTS_JSON", productsJsonArray.toString())
                }
                context.startActivity(intent)
            }
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = kiosk.image,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(kiosk.address, fontWeight = FontWeight.Bold)
            Text(kiosk.formatted_distance, color = Color.Gray)
        }
        Spacer(modifier = Modifier.width(16.dp))
        TextButton(
            onClick = {
                scope.launch {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newCameraPosition(
                            CameraPosition(LatLng(kiosk.latitude, kiosk.longitude), 15f, 0f, 0f)
                        ),
                        durationMs = 1000
                    )
                }
                onItemClick()
            },
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.White,
                containerColor = Color(0xFFF5F2F0)
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text("Directions", color = Color.Black, modifier = Modifier.padding(horizontal = 7.dp))
        }
    }
}