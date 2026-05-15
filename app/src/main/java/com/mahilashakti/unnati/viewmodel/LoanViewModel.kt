package com.mahilashakti.unnati.viewmodel

import androidx.lifecycle.*
import com.mahilashakti.unnati.data.Loan
import com.mahilashakti.unnati.repository.AppRepository
import kotlinx.coroutines.launch

class LoanViewModel(private val repo: AppRepository) : ViewModel() {

    val totalIssued = repo.totalLoansIssued.asLiveData()
    val totalOutstanding = repo.totalOutstanding.asLiveData()

    fun loansFor(memberId: Int) = repo.getLoansForMember(memberId).asLiveData()

    /** Returns error string or null on success */
    suspend fun issueLoan(memberId: Int, amount: Double, rate: Double,
                          groupFund: Double): String? {
        if (amount <= 0 || rate < 0) return "Invalid amount or interest rate"
        if (amount > groupFund) return "Amount exceeds group fund balance"
        if (repo.getActiveLoan(memberId) != null) return "Member already has an unpaid loan"
        repo.issueLoan(Loan(memberId = memberId, principal = amount,
            interestRate = rate, balance = amount))
        return null
    }

    suspend fun recordRepayment(memberId: Int, amount: Double): String? {
        val loan = repo.getActiveLoan(memberId) ?: return "No active loan found"
        if (amount <= 0) return "Enter a valid amount"
        if (amount > loan.balance) return "Amount exceeds outstanding balance"
        repo.recordRepayment(loan, amount)
        return null
    }

    class Factory(private val repo: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return LoanViewModel(repo) as T
        }
    }
}