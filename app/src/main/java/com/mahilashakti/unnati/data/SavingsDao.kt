package com.mahilashakti.unnati.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsDao {
    @Query("SELECT * FROM savings WHERE memberId = :memberId ORDER BY date DESC")
    fun getSavingsForMember(memberId: Int): Flow<List<SavingsEntry>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM savings WHERE status = 'Paid'")
    fun getTotalPaidSavings(): Flow<Double>

    @Insert
    suspend fun insertSavings(entry: SavingsEntry)
}