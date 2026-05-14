package com.example.mahilashaktiunnati.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repayments")
data class Repayment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val loanId: Int,
    val memberId: Int,
    val amount: Double,
    val date: String
)