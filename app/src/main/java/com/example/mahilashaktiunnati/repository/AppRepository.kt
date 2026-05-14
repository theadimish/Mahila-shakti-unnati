package com.example.mahilashaktiunnati.repository

import androidx.lifecycle.LiveData
import com.example.mahilashaktiunnati.data.dao.AppDao
import com.example.mahilashaktiunnati.data.entity.Member
import com.example.mahilashaktiunnati.data.entity.Savings
import com.example.mahilashaktiunnati.data.entity.Loan
import com.example.mahilashaktiunnati.data.entity.Repayment
import com.example.mahilashaktiunnati.data.model.LoanWithMember

class AppRepository(private val dao: AppDao) {

    val members = dao.getAllMembers()
    val totalGroupCapital = dao.getTotalGroupCapital()
    val pendingContributions = dao.getPendingContributions()
    val activeLoanCount = dao.getActiveLoanCount()
    val totalDisbursed = dao.getTotalDisbursed()

    suspend fun addMember(member: Member) {
        dao.insertMember(member)
    }

    suspend fun addSavings(savings: Savings) {
        dao.insertSavings(savings)
    }

    suspend fun applyLoan(loan: Loan) {
        val existingLoan = dao.getActiveLoan(loan.memberId)

        if (existingLoan != null) {
            throw Exception("This member already has an unpaid loan")
        }

        dao.insertLoan(loan)
    }

    suspend fun deleteMember(member: Member) {
        dao.deleteMember(member)
    }

    fun getSavingsForMember(memberId: Int): LiveData<List<Savings>> {
        return dao.getSavingsForMember(memberId)
    }

    fun getActiveLoans(): LiveData<List<Loan>> {
        return dao.getActiveLoans()
    }

    suspend fun updateLoan(loan: Loan) {
        dao.updateLoan(loan)
    }

    fun getPaidSavingsForMember(memberId: Int): LiveData<Double?> {
        return dao.getPaidSavingsForMember(memberId)
    }

    suspend fun addRepayment(repayment: Repayment) {
        dao.insertRepayment(repayment)
    }

    fun getLatestSavingsForMember(memberId: Int): LiveData<Savings?> {
        return dao.getLatestSavingsForMember(memberId)
    }

    fun getAllSavingsWithMember() = dao.getAllSavingsWithMember()

    fun getActiveLoansWithMember(): LiveData<List<LoanWithMember>> {
        return dao.getActiveLoansWithMember()
    }

    fun getMemberByIdSync(memberId: Int) =
        dao.getMemberByIdSync(memberId)
}