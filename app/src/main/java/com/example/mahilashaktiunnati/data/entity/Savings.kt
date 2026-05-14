package com.example.mahilashaktiunnati.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "savings",
    foreignKeys = [
        ForeignKey(
            entity = Member::class,
            parentColumns = ["id"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Savings(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memberId: Int,
    val amount: Double,
    val status: String, // Paid or Pending
    val weekDate: String
)