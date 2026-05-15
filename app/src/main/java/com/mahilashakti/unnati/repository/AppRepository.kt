package com.mahilashakti.unnati.repository

import com.mahilashakti.unnati.data.*
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val memberDao: MemberDao,
    private val savingsDao: SavingsDao,
    private val loanDao: LoanDao
) {
    // ── Members ──────────────────────────────────────────────────────────────
    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()

    suspend fun addMember(member: Member) = memberDao.insertMember(member)
    suspend fun deleteMember(member: Member) = memberDao.deleteMember(member)
    suspend fun getMemberById(id: Int) = memberDao.getMemberById(id)

    // ── Savings ──────────────────────────────────────────────────────────────
    fun getSavingsForMember(memberId: Int): Flow<List<SavingsEntry>> =
        savingsDao.getSavingsForMember(memberId)

    val totalPaidSavings: Flow<Double> = savingsDao.getTotalPaidSavings()

    suspend fun recordSavings(entry: SavingsEntry) = savingsDao.insertSavings(entry)

    // ── Loans ────────────────────────────────────────────────────────────────
    fun getLoansForMember(memberId: Int): Flow<List<Loan>> =
        loanDao.getLoansForMember(memberId)

    suspend fun getActiveLoan(memberId: Int): Loan? = loanDao.getActiveLoan(memberId)

    val totalLoansIssued: Flow<Double> = loanDao.getTotalLoansIssued()
    val totalOutstanding: Flow<Double> = loanDao.getTotalOutstandingBalance()

    suspend fun issueLoan(loan: Loan) = loanDao.insertLoan(loan)

    suspend fun recordRepayment(loan: Loan, amount: Double) {
        val newBalance = (loan.balance - amount).coerceAtLeast(0.0)
        val newStatus = if (newBalance == 0.0) "Repaid" else "Active"
        loanDao.updateLoan(loan.copy(balance = newBalance, status = newStatus))
    }
}