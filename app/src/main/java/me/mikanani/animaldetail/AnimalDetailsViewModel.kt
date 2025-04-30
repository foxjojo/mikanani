package me.mikanani.animaldetail

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.mikanani.R
import me.mikanani.animalhome.AnimalHomeViewModel
import me.mikanani.data.AniInfoData
import me.mikanani.data.DownloadData
import me.mikanani.data.LoadState
import me.mikanani.data.SubgroupData


class AnimalDetailsViewModel(val urlRoot: String, val animalHomeViewModel: AnimalHomeViewModel) : ViewModel() {
    class Data {
        var loadState = mutableStateOf(LoadState.LOADING)
        lateinit var info: AniInfoData
    }

    val data = Data()

    fun refresh() {
        data.loadState.value = LoadState.LOADING
        viewModelScope.launch {
            var doc = Ksoup.parseGetRequest(animalHomeViewModel.curSelect.infoUrl)
            val desc = doc.getElementsByClass("header2-desc").first()?.text()
            var subGroups =
                doc.getElementsByClass("subgroup-text")
            val subGroupsInfoData: MutableList<SubgroupData> = mutableListOf()
            for (subGroup in subGroups) {
                delay(500)
                val subInfo =
                    subGroup.getElementsByClass("pull-right subgroup-subscribe js-subscribe_bangumi_page")
                        .first()
                val groupName = subGroup.getElementsByTag("a").first()?.text() ?: "Unknown"
                subInfo?.let {
                    val subtitleGroupId = subInfo.attr("data-subtitlegroupid")
                    val bangumiId = subInfo.attr("data-bangumiid")
                    doc = Ksoup.parseGetRequest(
                        url = urlRoot + "/Home/ExpandEpisodeTable?bangumiId=${bangumiId}&subtitleGroupId=${subtitleGroupId}&take=65",
                    )
                    var list: MutableList<DownloadData> = mutableListOf()
                    doc.body().getElementsByTag("tr").forEach {
                        var tds = it.getElementsByTag("td")
                        if (tds.size == 5) {
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
                animalHomeViewModel.curSelect.imgUrl,
                animalHomeViewModel.curSelect.name,
                desc ?: "Unknown",
                animalHomeViewModel.curSelect.infoUrl,
                subGroupsInfoData
            )
            data.info = info
            data.loadState.value = LoadState.LOADED


        }
    }
}