package com.mahilashakti.unnati.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "loans",
    foreignKeys = [ForeignKey(
        entity = Member::class,
        parentColumns = ["id"],
        childColumns = ["memberId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Loan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val memberId: Int,
    val principal: Double,
    val interestRate: Double,   // annual % e.g. 12.0
    val balance: Double,
    val issuedAt: Long = System.currentTimeMillis(),
    val status: String = "Active"  // "Active" or "Repaid"
)