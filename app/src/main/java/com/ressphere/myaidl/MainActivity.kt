package com.ressphere.myaidl

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.ressphere.myaidl.ui.theme.MyAidlTheme



class MainActivity : ComponentActivity() {
    private val backDispatcher by lazy { onBackPressedDispatcher }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            0
        )
        setContent {
            MyAidlTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ServerScreen(applicationContext, backDispatcher)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerScreen(context: Context, backDispatcher: OnBackPressedDispatcher?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween, // or Arrangement.Top
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = "Server") },
            navigationIcon = {
                IconButton(onClick = { backDispatcher?.onBackPressed() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Server",
                style = MaterialTheme.typography.headlineSmall,
                //modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = { startServer(context = context) },
                //modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Start")
            }
            Button(
                onClick = { stopServer(context = context) },
                //modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Stop")
            }
        }
    }
}

fun startServer(context: Context) {
    Intent(context, ServerService::class.java).also {
        it.action = ServerService.Actions.START.toString()
        context.startService(it)
    }
}

fun stopServer(context: Context) {
    Intent(context, ServerService::class.java).also {
        it.action = ServerService.Actions.STOP.toString()
        context.startService(it)
    }
}

@Preview(showBackground = true)
@Composable
fun ServerScreenPreview(
    context: Context = LocalContext.current,
    backDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
) {
    MyAidlTheme {
        ServerScreen(context = context, backDispatcher = backDispatcher)
    }
}
