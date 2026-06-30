package com.vyapar.securecallarchive.ui.screens.playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vyapar.securecallarchive.data.local.entity.CallRecordingEntity
import com.vyapar.securecallarchive.domain.repository.CallRecordingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val repository: CallRecordingRepository
) : ViewModel() {

    private val _recordingId = MutableStateFlow<Long?>(null)
    val recording: Flow<CallRecordingEntity?> = _recordingId
        .filterNotNull()
        .flatMapLatest { id -> repository.getRecordingById(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    fun loadRecording(recordingId: Long) {
        _recordingId.value = recordingId
        Timber.d("Loading recording: $recordingId")
    }

    fun play() {
        _isPlaying.value = true
        Timber.d("Playing recording")
    }

    fun pause() {
        _isPlaying.value = false
        Timber.d("Paused recording")
    }

    fun seekTo(position: Long) {
        _currentPosition.value = position
        Timber.d("Seek to: $position")
    }

    fun exportRecording() {
        Timber.d("Export recording")
        // TODO: Implement export functionality
    }
}
