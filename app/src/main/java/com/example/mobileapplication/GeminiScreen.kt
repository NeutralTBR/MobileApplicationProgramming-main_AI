package com.example.mobileapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun GeminiScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    geminiAIClient: GeminiAIClient = viewModel()
) {
    var prompt by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    val uiState by geminiAIClient.uiState.collectAsState()
    val scrollState = rememberScrollState()

    fun CleanUp(input: String): String {
        val cleanedString = input.replace("*", "")

        return cleanedString
    }

    Column (
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ){

        Box(
            modifier = modifier
                .requiredWidth(width = 430.dp)
                .requiredHeight(height = 350.dp)
                .background(color = Color(0xffC6CBE0))
        ) {
            Box(
                modifier = modifier
                    .requiredWidth(width = 430.dp)
                    .requiredHeight(height = 90.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF0094FE),
                                Color(0xFF61B2FF),
                                Color(0xFFABCDFF)
                            )
                        )
                    )
            )
            Text(
                text = "Buat soal",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 42.dp,
                        y = 36.dp)
                    .requiredWidth(width = 220.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 17.dp,
                        y = 132.dp)
                    .requiredWidth(width = 385.dp)
                    .requiredHeight(height = 43.dp)
            ) {

            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 22.dp,
                        y = 208.dp)
                    .requiredWidth(width = 390.dp)
            ) {
                Box(
                    modifier = Modifier
                        .requiredWidth(width = 390.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(color = Color.White)
                ) {
                    TextField(
                        value = prompt,
                        onValueChange = { newValue: String -> prompt = newValue },
                        label = {Text("Prompt")},
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 20.dp,
                                y = 18.dp
                            )
                            .requiredWidth(width = 220.dp)
                    )
                    Button(
                        onClick = {
                            result = geminiAIClient.sendPrompt(prompt).toString()
                        },
                        enabled = prompt.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 100.dp)
                    ) {
                        Text(text = "Jalankan")
                    }
                }

            }
        }
        SelectionContainer{
            if (uiState is UiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                var textColor = MaterialTheme.colorScheme.onSurface
                if (uiState is UiState.Error) {
                    textColor = MaterialTheme.colorScheme.error
                    result = (uiState as UiState.Error).errorMessage
                } else if (uiState is UiState.Success) {
                    textColor = MaterialTheme.colorScheme.onSurface
                    result = (uiState as UiState.Success).outputText
                }
                Text(
                    text = CleanUp(result),
                    textAlign = TextAlign.Start,
                    color = textColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                        .fillMaxSize()
                )
            }
        }
//        if (result.isNotEmpty()) {
//            Button(
//                onClick = {
//                    answer = geminiAIClient.sendPrompt("Selesaikan soal berikut ini dengan penjelasan yang mudah dimengerti dan lengkap: $result").toString()
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 100.dp)
//            ) {
//                Text(text = "Kunci Jawaban")
//            }
//        }
//        SelectionContainer{
//            if (uiState is UiState.Loading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//            } else {
//                var textColor = MaterialTheme.colorScheme.onSurface
//                if (uiState is UiState.Error) {
//                    textColor = MaterialTheme.colorScheme.error
//                    result = (uiState as UiState.Error).errorMessage
//                } else if (uiState is UiState.Success) {
//                    textColor = MaterialTheme.colorScheme.onSurface
//                    result = (uiState as UiState.Success).outputText
//                }
//                Text(
//                    text = CleanUp(answer),
//                    textAlign = TextAlign.Start,
//                    color = textColor,
//                    modifier = Modifier
//                        .align(Alignment.CenterHorizontally)
//                        .padding(16.dp)
//                        .fillMaxSize()
//                )
//            }
//        }
    }
}