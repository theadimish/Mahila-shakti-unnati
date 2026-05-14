package com.example.mahilashaktiunnati.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mahilashaktiunnati.data.database.AppDatabase
import com.example.mahilashaktiunnati.data.entity.Member
import com.example.mahilashaktiunnati.data.entity.Savings
import com.example.mahilashaktiunnati.repository.AppRepository
import com.example.mahilashaktiunnati.data.entity.Repayment
import com.example.mahilashaktiunnati.data.model.LoanWithMember
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.mahilashaktiunnati.data.entity.Loan

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    val members: LiveData<List<Member>>
    val totalGroupCapital: LiveData<Double?>
    val pendingContributions: LiveData<Int>
    val activeLoanCount: LiveData<Int>
    val totalDisbursed: LiveData<Double?>

    init {
        val dao = AppDatabase.getDatabase(application).appDao()
        repository = AppRepository(dao)

        members = repository.members
        totalGroupCapital = repository.totalGroupCapital
        pendingContributions = repository.pendingContributions
        activeLoanCount = repository.activeLoanCount
        totalDisbursed = repository.totalDisbursed
    }

    fun addMember(name: String, uniqueId: String, phone: String, photoUri: String = "") {
        viewModelScope.launch {
            repository.addMember(
                Member(
                    name = name,
                    uniqueId = uniqueId,
                    phone = phone,
                    photoUri = photoUri
                )
            )
        }
    }

    fun addSavings(memberId: Int, amount: Double, status: String) {
        viewModelScope.launch {
            val today = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Date())

            repository.addSavings(
                Savings(
                    memberId = memberId,
                    amount = amount,
                    status = status,
                    weekDate = today
                )
            )
        }
    }

    fun applyLoan(
        memberId: Int,
        principal: Double,
        rate: Double,
        durationMonths: Int,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val durationYears = durationMonths / 12.0
                val interest = (principal * rate * durationYears) / 100
                val totalRepayable = principal + interest
                val today = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date())

                repository.applyLoan(
                    Loan(
                        memberId = memberId,
                        principal = principal,
                        rate = rate,
                        durationMonths = durationMonths,
                        interest = interest,
                        totalRepayable = totalRepayable,
                        isRepaid = false,
                        date = today
                    )
                )

                onResult(true, "Loan approved. Interest ₹$interest, Total ₹$totalRepayable")
            } catch (e: Exception) {
                onResult(false, e.message ?: "Loan failed")
            }
        }
    }

    fun deleteMember(member: Member) {
        viewModelScope.launch {
            repository.deleteMember(member)
        }
    }

    fun getSavingsForMember(memberId: Int): LiveData<List<Savings>> {
        return repository.getSavingsForMember(memberId)
    }

    fun getActiveLoans(): LiveData<List<Loan>> {
        return repository.getActiveLoans()
    }

    fun repayLoan(loan: Loan, amount: Double) {
        viewModelScope.launch {
            val totalRepayableRounded =
                String.format("%.2f", loan.totalRepayable).toDouble()

            val newPaid =
                String.format("%.2f", loan.amountPaid + amount).toDouble()

            val isFullyPaid = newPaid >= totalRepayableRounded

            val finalPaid =
                if (isFullyPaid) totalRepayableRounded else newPaid

            val updatedLoan = loan.copy(
                amountPaid = finalPaid,
                isRepaid = isFullyPaid
            )

            val today = java.text.SimpleDateFormat(
                "yyyy-MM-dd",
                java.util.Locale.getDefault()
            ).format(java.util.Date())

            repository.addRepayment(
                Repayment(
                    loanId = loan.id,
                    memberId = loan.memberId,
                    amount = amount,
                    date = today
                )
            )

            repository.updateLoan(updatedLoan)
        }
    }

    fun getPaidSavingsForMember(memberId: Int): LiveData<Double?> {
        return repository.getPaidSavingsForMember(memberId)
    }

    fun getLatestSavingsForMember(memberId: Int): LiveData<Savings?> {
        return repository.getLatestSavingsForMember(memberId)
    }

    fun getAllSavingsWithMember() = repository.getAllSavingsWithMember()

    fun getActiveLoansWithMember(): LiveData<List<LoanWithMember>> {
        return repository.getActiveLoansWithMember()
    }

    fun getMemberByIdSync(memberId: Int) =
        repository.getMemberByIdSync(memberId)
}