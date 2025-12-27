package com.charan.habitdiary.presentation.media_viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charan.habitdiary.R
import com.charan.habitdiary.data.repository.FileRepository
import com.charan.habitdiary.presentation.common.model.ToastMessage
import com.charan.habitdiary.utils.ProcessState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MediaViewerViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MediaViewerState())
    val state = _state.asStateFlow()
    private val _effect = MutableSharedFlow<MediaViewerEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event : MediaViewerEvents) {
        when(event){
            is MediaViewerEvents.DownloadImage -> {
                downloadImage(filePath = event.filePath)

            }

            is MediaViewerEvents.ShareImage -> {
                handelShareImage(event.filePath)

            }
        }
    }

    private fun handelShareImage(filePath : String) = viewModelScope.launch {
        val fileUri = fileRepository.getMediaUri(filePath)
        sendEffect(MediaViewerEffect.ShareImage(fileUri))

    }

    private fun downloadImage(filePath : String) = viewModelScope.launch {
        fileRepository.saveMediaToDownloads(filePath).collectLatest { state->
            when(state){
                is ProcessState.Error -> {
                    _state.update {
                        it.copy(
                            isDownloading = false,
                        )
                    }
                    sendEffect(MediaViewerEffect.ShowToast(ToastMessage.Text(state.exception)))


                }
                is ProcessState.Loading -> {
                    _state.update {
                        it.copy(
                            isDownloading = true
                        )
                    }
                }
                ProcessState.NotDetermined -> {

                }
                is ProcessState.Success<*> -> {
                    _state.update {
                        it.copy(
                            isDownloading = false
                        )
                    }
                    sendEffect(MediaViewerEffect.ShowToast(ToastMessage.Res(R.string.saved_to_download)))

                }
            }
        }
    }

    private fun sendEffect(effect : MediaViewerEffect) = viewModelScope.launch {
        _effect.emit(effect)
    }

}