package com.vyapar.securecallarchive.data.local.dao

import androidx.room.*
import com.vyapar.securecallarchive.data.local.entity.CallRecordingEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface CallRecordingDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallRecording(recording: CallRecordingEntity): Long
    
    @Update
    suspend fun updateCallRecording(recording: CallRecordingEntity)
    
    @Delete
    suspend fun deleteCallRecording(recording: CallRecordingEntity)
    
    @Query("SELECT * FROM call_recordings ORDER BY startTime DESC")
    fun getAllCallRecordings(): Flow<List<CallRecordingEntity>>
    
    @Query("SELECT * FROM call_recordings WHERE phoneNumber = :phoneNumber ORDER BY startTime DESC")
    fun getRecordingsByPhoneNumber(phoneNumber: String): Flow<List<CallRecordingEntity>>
    
    @Query("SELECT * FROM call_recordings WHERE startTime >= :fromDate AND startTime <= :toDate ORDER BY startTime DESC")
    fun getRecordingsByDateRange(fromDate: LocalDateTime, toDate: LocalDateTime): Flow<List<CallRecordingEntity>>
    
    @Query("SELECT * FROM call_recordings WHERE callType = :callType ORDER BY startTime DESC")
    fun getRecordingsByCallType(callType: String): Flow<List<CallRecordingEntity>>
    
    @Query("SELECT * FROM call_recordings WHERE id = :id")
    fun getRecordingById(id: Long): Flow<CallRecordingEntity?>
    
    @Query("SELECT COUNT(*) FROM call_recordings")
    fun getRecordingCount(): Flow<Int>
    
    @Query("SELECT SUM(durationSeconds) FROM call_recordings")
    fun getTotalDuration(): Flow<Long?>
}
