
package com.example.final_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.final_project.ui.theme.Final_projectTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Final_projectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GuessNumber()
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class) //正在使用一個實驗性的 API
@Composable
fun GuessNumber(){
    var guessInput by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var guessHistory by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var secretNumber by remember { mutableStateOf(generateSecretNumber()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    fun resetGame() {
        guessInput = ""
        resultText = ""
        guessHistory = emptyList()
        secretNumber = generateSecretNumber()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = guessInput,
            onValueChange = { guessInput = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                val guessResult = checkGuess(guessInput, secretNumber)
                resultText = guessResult
                guessHistory = guessHistory + Pair(guessInput, guessResult)
                guessInput = ""
            }),
            label = { Text("請輸入") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                keyboardController?.hide()
                val guessResult = checkGuess(guessInput, secretNumber)
                resultText = guessResult
                guessHistory = guessHistory + Pair(guessInput, guessResult)
                guessInput = ""
            },
            enabled = guessInput.length == 4,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("提交")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = resultText)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "猜測歷史：")
        Column {
            guessHistory.forEachIndexed { index, guessResult ->
                Text(text = "猜測 ${index + 1}: ${guessResult.first}, ${guessResult.second}")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                resetGame()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("重新開始")
        }
    }
}

fun generateSecretNumber(): String {
    val numbers = mutableListOf<Int>()
    while (numbers.size < 4) {1
        val num = (0..9).random()
        if (!numbers.contains(num)) {
            numbers.add(num)
        }
    }
    return numbers.joinToString("")
}

fun checkGuess(guess: String, secret: String): String {
    if (guess.toSet().size != guess.length) {
        return "ERROR"
    }

    var aCount = 0
    var bCount = 0
    for (i in guess.indices) {
        val guessChar = guess[i]
        val secretChar = secret[i]
        if (guessChar == secretChar) {
            aCount++
        } else if (secret.contains(guessChar)) {
            bCount++
        }
    }
    return if (aCount == 4) {
        "BINGO!"
    } else {
        "Result: $aCount A $bCount B"
    }
}