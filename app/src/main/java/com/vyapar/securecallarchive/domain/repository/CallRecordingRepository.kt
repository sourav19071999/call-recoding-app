package com.vyapar.securecallarchive.domain.repository

import com.vyapar.securecallarchive.data.local.entity.CallRecordingEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface CallRecordingRepository {
    suspend fun insertCallRecording(recording: CallRecordingEntity): Long
    suspend fun updateCallRecording(recording: CallRecordingEntity)
    suspend fun deleteCallRecording(recording: CallRecordingEntity)
    fun getAllCallRecordings(): Flow<List<CallRecordingEntity>>
    fun getRecordingsByPhoneNumber(phoneNumber: String): Flow<List<CallRecordingEntity>>
    fun getRecordingsByDateRange(fromDate: LocalDateTime, toDate: LocalDateTime): Flow<List<CallRecordingEntity>>
    fun getRecordingsByCallType(callType: String): Flow<List<CallRecordingEntity>>
    fun getRecordingById(id: Long): Flow<CallRecordingEntity?>
    fun getRecordingCount(): Flow<Int>
    fun getTotalDuration(): Flow<Long?>
}
