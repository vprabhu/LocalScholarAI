package com.vpdevs.localscholarai.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.genai.summarization.Summarization
import com.google.mlkit.genai.summarization.Summarizer
import com.google.mlkit.genai.summarization.SummarizerOptions
import com.vpdevs.localscholarai.R
import com.vpdevs.localscholarai.ui.viewmodel.MAX_INPUT_CHARACTER_LIMIT
import com.vpdevs.localscholarai.ui.viewmodel.SummarizeViewModel
import com.vpdevs.localscholarai.ui.viewmodel.UiState

@Composable
fun HomeScreen(
    summarizeViewModel: SummarizeViewModel = viewModel()
) {

    val context = LocalContext.current

    val summarizerOptions = SummarizerOptions.builder(context)
        .setInputType(SummarizerOptions.InputType.ARTICLE)
        .setOutputType(SummarizerOptions.OutputType.THREE_BULLETS)
        .setLanguage(SummarizerOptions.Language.ENGLISH)
        .setLongInputAutoTruncationEnabled(true)
        .build()

    val summarizer = Summarization.getClient(summarizerOptions)

    val summarizeText by summarizeViewModel.summarizeText.collectAsState()

    val result = rememberSaveable { mutableStateOf("Summary will appear here") }
    var prompt by rememberSaveable { mutableStateOf("") }
    val isInputLengthError = remember { mutableStateOf(false) }

    val isLoading = remember {
        mutableStateOf(false)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    when (summarizeText) {
        UiState.Initial -> {}

        UiState.Loading -> {
            isLoading.value = true
        }

        is UiState.Success -> {
            isLoading.value = false
            result.value = (summarizeText as UiState.Success).outputText
        }

        is UiState.Error -> {
            isLoading.value = false
            result.value = (summarizeText as UiState.Error).errorMessage
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 64.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black,
            textAlign = TextAlign.Start
        )

        OutlinedTextField(
            label = {
                Text(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    text = stringResource(id = R.string.label_prompt),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )
            },
            value = prompt,
            onValueChange = { prompt = it },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .padding(bottom = 8.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    callSummarizeTextCallback(
                        isInputLengthError = isInputLengthError,
                        prompt = prompt,
                        summarizeViewModel = summarizeViewModel,
                        summarizer = summarizer,
                        result = result,
                        keyboardController = keyboardController
                    )
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
        )
        androidx.compose.animation.AnimatedVisibility(visible = isInputLengthError.value) {
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                text = stringResource(id = R.string.error_min_characters),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Thin,
                color = Color.Red
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black
                    ),
                onClick = {
                    callSummarizeTextCallback(
                        isInputLengthError = isInputLengthError,
                        prompt = prompt,
                        summarizeViewModel = summarizeViewModel,
                        summarizer = summarizer,
                        result = result,
                        keyboardController = keyboardController
                    )
                }
            ) {
                Text(stringResource(id = R.string.button_summarize))
            }

            Button(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black
                    ),
                onClick = {
                    prompt = ""
                }
            ) {
                Text(stringResource(id = R.string.button_clear))
            }
        }

        AnimatedVisibility(
            visible = isLoading.value,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                text = stringResource(id = R.string.text_summarizing),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Thin
            )

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Text(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black
                )
                .fillMaxWidth()
                .weight(0.5f)
                .padding(top = 16.dp),
            text = result.value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            text = stringResource(id = R.string.text_footer),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Thin
        )
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        summarizer.close()
    }
}

private fun callSummarizeTextCallback(
    isInputLengthError: MutableState<Boolean>,
    prompt: String,
    summarizeViewModel: SummarizeViewModel,
    summarizer: Summarizer,
    result: MutableState<String>,
    keyboardController: SoftwareKeyboardController?
) {
    isInputLengthError.value = false
    if (prompt.isNotEmpty() && prompt.length >= MAX_INPUT_CHARACTER_LIMIT) {
        keyboardController?.hide()
        result.value = ""
        summarizeViewModel.summarizeText(
            text = prompt,
            summarizer = summarizer
        )
    } else {
        isInputLengthError.value = true
        Log.d("LocalScholarAI", "Input length must be at least 400 characters ")
    }
}
