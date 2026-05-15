package com.mahilashakti.unnati.viewmodel

import androidx.lifecycle.*
import com.mahilashakti.unnati.data.SavingsEntry
import com.mahilashakti.unnati.repository.AppRepository
import kotlinx.coroutines.launch

class SavingsViewModel(private val repo: AppRepository) : ViewModel() {

    val totalSavings = repo.totalPaidSavings.asLiveData()

    fun savingsFor(memberId: Int) = repo.getSavingsForMember(memberId).asLiveData()

    fun recordSavings(memberId: Int, amount: Double, status: String) {
        if (amount <= 0) return
        viewModelScope.launch {
            repo.recordSavings(SavingsEntry(memberId = memberId, amount = amount, status = status))
        }
    }

    class Factory(private val repo: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return SavingsViewModel(repo) as T
        }
    }
}