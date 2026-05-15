package com.mahilashakti.unnati.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberById(id: Int): Member?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMember(member: Member)

    @Delete
    suspend fun deleteMember(member: Member)
}