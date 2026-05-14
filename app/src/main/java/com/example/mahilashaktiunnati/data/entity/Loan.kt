package com.example.mahilashaktiunnati.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "loans",
    foreignKeys = [
        ForeignKey(
            entity = Member::class,
            parentColumns = ["id"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Loan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memberId: Int,
    val principal: Double,
    val rate: Double,
    val durationMonths: Int,
    val interest: Double,
    val totalRepayable: Double,
    val amountPaid: Double = 0.0,
    val isRepaid: Boolean = false,
    val date: String
)