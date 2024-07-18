package com.example.recipeapppaparaproject.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.recipeapppaparaproject.R
import com.example.recipeapppaparaproject.data.model.Player
import com.example.recipeapppaparaproject.presentation.playerViewModel.PlayerViewModel
import com.example.recipeapppaparaproject.presentation.playerViewModel.UIState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: PlayerViewModel = hiltViewModel()) {

    val playersState by viewModel.playersState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getPlayers()
    }


    Image(
        painter = painterResource(id = R.drawable.ftballback),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        alpha = 0.7f,
        contentScale = ContentScale.FillBounds)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(searchQuery) { query ->
            searchQuery = query
            if (query.isEmpty()) {
                viewModel.getPlayers()
            } else {
                viewModel.searchPlayerByName(query)
            }
        }


        CustomCard(navController)


        Text(
            text = "Anıl Spor Oyuncuları",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 40.dp)
                .align(Alignment.Start)
        )

        when (playersState) {
            is UIState.Loading -> {
                CircularProgressIndicator()
            }

            is UIState.Success -> {
                val players = (playersState as UIState.Success).data


                LazyRow {

                    items(players) { player ->
                        PlayerCard(player = player){
                            navController.navigate("player_detail_screen/${player.formanumber}")
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
    }
}


@Composable
fun SearchBar(searchText: String, onTextChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray.copy(alpha = 0.8f), RoundedCornerShape(18.dp))
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = onTextChange,
                textStyle = TextStyle(color = Color.Black),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent, shape = RoundedCornerShape(16.dp))
            ) {
                if (searchText.isEmpty()) {
                    Text(
                        text = "Search Player",
                        style = TextStyle(color = Color.Gray)
                    )
                }
                it()
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
        }
    }
}








@Composable
fun CustomCard(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, end = 15.dp, start = 15.dp)
            .shadow(12.dp, RoundedCornerShape(25.dp))
            .height(170.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF154FAA), Color(0xFF162263)),
                ),
                shape = RoundedCornerShape(25.dp)
            )
            .padding(16.dp)
            .clickable { navController.navigate("lineup_screen") }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 15.dp)
            ) {
                Text(
                    text = "AnılSporumuzun maçı için ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Kadronu Kur  ➤",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.anll),
                contentDescription = null,
                modifier = Modifier
                    .size(125.dp)
                    .background(Color.Transparent),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}





@Composable
fun PlayerCard(player: Player ,viewModel: PlayerViewModel = hiltViewModel(),onClick: (String) -> Unit,) {

    var rate by remember { mutableStateOf("") }

    LaunchedEffect(player.formanumber) {
        val result = viewModel.getRate(player.formanumber)
        rate = result.toString()
    }
    Box(
        modifier = Modifier.fillMaxSize()
        .clickable { onClick(player.formanumber) },
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .width(250.dp)
                .height(350.dp),
            shape = CutCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            border = BorderStroke(3.dp, Color.Gray),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (player.pp.isNotEmpty()) {
                    val imagePainter = rememberImagePainter(player.pp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        painter = imagePainter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(

                    text = "${player.name} ${player.surname}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Yaş: ${player.age}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Pozisyon: ${player.position}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Memleket: ${player.homeTown}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Gol Sayısı: ${player.goals}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.Start)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text =rate,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF083e94),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(100.dp))

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF083e94)),

                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = player.formanumber,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

            }
        }
    }
}
