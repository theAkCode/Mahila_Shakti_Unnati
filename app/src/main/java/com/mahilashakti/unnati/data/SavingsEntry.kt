package com.mahilashakti.unnati.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "savings",
    foreignKeys = [ForeignKey(
        entity = Member::class,
        parentColumns = ["id"],
        childColumns = ["memberId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SavingsEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val memberId: Int,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val status: String = "Paid"   // "Paid" or "Pending"
)