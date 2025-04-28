package me.mikanani.animalhome

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import kotlinx.coroutines.launch
import me.mikanani.R
import me.mikanani.animaldetail.AnimalDetailsViewModel.Data
import me.mikanani.data.DayAniData
import me.mikanani.data.DayData
import me.mikanani.data.LoadState

class AnimalHomeViewModel(private val rootUrl: String) : ViewModel() {
    lateinit var curSelect: DayAniData
    val data = Data()

    class Data {
        var loadState = mutableStateOf(LoadState.LOADING)
        lateinit var days: MutableList<DayData>
    }

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val doc =
                Ksoup.parseGetRequest(url = rootUrl)
            val days: MutableList<DayData> = mutableListOf()
            val daysClass = doc.getElementsByClass("sk-bangumi")

            daysClass.forEach { element ->
                val dayIndex = element.attr("data-dayofweek")
                val aniList: MutableList<DayAniData> = mutableListOf()
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
            data.days = days
            data.loadState.value = LoadState.LOADED
        }
    }
}
