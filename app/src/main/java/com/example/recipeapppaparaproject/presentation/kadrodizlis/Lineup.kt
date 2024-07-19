package com.example.recipeapppaparaproject.presentation.kadrodizlis

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.recipeapppaparaproject.R
import com.example.recipeapppaparaproject.data.model.Player
import com.example.recipeapppaparaproject.presentation.playerViewModel.PlayerViewModel
import com.example.recipeapppaparaproject.presentation.playerViewModel.UIState
import com.example.recipeapppaparaproject.utils.captureAndShareScreenshot

@Composable
fun LineUpScreen(navController: NavController, viewModel: PlayerViewModel = hiltViewModel()) {

    val playersState by viewModel.playersState.collectAsState()
    var selectedPosition by remember { mutableStateOf<Position?>(null) }
    var isPlayerSelectionVisible by remember { mutableStateOf(false) }
    val selectedPlayers = remember { mutableStateOf(mutableMapOf<Position, Player>()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getPlayers()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.saha),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Button(
            onClick = {
                val activity = context as? Activity
                activity?.window?.decorView?.let { view ->
                    captureAndShareScreenshot(context, view)
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Text("Share")
        }

        // Forwards
        PositionButton(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp),
            player = selectedPlayers.value[Position.FORWARD],
            onClick = {
                selectedPosition = Position.FORWARD
                isPlayerSelectionVisible = true
            }
        )

        // Midfielders
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PositionButton(
                player = selectedPlayers.value[Position.MIDFIELDER1],
                onClick = {
                    selectedPosition = Position.MIDFIELDER1
                    isPlayerSelectionVisible = true
                }
            )
            Spacer(modifier = Modifier.width(40.dp)) // Aralarında boşluk ekliyoruz
            PositionButton(
                player = selectedPlayers.value[Position.MIDFIELDER2],
                onClick = {
                    selectedPosition = Position.MIDFIELDER2
                    isPlayerSelectionVisible = true
                }
            )
        }

        // Defenders
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 180.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            PositionButton(
                modifier = Modifier.padding(end = 7.dp),
                player = selectedPlayers.value[Position.DEFENDER1],
                onClick = {
                    selectedPosition = Position.DEFENDER1
                    isPlayerSelectionVisible = true
                }
            )
            PositionButton(
                modifier = Modifier.padding(horizontal = 6.dp),
                player = selectedPlayers.value[Position.DEFENDER2],
                onClick = {
                    selectedPosition = Position.DEFENDER2
                    isPlayerSelectionVisible = true
                }
            )
            PositionButton(
                modifier = Modifier.padding(start = 5.dp),
                player = selectedPlayers.value[Position.DEFENDER3],
                onClick = {
                    selectedPosition = Position.DEFENDER3
                    isPlayerSelectionVisible = true
                }
            )
        }

        // Goalkeeper
        PositionButton(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 50.dp),
            player = selectedPlayers.value[Position.GOALKEEPER],
            onClick = {
                selectedPosition = Position.GOALKEEPER
                isPlayerSelectionVisible = true
            }
        )

        if (isPlayerSelectionVisible) {
            when (playersState) {
                is UIState.Loading -> {
                    // Loading state - show a progress indicator or a loading message
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Loading players...")
                    }
                }
                is UIState.Success -> {
                    // Success state - show the player selection dialog
                    PlayerSelectionDialog(
                        players = (playersState as UIState.Success).data,
                        onPlayerSelected = { player ->
                            selectedPosition?.let {
                                selectedPlayers.value[it] = player
                            }
                            isPlayerSelectionVisible = false
                        },
                        onDismiss = { isPlayerSelectionVisible = false }
                    )
                }
                is UIState.Error -> {
                    // Error state - show an error message
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error loading players")
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun PositionButton(modifier: Modifier = Modifier, player: Player?, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFC107))
    ) {
        if (player == null) {
            Text(text = "+", fontSize = 20.sp)
        } else {
            PlayerCard2(player = player)
        }
    }
}

@Composable
fun PlayerCard2(player: Player) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .size(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberImagePainter(player.pp),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = player.name,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

@Composable
fun PlayerSelectionDialog(
    players: List<Player>,
    onPlayerSelected: (Player) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(players) { player ->
                    PlayerCard(player = player) { onPlayerSelected(player) }
                }
            }
        }
    }
}

@Composable
fun PlayerCard(player: Player, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Player Image
            Image(
                painter = rememberImagePainter(player.pp),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Player Info Column
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = "${player.name} ${player.surname}",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Forma Numarası: ${player.formanumber}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                Text(
                    text = "Yaş: ${player.age}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }
        }
    }
}

enum class Position {
    FORWARD, MIDFIELDER1, MIDFIELDER2, DEFENDER1, DEFENDER2, DEFENDER3, GOALKEEPER
}
