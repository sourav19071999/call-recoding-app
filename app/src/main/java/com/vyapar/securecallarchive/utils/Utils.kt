package com.vyapar.securecallarchive.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FileNameUtils {
    
    fun generateRecordingFileName(phoneNumber: String, callType: String): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val timestamp = now.format(formatter)
        
        // Clean phone number for filename
        val cleanNumber = phoneNumber.replace("[^0-9+]".toRegex(), "")
        
        return "${cleanNumber}_${timestamp}.mp3"
    }

    fun generateRecordingPath(baseStoragePath: String, phoneNumber: String): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val datePath = now.format(formatter)
        
        return "$baseStoragePath/CallRecordings/$datePath/"
    }

    fun generateFullPath(baseStoragePath: String, phoneNumber: String): String {
        val dirPath = generateRecordingPath(baseStoragePath, phoneNumber)
        val fileName = generateRecordingFileName(phoneNumber, "")
        return dirPath + fileName
    }
}

object DurationUtils {
    
    fun formatDuration(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        
        return when {
            hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, secs)
            else -> String.format("%02d:%02d", minutes, secs)
        }
    }

    fun formatDurationLong(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return when {
            hours > 0 -> "$hours h $minutes m $secs s"
            minutes > 0 -> "$minutes m $secs s"
            else -> "$secs s"
        }
    }
}

object DateUtils {
    
    fun formatDate(dateTime: LocalDateTime, pattern: String = "MMM dd, yyyy"): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return dateTime.format(formatter)
    }

    fun formatDateTime(dateTime: LocalDateTime, pattern: String = "MMM dd, yyyy HH:mm"): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return dateTime.format(formatter)
    }

    fun isToday(dateTime: LocalDateTime): Boolean {
        return dateTime.toLocalDate() == LocalDateTime.now().toLocalDate()
    }

    fun isYesterday(dateTime: LocalDateTime): Boolean {
        return dateTime.toLocalDate() == LocalDateTime.now().toLocalDate().minusDays(1)
    }
}
