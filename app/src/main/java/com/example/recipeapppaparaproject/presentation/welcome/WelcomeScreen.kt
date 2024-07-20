package com.example.recipeapppaparaproject.presentation.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.recipeapppaparaproject.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.text.TextStyle


@Composable
fun WelcomeScreen(navController: NavController) {


    Image(
        painter = painterResource(id = R.drawable.girisss),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds)


    Box() {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp)
                .background(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.7f),
                )
                .padding(16.dp)
        ) {

            Text(
                text = "Anıl Spor 'a Hoşgeldiniz",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                letterSpacing = (-1).sp
            )

            Text(
                text = "Anıl Spor olarak, spor tutkunlarının hayallerindeki takımı kurun ve oyuncuları yakından tanıma şansını yakalayın.",
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = (-0.1).sp
            )

            Button(
                onClick = {
                    navController.navigate("login_screen")
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0d47a1)),
                modifier = Modifier
                    .padding(13.dp, 26.dp)
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Başla  ❯❯",
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )

            }

        }
    }


}



