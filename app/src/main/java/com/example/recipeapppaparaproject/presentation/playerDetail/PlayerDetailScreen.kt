package com.example.recipeapppaparaproject.presentation.playerDetail

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.recipeapppaparaproject.R
import com.example.recipeapppaparaproject.data.model.Player
import com.example.recipeapppaparaproject.presentation.playerViewModel.PlayerViewModel
import com.example.recipeapppaparaproject.presentation.playerViewModel.UIState

@Composable
fun PlayerDetailScreen(formNumber: String, viewModel: PlayerViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.getPlayersByFormNumber(formNumber)
    }

    val playersState by viewModel.playersState.collectAsState()


    Image(
        painter = painterResource(id = R.drawable.ftballback),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        alpha = 0.7f,
        contentScale = ContentScale.FillBounds)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        when (playersState) {
            is UIState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is UIState.SuccessPLayer -> {
                val player = (playersState as UIState.SuccessPLayer).data
                PlayerDetail(player)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .padding(bottom = 40.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    BottomInfoBar(player)
                }
            }
            is UIState.Error -> {
                // Error state UI
                val errorMessage = (playersState as UIState.Error).message
                // Display error message
            }
            else -> {
                // Other states (if any)
            }
        }
    }

}

@Composable
fun PlayerDetail(player: Player) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        BackPoster(player = player)

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp)) // ForegroundPoster'ın üst kısmı için boşluk
            ForegroundPoster(player = player)
            Text(
                text = "${player.name} ${player.surname}",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 30.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            TextBuilder(icon = Icons.Filled.Person, title = "Yaş :", bodyText = player.age.toString())
            TextBuilder(icon = Icons.Filled.Info, title = "Pozisyon :", bodyText = player.position)
            TextBuilder(icon = Icons.Filled.Info, title = "Takım  :", bodyText = "Anıl Spor FC")
            TextBuilder(icon = Icons.Filled.Info, title = "Memleket :", bodyText = player.homeTown)

        }
    }
}

@Composable
fun BackPoster(player: Player) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(Color.DarkGray)
    ) {
        Image(
            painter = rememberImagePainter(data = player.pp),
            contentDescription = player.name,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.6f),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .height(250.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.White
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )
    }
}

@Composable
fun ForegroundPoster(player: Player) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .width(250.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = rememberImagePainter(data = player.pp),
            contentDescription = player.name,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .aspectRatio(8f / 6f), // 16:9 en boy oranı
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .width(250.dp)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xB91A1B1B),
                        )
                    ), shape = RoundedCornerShape(16.dp)
                )
        )
    }
}

@Composable
fun TextBuilder(icon: ImageVector, title: String, bodyText: String) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.Black
            )
            Text(
                text = "$title $bodyText",
                modifier = Modifier.padding(start = 3.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun BottomInfoBar(player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(   brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF09446B), // Dark Blue
                    Color(0xFF3B75A3),
                    Color(0xFF316D9E),// Light Blue
                    Color(0xFF09446B)  // Sky Blue
                ),
                startX = 0f,
                endX = 1000f // Uzunluğu istediğiniz bir değere ayarlayın
            ))
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem(title = "Played", value = player.age)
        StatItem(title = "Wins", value = player.age)
        BallIcon()
        StatItem(title = "Goals", value = player.goals)
        StatItem(title = "Assists", value = player.age)
    }
}

@Composable
fun StatItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color =  Color(0xFFFF9800), fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BallIcon() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(Color.White,shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Menu, // Replace with your desired icon
            contentDescription = "Ball",
            modifier = Modifier.size(33.dp)
        )
    }
}
