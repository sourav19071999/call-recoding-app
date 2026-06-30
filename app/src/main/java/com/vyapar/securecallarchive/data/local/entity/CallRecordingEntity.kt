package com.vyapar.securecallarchive.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "call_recordings")
data class CallRecordingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phoneNumber: String,
    val callType: String, // INCOMING or OUTGOING
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val durationSeconds: Long = 0,
    val filePath: String,
    val fileName: String,
    val isEncrypted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val notes: String = ""
)
