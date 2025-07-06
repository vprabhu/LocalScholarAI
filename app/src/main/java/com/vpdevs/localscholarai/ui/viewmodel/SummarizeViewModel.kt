package com.vpdevs.localscholarai.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.genai.common.DownloadCallback
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import com.google.mlkit.genai.summarization.SummarizationRequest
import com.google.mlkit.genai.summarization.Summarizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val  MAX_INPUT_CHARACTER_LIMIT = 400

class SummarizeViewModel : ViewModel() {


    private val _summarizeText: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val summarizeText: StateFlow<UiState> =
        _summarizeText.asStateFlow()


    fun summarizeText(text: String, summarizer: Summarizer) {
        _summarizeText.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            prepareAndStartSummarization(
                summarizer = summarizer,
                articleToSummarize = text
            )
        }
    }

    private suspend fun prepareAndStartSummarization(
        summarizer: Summarizer,
        articleToSummarize: String
    ) {
        // Check feature availability. Status will be one of the following:
        // UNAVAILABLE, DOWNLOADABLE, DOWNLOADING, AVAILABLE
        val featureStatus = withContext(Dispatchers.IO) {
            summarizer.checkFeatureStatus().get()
        }
        Log.d("LocalScholarAI", "featureStatus: $featureStatus")

        when (featureStatus) {
            FeatureStatus.DOWNLOADABLE -> {
                Log.d("LocalScholarAI", "FeatureStatus.DOWNLOADABLE")
                // Download feature if necessary. If downloadFeature is not called,
                // the first inference request will also trigger the feature to be
                // downloaded if it's not already downloaded.
                summarizer.downloadFeature(object : DownloadCallback {
                    override fun onDownloadStarted(bytesToDownload: Long) {}

                    override fun onDownloadFailed(e: GenAiException) {}

                    override fun onDownloadProgress(totalBytesDownloaded: Long) {}

                    override fun onDownloadCompleted() {
                        startSummarizationRequest(
                            text = articleToSummarize,
                            summarizer = summarizer
                        )
                    }
                })
            }
            FeatureStatus.DOWNLOADING -> {
                Log.d("LocalScholarAI", "FeatureStatus.DOWNLOADING")
                // Inference request will automatically run once feature is
                // downloaded. If Gemini Nano is already downloaded on the device,
                // the feature-specific LoRA adapter model will be downloaded
                // quickly. However, if Gemini Nano is not already downloaded, the
                // download process may take longer.
                startSummarizationRequest(
                    text = articleToSummarize,
                    summarizer = summarizer
                )
            }
            FeatureStatus.AVAILABLE -> {
                Log.d("LocalScholarAI", "FeatureStatus.AVAILABLE")
                startSummarizationRequest(
                    text = articleToSummarize,
                    summarizer = summarizer
                )
            }
        }
    }

    fun startSummarizationRequest(
        text: String,
        summarizer: Summarizer
    ) {
        // Create task request
        val summarizationRequest = SummarizationRequest.builder(text).build()
        // Start summarization request with streaming response
       /* summarizer.runInference(summarizationRequest) { newText ->
            // Show new text in UI
            Log.d("ScholarSampleAI", "startSummarizationRequest: $newText")
        }*/

        // get a non-streaming response from the request
        try {
            val summarizationResult = summarizer.runInference(
                summarizationRequest
            ).get().summary

            _summarizeText.update {
                UiState.Success(summarizationResult)
            }
            Log.d(
                "LocalScholarAI",
                "non-streaming response startSummarizationRequest : $summarizationResult"
            )
        } catch (e: GenAiException) {
            _summarizeText.update {
                UiState.Error(e.message.toString())
            }
        }

    }


}
