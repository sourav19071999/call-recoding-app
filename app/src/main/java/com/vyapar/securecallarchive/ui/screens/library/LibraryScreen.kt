package com.vyapar.securecallarchive.ui.screens.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vyapar.securecallarchive.data.local.entity.CallRecordingEntity
import java.time.format.DateTimeFormatter

@Composable
fun LibraryScreen(
    onPlaybackClick: (Long) -> Unit,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val recordings by viewModel.recordings.collectAsStateWithLifecycle(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle(initial = "")
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedRecording by remember { mutableStateOf<CallRecordingEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Call Recordings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Search by number...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            },
            singleLine = true
        )

        // Recordings List
        if (recordings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recordings found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn {
                items(recordings) { recording ->
                    RecordingItem(
                        recording = recording,
                        onPlayClick = { onPlaybackClick(recording.id) },
                        onDeleteClick = {
                            selectedRecording = recording
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && selectedRecording != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Recording?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        selectedRecording?.let { viewModel.deleteRecording(it) }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun RecordingItem(
    recording: CallRecordingEntity,
    onPlayClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onPlayClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recording.phoneNumber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${recording.callType} • ${formatDate(recording.startTime)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Duration: ${formatDuration(recording.durationSeconds)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun formatDate(dateTime: java.time.LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
    return dateTime.format(formatter)
}

private fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, secs)
        else -> String.format("%02d:%02d", minutes, secs)
    }
}
