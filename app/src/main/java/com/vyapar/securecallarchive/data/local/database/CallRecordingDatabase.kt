package com.vyapar.securecallarchive.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vyapar.securecallarchive.data.local.dao.CallRecordingDao
import com.vyapar.securecallarchive.data.local.entity.CallRecordingEntity

@Database(
    entities = [CallRecordingEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
abstract class CallRecordingDatabase : RoomDatabase() {
    abstract fun callRecordingDao(): CallRecordingDao
}
