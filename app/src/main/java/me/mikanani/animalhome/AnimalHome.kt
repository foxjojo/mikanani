package me.mikanani.animalhome

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import me.mikanani.Detail
import me.mikanani.R
import me.mikanani.data.DayAniData
import me.mikanani.data.DayData
import me.mikanani.data.LoadState
import me.mikanani.data.NumToDay


@Composable
fun AnimalHome(
    context: Context,
    navigators: NavHostController,
    animalHomeViewModel: AnimalHomeViewModel
) {
    class Data {
        var loadState: LoadState = LoadState.LOADING
        lateinit var days: MutableList<DayData>
    }

    var loadData by remember { mutableStateOf(Data()) }
    LaunchedEffect(loadData) {
        var rootUrl = context.resources.getString(R.string.url_root)
        var doc =
            Ksoup.parseGetRequest(url = rootUrl)
        var days: MutableList<DayData> = mutableListOf()
        val daysClass = doc.getElementsByClass("sk-bangumi")

        daysClass.forEach { element ->
            var dayIndex = element.attr("data-dayofweek")
            var aniList: MutableList<DayAniData> = mutableListOf()
            element.getElementsByClass("list-inline an-ul").forEach { ul ->
                ul.getElementsByTag("li").forEach { li ->
                    if (!li.getElementsByTag("a").isEmpty()) {
                        var imgUrl = li.getElementsByTag("span")[0].attr("data-src")
                        var name = li.getElementsByTag("a")[0].attr("title")
                        var url = li.getElementsByTag("a")[0].attr("href")
                        aniList.add(
                            DayAniData(
                                rootUrl + imgUrl,
                                name,
                                rootUrl + url
                            )
                        )
                    }
                }
            }


            days.add(DayData(dayIndex.toInt(), aniList))
        }


        loadData = Data().apply {
            this.days = days
            this.loadState = LoadState.LOADED
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        when (loadData.loadState) {
            LoadState.LOADING -> {
                Text(text = "Loading", Modifier.padding(innerPadding))
            }

            LoadState.LOADED -> {
                FormatData(
                    loadData.days,
                    navigators,
                    Modifier.padding(innerPadding)
                )
            }

            LoadState.ERROR -> {
                Text(text = "Error", Modifier.padding(innerPadding))
            }
        }
    }

}

@Composable
fun FormatData(
    days: MutableList<DayData>,
    navigators: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(days.size) { index ->
            Text(NumToDay.map[days[index].day].toString())
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(
                    10.dp
                )
            ) {
                items(days[index].aniList.size) { aniIndex ->
                    AnimCard(days[index].aniList[aniIndex], navigators)
                }
            }
        }
    }

}


@Composable
fun AnimCard(info: DayAniData, navigators: NavHostController) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .size(width = 140.dp, height = 180.dp)
            .clickable {
                navigators.navigate(Detail(info, Modifier))
            }
    ) {
        Column {
            AsyncImage(model = info.imgUrl, "")
            Text(
                text = info.name,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
        }

    }

}