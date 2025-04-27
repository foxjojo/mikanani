package me.mikanani

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.mikanani.animaldetail.AnimalDetails
import me.mikanani.animaldetail.AnimalDetailsViewModel
import me.mikanani.animalhome.AnimalHome
import me.mikanani.animalhome.AnimalHomeViewModel
import me.mikanani.data.DayAniData


@Serializable
data class Home(val id: String="")

@Serializable
data class Detail(val dayData: DayAniData)


@Composable
fun Nav(navController: NavHostController, context: Context) {
    val animalHomeViewModel = AnimalHomeViewModel()
    val animalDetailViewModel = AnimalDetailsViewModel()
    NavHost(
        navController,
        startDestination = Home()
    ) {

        composable<Home> {
            AnimalHome(
                context,
                navController,
                animalHomeViewModel
            )
        }

        composable<Detail> {
            AnimalDetails(animalDetailViewModel)
        }

    }
}