package me.mikanani.animaldetail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.mikanani.data.AniInfoData
import me.mikanani.data.DownloadData
import me.mikanani.data.LoadState
import me.mikanani.data.SubgroupData


@Composable
fun AnimalDetails(animalDetailsViewModel: AnimalDetailsViewModel) {
    LaunchedEffect(Unit) {
        animalDetailsViewModel.refresh()
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (animalDetailsViewModel.data.loadState.value) {
            LoadState.LOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            LoadState.LOADED -> {
                AnimalDetailsCompose(animalDetailsViewModel.data.info, Modifier.padding(innerPadding))
            }

            LoadState.ERROR -> {

            }
        }

    }


}

@Composable
private fun AnimalDetailsCompose(aniData: AniInfoData, modifier: Modifier) {

    val selectedSubgroups = remember { mutableStateOf(aniData.subgroupsData[0]) }
    LazyColumn(modifier) {
        item {
            Text(aniData.name)
            Text(aniData.desc)
            SelectSubgroupsPanel(aniData.subgroupsData, selectedSubgroups)
        }

        items(selectedSubgroups.value.list.size) {
            DataItem(selectedSubgroups.value.list[it])
        }

    }


}

@Composable
private fun SelectSubgroupsPanel(subgroups: MutableList<SubgroupData>, selectedSubgroups: MutableState<SubgroupData>) {
    // Card(modifier = Modifier.size(100.dp)) {
    LazyRow {
        items(subgroups.size) {
            Row {
                RadioButton(selected = selectedSubgroups.value == subgroups[it], onClick = {

                    selectedSubgroups.value = subgroups[it]
                })
                Text(subgroups[it].groupName)
            }
        }
    }
    //  }
}

@Composable
private fun DataItem(item: DownloadData) {
    Row {
        Text(item.name)
        Text(item.size)
        Text(item.upTime)
        Button(onClick = {}) {
            Text("打开")
        }
    }
}
