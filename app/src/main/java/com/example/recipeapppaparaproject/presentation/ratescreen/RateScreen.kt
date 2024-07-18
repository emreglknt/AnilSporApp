package com.example.recipeapppaparaproject.presentation.ratescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.recipeapppaparaproject.R
import com.example.recipeapppaparaproject.data.model.Player
import com.example.recipeapppaparaproject.presentation.playerViewModel.PlayerViewModel
import com.example.recipeapppaparaproject.presentation.playerViewModel.UIState

@Composable
fun RateScreen(navController: NavController, viewModel: PlayerViewModel = hiltViewModel()) {

    val playersState by viewModel.playersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getPlayers()
    }


    Image(
        painter = painterResource(id = R.drawable.ftballback),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        alpha = 0.7f,
        contentScale = ContentScale.FillBounds
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (playersState) {
            is UIState.Loading -> {
                CircularProgressIndicator()
            }

            is UIState.Success -> {
                val players = (playersState as UIState.Success).data

                LazyColumn (
                    modifier = Modifier.weight(1.5f)
                ){
                    items(players) { player ->
                        PlayerCard(player = player) { formNumber, rating ->
                            viewModel.ratePlayer(formNumber, rating)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }

            is UIState.Error -> {
                Text(
                    text = (playersState as UIState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            is UIState.SuccessPLayer -> TODO()
        }

        Button(
            onClick = {
                navController.navigate("home_screen")
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0d47a1)),
            modifier = Modifier
                .padding(11.dp, 20.dp)
                .fillMaxWidth()
                .height(40.dp)
        ) {

            androidx.compose.material.Text(
                text = "Oylamayı Bitir",
                color = Color.White,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
            )



        }





    }
}

@Composable
fun PlayerCard(player: Player, onRateClicked: (String, Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            shape = CutCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            border = BorderStroke(3.dp, Color.Gray),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                // Sol tarafta resim
                if (player.pp.isNotEmpty()) {
                    val imagePainter = rememberImagePainter(player.pp)
                    Image(
                        painter = imagePainter,
                        contentDescription = null,
                        modifier = Modifier
                            .width(120.dp)
                            .fillMaxHeight()
                            .clip(shape = RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                // Sağ tarafta ad ve soyad
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${player.name} ${player.surname}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Yaş: ${player.age}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    var rating by remember { mutableStateOf(1f) }
                    RateBar(
                        maxStars = 5,
                        rating = rating,
                        onRatingChanged = { newRating ->
                            rating = newRating
                            onRateClicked(player.formanumber, newRating.toInt())
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RateBar(
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    val density = LocalDensity.current.density
    val starSize = (12f * density).dp
    val starSpacing = (0.5f * density).dp

    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Default.Star
            val iconTintColor = if (isSelected) Color(0xFFFFC700) else Color(0x20000000)
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .clickable {
                        onRatingChanged(i.toFloat())
                    }
                    .size(starSize)
            )
            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}

