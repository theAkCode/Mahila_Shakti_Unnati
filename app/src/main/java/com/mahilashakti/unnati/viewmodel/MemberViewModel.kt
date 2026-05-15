package com.mahilashakti.unnati.viewmodel

import androidx.lifecycle.*
import com.mahilashakti.unnati.data.Member
import com.mahilashakti.unnati.repository.AppRepository
import kotlinx.coroutines.launch

class MemberViewModel(private val repo: AppRepository) : ViewModel() {

    val members = repo.allMembers.asLiveData()

    fun addMember(name: String, phone: String) {
        if (name.isBlank() || phone.length != 10) return
        viewModelScope.launch { repo.addMember(Member(name = name.trim(), phone = phone.trim())) }
    }

    fun deleteMember(member: Member) {
        viewModelScope.launch { repo.deleteMember(member) }
    }

    class Factory(private val repo: AppRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST") return MemberViewModel(repo) as T
        }
    }
}