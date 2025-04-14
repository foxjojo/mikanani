package me.mikanani.data

data class DayData(
    val day: Int,
    val aniList: MutableList<DayAniData>,
)


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