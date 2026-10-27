package com.phone.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phone.app.core.database.entity.CallRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CallRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallRecord(callRecord: CallRecordEntity)

    @Query("SELECT * FROM call_records ORDER BY timestampMillis DESC")
    fun getAllCallRecords(): Flow<List<CallRecordEntity>>

    @Query("SELECT * FROM call_records WHERE id = :id")
    suspend fun getCallRecordById(id: String): CallRecordEntity?

    @Query("DELETE FROM call_records")
    suspend fun deleteAllCallRecords()
}
