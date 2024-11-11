package com.example.a17practic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a17practic.ui.theme._17PracticTheme
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import androidx.compose.ui.zIndex
import com.google.maps.android.compose.MarkerState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _17PracticTheme {
                AppNavigation()
                }
            }
        }
    }

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "splash") {
        composable(route = "splash") { SplashScreen(navController) }
        composable(route = "login") { LoginScreen(navController) }
        composable(route = "register") { RegisterScreen(navController) }
        composable(route = "map") { MapScreen(navController) }
        composable(route = "map path") { CustomMapScreen(navController) }
        composable(route = "wait timer") { WaitTimerScreen (navController) }
        composable(route = "drive timer") { DriveTimerScreen (navController) }
        composable(route = "drive timer thank you") { DriveTimerThankYouScreen (navController) }
        composable(route = "settings") { SettingsScreen (navController) }
        composable(route = "history") { HistoryScreen (navController) }
    }
}


data class MarkerInfo(val position: LatLng, val iconRes: Int)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapScreen(navController: NavHostController) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.Builder()
            .target(LatLng(55.044026, 82.917393))
            .zoom(10f)
            .build()
    }

    val markers = listOf(
        MarkerInfo(LatLng(55.029986, 82.920432), R.drawable.car1),
        MarkerInfo(LatLng(55.031387, 82.923788), R.drawable.car2),
        MarkerInfo(LatLng(55.026709, 82.922487), R.drawable.car3),
        MarkerInfo(LatLng(55.031387, 82.913288), R.drawable.car4),
    )

    var isMenuOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            markers.forEach { markerInfo ->
                Marker(
                    state = MarkerState(position = markerInfo.position),
                    icon = BitmapDescriptorFactory.fromResource(markerInfo.iconRes)
                )
            }
        }

        IconButton(
            onClick = { isMenuOpen = !isMenuOpen },
            modifier = Modifier
                .padding(top = 40.dp, start = 20.dp)
                .size(55.dp)
                .zIndex(1f)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.burger),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color.Unspecified
            )
        }

        AnimatedVisibility(visible = isMenuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .background(Color(0xFF2A2E43))
                    .align(Alignment.CenterStart)
                    .padding(top = 100.dp, start = 30.dp)
                    .zIndex(2f)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    MenuItem(
                        iconId = R.drawable.iconprofile,
                        text = "Ivanov Ivan\nDriver",
                        onClick = { navController.navigate("settings") }
                    )
                    Spacer(modifier = Modifier.height(60.dp))
                    MenuItem(
                        iconId = R.drawable.iconmenu,
                        text = "History",
                        onClick = { navController.navigate("history") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    MenuItem(
                        iconId = R.drawable.iconsettings,
                        text = "Settings",
                        onClick = { navController.navigate("settings") }
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItem(iconId: Int, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                color = Color.White
            )
        )
    }
}
@Composable
fun HistoryScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2A2E43))
                .height(60.dp)
                .padding(start = 36.dp, top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iconmenu),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "History",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        color = Color.White
                    )
                )
            }
        }
        Column(modifier = Modifier.padding(44.dp)) {
            HistoryItem("Toyota Camry ", "15 min", "$15")
            Spacer(modifier = Modifier.height(44.dp))
            HistoryItem("Kia Rio", "10 min", "$10")
            Spacer(modifier = Modifier.height(44.dp))
            HistoryItem("Kia Ceed", "60 min", "$70")
        }
    }
}

@Composable
fun HistoryItem(carName: String, duration: String, price: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = carName,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            Text(
                text = duration,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
        }
        Text(
            text = price,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        )
    }
}

@Composable
fun CustomMapScreen(navController: NavHostController) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.Builder()
            .target(LatLng(55.029986, 82.920432))
            .zoom(14f)
            .build()
    }
    val markers = listOf(
        MarkerInfo(LatLng(55.029986, 82.920432), R.drawable.purrplecar),
        MarkerInfo(LatLng(55.031387, 82.923788), R.drawable.car1),
        MarkerInfo(LatLng(55.03102, 82.920253), R.drawable.car2),
        MarkerInfo(LatLng(55.03135, 82.916437), R.drawable.car4),
        MarkerInfo(LatLng(55.029348, 82.923562), R.drawable.pointsmol)
    )
    val startMarker = markers[0].position
    val endMarker = markers[4].position

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            markers.forEach { markerInfo ->
                Marker(
                    state = rememberMarkerState(position = markerInfo.position),
                    icon = BitmapDescriptorFactory.fromResource(markerInfo.iconRes)
                )
            }

            Polyline(
                points = listOf(startMarker, endMarker),
                color = Color.Blue,
                width = 5f
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(400 .dp)
                .clip(RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp))
                .background(Color(0xFF2A2E43))
                .padding(top = 16.dp, start = 8.dp, end = 8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    InfoIcon(icon = R.drawable.carblack, text = "Kia Rio")
                    InfoIcon(icon = R.drawable.tapeblack, text = "1.5 km")
                    InfoIcon(icon = R.drawable.peopleblack, text = "5 min-free")
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Price:",
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    PriceOption(label = "Minute", price = "1 min - $1", isHighlighted = true)
                    PriceOption(label = "Hour", price = "60 min - $50", isHighlighted = false)
                    PriceOption(label = "Day", price = "1440 min - $300", isHighlighted = false)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { navController.navigate("wait timer") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFB900))
                ) {
                    Text(
                        "BOOK",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}
@Composable
fun InfoIcon(icon: Int, text: String) {
    Box(
        modifier = Modifier
            .size(110.dp, 90.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFF4D505E))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painter = painterResource(id = icon), contentDescription = text,  modifier = Modifier.size(50.dp))
            Text(text, color = Color.White)
        }
    }
}
@Composable
fun PriceOption(label: String, price: String, isHighlighted: Boolean) {
    val backgroundColor = if (isHighlighted) Color(0xFFFFB900) else Color(0xFF4D505E)

    Box(
        modifier = Modifier
            .size(110.dp, 90.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(backgroundColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(price, color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bluecar),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(200.dp))
            Image(
                painter = painterResource(id = R.drawable.falcon),
                contentDescription = null,
                modifier = Modifier.size(130.dp, 130.dp)
            )
        }
        LaunchedEffect(Unit) {
            delay(3000)
            navController.navigate("login")
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cars),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.White,
                            Color.White
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(250.dp))
            LoginInputField(
                icon = R.drawable.iconprofile,
                placeholder = "Login",
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .width(321.dp)
                    .padding(start = 0.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LoginInputField(
                icon = R.drawable.iconlock,
                placeholder = "Password",
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .width(321.dp)
                    .padding(start = 0.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            TextButton(
                onClick = { navController.navigate("register")},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { navController.navigate("map") },
                modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF3F425C))
            ) {
                Text(
                    text = "SIGN IN",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(150.dp))
            TextButton(
                onClick = { navController.navigate("register")},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Create A New Account?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun LoginInputField(
    icon: Int,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(60.dp)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(50.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(50.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(start = 24.dp, end = 16.dp)
        )

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(45.dp)
                .background(Color.Gray.copy(alpha = 0.6f))
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = Color.Black),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(placeholder, style = TextStyle(color = Color.Gray))
                }
                innerTextField()
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cars),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.White,
                            Color.White
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(250.dp))
            LoginInputField(
                icon = R.drawable.iconprofile,
                placeholder = "Login",
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .width(321.dp)
                    .padding(start = 0.dp)
            )
            LoginInputField(
                icon = R.drawable.iconemail,
                placeholder = "login@mail.ru",
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .width(321.dp)
                    .padding(start = 0.dp)
            )
            LoginInputField(
                icon = R.drawable.iconlock,
                placeholder = "Password",
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .width(321.dp)
                    .padding(start = 0.dp)
            )
            LoginInputField(
                icon = R.drawable.iconlock,
                placeholder = "Password",
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .width(321.dp)
                    .padding(start = 0.dp)
            )
            Spacer(modifier = Modifier.height(46.dp))

            Button(onClick = {navController.navigate("map")},modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF3F425C))
            ) {
                Text(
                    text = "SIGN UP",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }

        }
    }
}

@Composable
fun WaitTimerScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cars),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2A2E43).copy(alpha = 0.3f),
                            Color(0xFF2A2E43),
                            Color(0xFF2A2E43)
                        )
                    )
                )
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A2E43))
            .height(60.dp)
            .padding(start = 36.dp, top = 16.dp)){
            Text(
                text = "Wait Timer",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(300.dp))
            Text(
                text = "00:14:59",
                style = TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(250.dp))
            Button(onClick = { navController.navigate("drive timer") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFCC41ED))
            ) {
                Text(
                    text = "STOP",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
            Button(onClick = { navController.navigate("map path") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFFFB900))
            ) {
                Text(
                    text = "Cancel",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun DriveTimerScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cars),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2A2E43).copy(alpha = 0.3f),
                            Color(0xFF2A2E43),
                            Color(0xFF2A2E43)
                        )
                    )
                )
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A2E43))
            .height(60.dp)
            .padding(start = 36.dp, top = 16.dp)){
            Text(
                text = "Drive Timer",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(300.dp))
            Text(
                text = "00:00:00",
                style = TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(340.dp))
            Button(onClick = { navController.navigate("map") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFCC41ED))
            ) {
                Text(
                    text = "STOP",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun DriveTimerThankYouScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cars),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2A2E43).copy(alpha = 0.3f),
                            Color(0xFF2A2E43),
                            Color(0xFF2A2E43)
                        )
                    )
                )
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A2E43))
            .height(60.dp)
            .padding(start = 36.dp, top = 16.dp)){
            Text(
                text = "Drive Timer",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(200.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .size(300.dp, 350.dp)
                    .background(Color(0xFF4D505E)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Thank you!",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    TextButton(onClick = { navController.navigate("map path") }) {
                        Text("OK", color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(140.dp))
            Button(onClick = { navController.navigate("map path") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFCC41ED))
            ) {
                Text(
                    text = "STOP",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.cars),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2A2E43).copy(alpha = 0.3f),
                            Color(0xFF2A2E43),
                            Color(0xFF2A2E43)
                        )
                    )
                )
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A2E43))
            .height(60.dp)
            .padding(start = 36.dp, top = 16.dp)){
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iconsettings),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Settings",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        color = Color.White
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(300.dp))
            Text(
                text = "Ivanov Ivan",
                style = TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "15 hours      $ 1510",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )
            )
            Text(
                text = "Drive                  Paid",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = "E-mail: ivanov@gmail.com",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White.copy(0.7f)
                )
            )
            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF3F425C))
            ) {
                Text(
                    text = "EXIT",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    _17PracticTheme  {
        SplashScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    _17PracticTheme  {
        LoginScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    _17PracticTheme  {
        RegisterScreen(rememberNavController())
    }
}



@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    _17PracticTheme {
        MapScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun CustomMapScreenPreview() {
    _17PracticTheme {
        CustomMapScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun WaitTimerScreenPreview() {
    _17PracticTheme {
        WaitTimerScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun DriveTimerScreenPreview() {
    _17PracticTheme {
        DriveTimerScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun DriveTimerThankYouScreenPreview() {
    _17PracticTheme {
        DriveTimerThankYouScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    _17PracticTheme {
        SettingsScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    _17PracticTheme {
        HistoryScreen(rememberNavController())
    }
}