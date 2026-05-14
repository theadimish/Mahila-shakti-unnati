package com.example.mahilashaktiunnati.data.model

import com.example.mahilashaktiunnati.data.entity.Loan

data class LoanWithMember(
    val id: Int,
    val memberId: Int,
    val principal: Double,
    val rate: Double,
    val durationMonths: Int,
    val interest: Double,
    val totalRepayable: Double,
    val amountPaid: Double,
    val isRepaid: Boolean,
    val date: String,
    val name: String
) {
    fun toLoan(): Loan {
        return Loan(
            id = id,
            memberId = memberId,
            principal = principal,
            rate = rate,
            durationMonths = durationMonths,
            interest = interest,
            totalRepayable = totalRepayable,
            amountPaid = amountPaid,
            isRepaid = isRepaid,
            date = date
        )
    }
}