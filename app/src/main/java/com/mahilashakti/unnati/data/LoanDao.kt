package com.mahilashakti.unnati.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {
    @Query("SELECT * FROM loans WHERE memberId = :memberId ORDER BY issuedAt DESC")
    fun getLoansForMember(memberId: Int): Flow<List<Loan>>

    @Query("SELECT * FROM loans WHERE memberId = :memberId AND status = 'Active' LIMIT 1")
    suspend fun getActiveLoan(memberId: Int): Loan?

    @Query("SELECT COALESCE(SUM(principal), 0.0) FROM loans")
    fun getTotalLoansIssued(): Flow<Double>

    @Query("SELECT COALESCE(SUM(balance), 0.0) FROM loans WHERE status = 'Active'")
    fun getTotalOutstandingBalance(): Flow<Double>

    @Insert
    suspend fun insertLoan(loan: Loan)

    @Update
    suspend fun updateLoan(loan: Loan)
}