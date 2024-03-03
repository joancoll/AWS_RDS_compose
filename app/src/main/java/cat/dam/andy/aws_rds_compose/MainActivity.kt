package cat.dam.andy.aws_rds_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cat.dam.andy.aws_rds_compose.ui.theme.AWS_RDS_composeTheme

class MainActivity : ComponentActivity() {
    // Member variables
    private var searchText by mutableStateOf("")
    private var resultText by mutableStateOf("")
    private val TAG = "RDS - MainActivity"

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AWS_RDS_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        // Input Section
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            value = searchText,
                            onValueChange = { searchText = it },
                            label = { Text(stringResource(id = R.string.name_search)) },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Search
                            ),
                            trailingIcon = {
                                IconButton(onClick = { searchByName() }) {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(id = R.string.search))
                                }
                            }
                        )

                        // Button Section
                        Button(
                            onClick = { searchByName() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Text(stringResource(id = R.string.name_search))
                        }
                        // Instructions Section
                        Text(
                            text = stringResource(id = R.string.instructions),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Results Section
                        Text(
                            text = resultText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                    }
                }
            }
        }
    }

    private fun searchByName() {
        Log.d(TAG, "searchByName $searchText")
        if (searchText.isNotEmpty()) {
            val databaseAdaptor = DatabaseAdaptor()
            databaseAdaptor.getResults(this, searchText)
        } else {
            displayResults(getString(R.string.no_valid_search))
        }
    }

    fun displayResults(res: String) {
        resultText = res
    }
}
