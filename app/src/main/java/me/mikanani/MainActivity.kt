package me.mikanani

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.network.parseGetRequestBlocking
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.select.Elements
import korlibs.io.resources.resource
import me.mikanani.ui.theme.MikananiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MikananiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )

                    formatData(resources.getString(R.string.url_root))

                    rememberCoroutineScope
                }
            }
        }
    }
}
suspend fun getData(urlRoot: String,callback){
    val doc: Document = Ksoup.parseGetRequest(url = urlRoot)
}
@Composable
suspend fun formatData(urlRoot: String) {
    val doc: Document = Ksoup.parseGetRequest(url = urlRoot)
    val days = doc.getElementsByClass("sk-bangumi")

    days.forEach { element ->
        element.getElementsByClass("an-info").forEach { aniInfo ->
            Column {
                Text(aniInfo.text())
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MikananiTheme {
        Greeting("Android")
    }
}