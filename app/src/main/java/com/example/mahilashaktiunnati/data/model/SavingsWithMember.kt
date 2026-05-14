package com.example.mahilashaktiunnati.data.model

import androidx.room.ColumnInfo

data class SavingsWithMember(
    val id: Int,
    val memberId: Int,
    val amount: Double,
    val status: String,
    val weekDate: String,

    @ColumnInfo(name = "name")
    val memberName: String
)