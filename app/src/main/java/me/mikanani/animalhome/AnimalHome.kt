package me.mikanani.animalhome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import me.mikanani.Detail
import me.mikanani.data.DayAniData
import me.mikanani.data.DayData
import me.mikanani.data.LoadState


@Composable
fun AnimalHome(
    navigators: NavHostController, animalHomeViewModel: AnimalHomeViewModel
) {
    LaunchedEffect(Unit) {
        animalHomeViewModel.refresh()
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        animalHomeViewModel.data
        when (animalHomeViewModel.data.loadState.value) {
            LoadState.LOADING -> {
                Text(text = "Loading", Modifier.padding(innerPadding))
            }

            LoadState.LOADED -> {
                Main(
                    animalHomeViewModel.data.days, navigators, Modifier.padding(innerPadding), animalHomeViewModel
                )
            }

            LoadState.ERROR -> {
                Text(text = "Error", Modifier.padding(innerPadding))
            }
        }
    }

}

@Composable
fun Main(
    days: MutableList<DayData>, navigators: NavHostController, modifier: Modifier, animalHomeViewModel: AnimalHomeViewModel
) {
    val pagerState = rememberPagerState(pageCount = { 8 })
    HorizontalPager(pagerState, modifier.fillMaxSize(), verticalAlignment = Alignment.Top) { day ->
        LazyVerticalStaggeredGrid(StaggeredGridCells.Adaptive(140.dp)) {
            items(days[day].aniList.size) { index ->
                AnimCard(days[day].aniList[index], navigators, animalHomeViewModel)
            }
        }
    }

}


@Composable
fun AnimCard(info: DayAniData, navigators: NavHostController, animalHomeViewModel: AnimalHomeViewModel) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ), modifier = Modifier
            .size(width = 140.dp, height = 180.dp)
            .clickable {
                animalHomeViewModel.curSelect = info
                navigators.navigate(Detail()) { launchSingleTop = true }
            }) {
        Column {
            AsyncImage(model = info.imgUrl, "")
            Text(
                text = info.name, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
            )
        }

    }

}