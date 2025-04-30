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


@Serializable
data class Home(val id: String = "")

@Serializable
data class Detail(val id: String = "")


@Composable
fun Nav(navController: NavHostController, context: Context) {
    val animalHomeViewModel = AnimalHomeViewModel(context.resources.getString(R.string.url_root))
    val animalDetailViewModel = AnimalDetailsViewModel(context.resources.getString(R.string.url_root),animalHomeViewModel)
    NavHost(
        navController,
        startDestination = Home()
    ) {

        composable<Home> {
            //animalHomeViewModel.refresh()
            AnimalHome(
                navController,
                animalHomeViewModel
            )
        }

        composable<Detail> {

            AnimalDetails(animalDetailViewModel)
        }

    }
}