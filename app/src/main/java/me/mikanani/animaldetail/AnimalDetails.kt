package me.mikanani.animaldetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.network.parseSubmitRequest
import me.mikanani.data.AniInfoData
import me.mikanani.data.DownloadData
import me.mikanani.data.LoadState
import me.mikanani.data.SubgroupData


@Composable
fun AnimalDetails(dayData: AnimalDetailsViewModel) {
    class Data {
        var loadState: LoadState = LoadState.LOADING
        lateinit var info: AniInfoData
    }

    val data by remember { mutableStateOf(Data()) }

    LaunchedEffect(data) {
        var doc =
            Ksoup.parseGetRequest(url = dayData.imgUrl)
        val desc = doc.getElementsByClass("header2-desc").first()?.text()
        var subGroups =
            doc.getElementsByClass("subgroup-text")
        val subGroupsInfoData: MutableList<SubgroupData> = mutableListOf()
        for (subGroup in subGroups) {

            val subInfo =
                subGroup.getElementsByClass("pull-right subgroup-subscribe js-subscribe_bangumi_page")
                    .first()
            val groupName = subGroup.getElementsByTag("a").first()?.text() ?: "Unknown"
            subInfo?.let {
                doc = Ksoup.parseSubmitRequest(
                    url = "https://mikanani.me/Home/ExpandEpisodeTable",
                    mapOf(
                        "bangumiId" to subInfo.attr("data-bangumiid"),
                        "subtitleGroupId" to subInfo.attr("data-subtitlegroupid"),
                        "take" to "65"
                    )
                )
                var list: MutableList<DownloadData> = mutableListOf()
                doc.body().getElementsByTag("tr").forEach {
                    var tds = it.getElementsByTag("td")
                    if (tds.size == 4) {
                        var name = tds[0].getElementsByTag("a")[0].text()
                        var magnet = tds[0].getElementsByTag("a")[1].attr("data-clipboard-text")
                        var size = tds[1].text()
                        var upTime = tds[2].text()
                        list.add(DownloadData(name, magnet, size, upTime))
                    }
                }
                subGroupsInfoData.add(SubgroupData(groupName, list))
            }


        }

        val info = AniInfoData(
            dayData.imgUrl,
            dayData.name,
            desc ?: "Unknown",
            dayData.infoUrl,
            subGroupsInfoData
        )
        data.info = info
        data.loadState = LoadState.LOADED
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (data.loadState) {
            LoadState.LOADING -> TODO()
            LoadState.LOADED -> {
                AnimalDetailsCompose(data.info, Modifier.padding(innerPadding))
            }

            LoadState.ERROR -> TODO()
        }

    }

}

@Composable
private fun AnimalDetailsCompose(aniData: AniInfoData, modifier: Modifier) {

    Column(modifier) {
        Text(aniData.name)
        Text(aniData.desc)
    }


}
