package com.ressphere.clientapplication

import android.app.Application
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ressphere.clientapplication.ui.theme.MyAidlTheme
import com.ressphere.common.MyData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClientActivity : ComponentActivity() {
    private val backDispatcher:()->Unit = {finish()}
    class ClientViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ClientViewModel::class.java)) {
                return ClientViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
    private val viewModel: ClientViewModel by viewModels {
        ClientViewModelFactory(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAidlTheme {
                val successResult by viewModel.successResult.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ClientScreen(successResult, viewModel::addOperation,
                        viewModel::divideOperation, viewModel::findDataById,
                        backDispatcher)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientScreen(
    successResult: MyData?,
    addOperation: (Int, Int) -> Int = { _,_-> 0 },
    divideOperation: (Float, Float) -> Float = { _,_-> 0.0f },
    findDataById: (String)-> Unit = {_-> Unit},
    backDispatcher: ()->Unit = {}
) {
    val number1 = remember { mutableStateOf("0") }
    val number2 = remember { mutableStateOf("0") }
    val id = remember { mutableStateOf("0") }
    val result = remember { mutableStateOf("") }

    LaunchedEffect(successResult) {
        successResult?.let {
            result.value = "Result: ${it.name}, ${it.age}"
        }
    }
    Column {
        TopAppBar(
            title = { Text(text = "Server") },
            navigationIcon = {
                IconButton(onClick = { backDispatcher() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        // Text boxes for entering numbers
        NumberInput("Number 1", number1)
        NumberInput("Number 2", number2)

        // Buttons for performing operations
        Button(onClick = {
            result.value = addOperation(
            number1.value.toInt(),
            number2.value.toInt()).toString()
        }) {
                Text(text = "Add")
        }

        Button(onClick = { result.value = divideOperation(
            number1.value.toFloat(),
            number2.value.toFloat()).toString() }) {
            Text(text = "Divide")
        }

        Text(text="result: ${result.value}")
        Divider(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            thickness = 1.dp
        )

        NumberInput("ID", id)
        Button(onClick = {
            findDataById(id.value)
        }) {
            Text(text = "Process...")
        }

        Text(text="result: ${result.value}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberInput(label: String, value: MutableState<String>) {
    // You can add any necessary input validation or formatting logic here
    // For simplicity, this example assumes the input is valid numeric values
    TextField(
        value = value.value,
        onValueChange = { value.value = it },
        label = { Text(text = label) }
    )
}

@Preview(showBackground = true)
@Composable
fun ClientScreenPreview() {
    MyAidlTheme {
        ClientScreen(MyData("", 0))
    }
}