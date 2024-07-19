import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleDropPage() {
    val currentLocation = LatLng(37.7749, -122.4194) // San Francisco as example
    val locations = listOf(
        LatLng(37.7749, -122.4194),
        LatLng(37.7695, -122.4661),
        LatLng(37.7831, -122.4039)
    )
    val dummyData = listOf(
        "Safeway" to "Opens at 8am",
        "Fred Meyer" to "Opens at 9am",
        "Whole Foods" to "Opens at 10am",
        "Safeway" to "Opens at 8am",
        "Fred Meyer" to "Opens at 9am",
        "Whole Foods" to "Opens at 10am"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            MapSection(currentLocation, locations)
        }
        item {
            Text(
                "Nearby locations",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(dummyData) { (name, time) ->
            LocationItem(name, time)
        }
    }
}

@Composable
fun MapSection(currentLocation: LatLng, locations: List<LatLng>) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 12f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        cameraPositionState = cameraPositionState
    ) {
        locations.forEach { location ->
            Marker(
                state = MarkerState(position = location),
                title = "Location",
                snippet = "Lat: ${location.latitude}, Lng: ${location.longitude}"
            )
        }
    }
}

@Composable
fun LocationItem(name: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background), // Your image resource id
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, fontWeight = FontWeight.Bold)
            Text(time, color = Color.Gray)
        }
        Spacer(modifier = Modifier.width(16.dp))
        TextButton(
            onClick = { /* TODO */ },
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
