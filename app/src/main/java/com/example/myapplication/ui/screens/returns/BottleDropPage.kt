import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.ui.data.model.BottleReturn
import com.example.myapplication.ui.viewmodel.MainViewModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleDropPage(viewModel: MainViewModel) {

    val currentLocation by viewModel.currentLocation.collectAsState()
    val bottomReturns by viewModel.bottleReturns.collectAsState()
    val kiosk by viewModel.kiosks.collectAsState()

    Log.d("ReZero", "$kiosk")

    val cameraPositionState = rememberCameraPositionState {
        currentLocation?.let {
            position = CameraPosition.fromLatLngZoom(it, 12f)
        }
    }



    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            // Ensuring the Box does not intercept gestures
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .background(Color.White)
            ) {
                MapSection(cameraPositionState, bottomReturns)
            }
        }
        item {
            Text(
                "Nearby locations",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(bottomReturns) { it ->
            LocationItem(it, cameraPositionState = cameraPositionState)
        }
    }
}

@Composable
fun MapSection(cameraPositionState: CameraPositionState, bottomReturns: List<BottleReturn>) {





    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            scrollGesturesEnabled = true,
            zoomGesturesEnabled = true,
            tiltGesturesEnabled = true,

        )
    ) {
        bottomReturns.forEach { bottomReturn ->
            val latLng = LatLng(bottomReturn.latitude, bottomReturn.longitude)
            Marker(
                state = MarkerState(position = latLng),
                title = "Location",
                snippet = "Lat: ${bottomReturn.latitude}, Lng: ${bottomReturn.longitude}"
            )
        }
    }
}

@Composable
fun LocationItem(bottleReturn: BottleReturn, cameraPositionState: CameraPositionState) {
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
            onClick = { scope.launch { cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(LatLng(bottleReturn.latitude,bottleReturn.longitude), 15f, 0f, 0f)
                ),
                durationMs = 1000
            )  }},
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
