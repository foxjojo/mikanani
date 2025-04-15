package me.mikanani

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.nodes.Document

import kotlinx.coroutines.launch
import me.mikanani.ui.theme.MikananiTheme

class MainActivity : ComponentActivity() {
    enum class LoadState {
        LOADING,
        LOADED,
        ERROR
    }

    class Data {
        var loadState: LoadState = LoadState.LOADING
        lateinit var doc: Document
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MikananiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    var loadData by remember { mutableStateOf(Data()) }
                    LaunchedEffect(loadData) {
                        var doc =
                            Ksoup.parseGetRequest(url = resources.getString(R.string.url_root))
                        loadData = Data().apply {
                            this.doc = doc
                            this.loadState = LoadState.LOADED
                        }
                    }

                    when (loadData.loadState) {
                        LoadState.LOADING -> {
                            Text(text = "Loading")
                        }

                        LoadState.LOADED -> {
                            FormatData(
                                loadData.doc,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        LoadState.ERROR -> {
                            Text(text = "Error")
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun FormatData(doc: Document, modifier: Modifier = Modifier) {
    val days = doc.getElementsByClass("sk-bangumi")
    LazyColumn {
        items(days.size) { index ->
            Row {
                Text(days[index].text(), modifier = modifier)
            }
        }
    }
//    days.forEach { element ->
//        element.getElementsByClass("an-info").forEach { aniInfo ->
//
//                items() {  }
//                Text(aniInfo.text(), modifier = modifier)
//            }
//        }
//    }
}

