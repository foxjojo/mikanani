package me.mikanani

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalGraphicsContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document


import kotlinx.coroutines.launch
import me.mikanani.data.DayAniData
import me.mikanani.data.DayData
import me.mikanani.data.NumToDay
import me.mikanani.ui.theme.MikananiTheme

class MainActivity : ComponentActivity() {
    enum class LoadState {
        LOADING,
        LOADED,
        ERROR
    }

    class Data {
        var loadState: LoadState = LoadState.LOADING
        lateinit var days: MutableList<DayData>
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MikananiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    var loadData by remember { mutableStateOf(Data()) }
                    LaunchedEffect(loadData) {
                        var rootUrl = resources.getString(R.string.url_root)
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

                    when (loadData.loadState) {
                        LoadState.LOADING -> {
                            Text(text = "Loading", modifier = Modifier.padding(innerPadding))
                        }

                        LoadState.LOADED -> {
                            FormatData(
                                loadData.days,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        LoadState.ERROR -> {
                            Text(text = "Error", modifier = Modifier.padding(innerPadding))
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun FormatData(days: MutableList<DayData>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(days.size) { index ->
            Text(NumToDay.map[days[index].day].toString())
            LazyRow(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                    10.dp
                )
            ) {
                items(days[index].aniList.size) { aniIndex ->
                    AnimCard(days[index].aniList[aniIndex])
                }
            }
        }
    }

}

@Composable
fun AnimCard(info: DayAniData) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .size(width = 140.dp, height = 180.dp)
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

@Preview
@Composable
fun AnimCardPreview() {
    AnimCard(
        DayAniData(
            "https://mikanime.tv/images/Bangumi/202504/18f01f9e.jpg?width=400&height=400&format=webp",
            "test",
            ""
        )
    )
}

@Composable
fun AnimDetail(info: DayAniData) {

}
