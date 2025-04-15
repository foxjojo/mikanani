package me.mikanani.data

data class DayData(
    val day: Int,
    val aniList: MutableList<DayAniData>,
)

data object NumToDay {
    val map: Map<Int, String> = mapOf(
        1 to "星期一",
        2 to "星期二",
        3 to "星期三",
        4 to "星期四",
        5 to "星期五",
        6 to "星期六",
        0 to "星期日",
        7 to "剧场版",
    )
}

data class DayAniData(
    val imgUrl: String,
    val name: String,
    val infoUrl: String,
)

data class AniInfoData(
    val imgUrl: String,
    val name: String,
    val infoUrl: String,
    val subgroupData: SubgroupData,
)

data class SubgroupData(
    val groupName: String,
    val list: MutableList<DownloadData>,
)

data class DownloadData(
    val name: String,
    val magnet: String,
    val size: String,
    val upTime: String,
)