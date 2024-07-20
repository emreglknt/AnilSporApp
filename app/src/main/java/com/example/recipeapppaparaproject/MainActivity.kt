package com.example.recipeapppaparaproject

import WeeklyReminderWorker
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.widget.Constraints
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.recipeapppaparaproject.presentation.ratescreen.RateScreen
import com.example.recipeapppaparaproject.presentation.home.HomeScreen
import com.example.recipeapppaparaproject.presentation.kadrodizlis.LineUpScreen
import com.example.recipeapppaparaproject.presentation.login.LoginScreen
import com.example.recipeapppaparaproject.presentation.playerDetail.PlayerDetailScreen
import com.example.recipeapppaparaproject.presentation.register.RegisterScreen
import com.example.recipeapppaparaproject.presentation.welcome.WelcomeScreen
import com.example.recipeapppaparaproject.ui.theme.RecipeAppPaparaProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeAppPaparaProjectTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    setupNavGraph(navController = navController)
                }
            }
        }
        scheduleWeeklyReminder()

    }


    private fun scheduleWeeklyReminder() {
        val constraints = androidx.work.Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresDeviceIdle(false)
            .build()

        val weeklyWorkRequest = PeriodicWorkRequestBuilder<WeeklyReminderWorker>(2, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "weekly_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            weeklyWorkRequest
        )
    }


}



@Composable
fun setupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "welcome_screen"
    ) {

        composable("welcome_screen") {
            WelcomeScreen(navController = navController)
        }
        composable("login_screen") {
            LoginScreen(navController = navController)
        }
        composable("register_screen") {
             RegisterScreen(navController = navController)
        }
        composable("home_screen") {
             HomeScreen(navController = navController)
        }
        composable("rate_screen") {
            RateScreen(navController = navController)
        }
        composable("lineup_screen") {
            LineUpScreen(navController = navController)
        }

        composable(
            "player_detail_screen/{formaNumber}",
            arguments = listOf(navArgument("formaNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val formaNumber = backStackEntry.arguments?.getString("formaNumber")
            if (formaNumber != null) {
                PlayerDetailScreen(formaNumber)
            }
        }


    }
}




