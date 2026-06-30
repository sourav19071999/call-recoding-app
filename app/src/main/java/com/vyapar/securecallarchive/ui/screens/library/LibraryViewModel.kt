package com.vyapar.securecallarchive.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vyapar.securecallarchive.data.local.entity.CallRecordingEntity
import com.vyapar.securecallarchive.domain.repository.CallRecordingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: CallRecordingRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val recordings: StateFlow<List<CallRecordingEntity>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getAllCallRecordings()
            } else {
                repository.getAllCallRecordings().map { recordings ->
                    recordings.filter {
                        it.phoneNumber.contains(query, ignoreCase = true) ||
                        it.fileName.contains(query, ignoreCase = true)
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteRecording(recording: CallRecordingEntity) {
        viewModelScope.launch {
            repository.deleteCallRecording(recording)
        }
    }
}
