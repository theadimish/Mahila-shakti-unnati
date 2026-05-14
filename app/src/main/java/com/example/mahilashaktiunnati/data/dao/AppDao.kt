
package com.example.mahilashaktiunnati.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mahilashaktiunnati.data.entity.Member
import com.example.mahilashaktiunnati.data.entity.Savings
import com.example.mahilashaktiunnati.data.entity.Loan
import com.example.mahilashaktiunnati.data.entity.Repayment
import com.example.mahilashaktiunnati.data.model.SavingsWithMember
import com.example.mahilashaktiunnati.data.model.LoanWithMember
import androidx.room.Delete



import androidx.room.Update


@Dao
interface AppDao {

    @Insert
    suspend fun insertMember(member: Member)

    @Insert
    suspend fun insertSavings(savings: Savings)

    @Insert
    suspend fun insertLoan(loan: Loan)

    @Insert
    suspend fun insertRepayment(repayment: Repayment)

    @Delete
    suspend fun deleteMember(member: Member)

    @Query("SELECT * FROM members ORDER BY id DESC")
    fun getAllMembers(): LiveData<List<Member>>

    @Query("SELECT SUM(amount) FROM savings WHERE status = 'Paid'")
    fun getTotalGroupCapital(): LiveData<Double?>

    @Query("SELECT COUNT(*) FROM savings WHERE status = 'Pending'")
    fun getPendingContributions(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM loans WHERE isRepaid = 0")
    fun getActiveLoanCount(): LiveData<Int>

    @Query("SELECT SUM(principal) FROM loans")
    fun getTotalDisbursed(): LiveData<Double?>

    @Query("SELECT * FROM loans WHERE memberId = :memberId AND isRepaid = 0 LIMIT 1")
    suspend fun getActiveLoan(memberId: Int): Loan?

    @Query("SELECT * FROM savings WHERE memberId = :memberId ORDER BY weekDate DESC")
    fun getSavingsForMember(memberId: Int): LiveData<List<Savings>>

    @Query("SELECT * FROM loans WHERE isRepaid = 0")
    fun getActiveLoans(): LiveData<List<Loan>>

    @Query("SELECT SUM(amount) FROM savings WHERE memberId = :memberId AND status = 'Paid'")
    fun getPaidSavingsForMember(memberId: Int): LiveData<Double?>

    @Update
    suspend fun updateLoan(loan: Loan)

    @Query("SELECT * FROM savings WHERE memberId = :memberId ORDER BY id DESC LIMIT 1")
    fun getLatestSavingsForMember(memberId: Int): LiveData<Savings?>


    @Query("""
    SELECT savings.id, savings.memberId, savings.amount, savings.status, savings.weekDate, members.name
    FROM savings
    INNER JOIN members ON savings.memberId = members.id
    ORDER BY savings.id DESC
""")
    fun getAllSavingsWithMember(): LiveData<List<SavingsWithMember>>


    @Query("""
    SELECT loans.*, members.name
    FROM loans
    INNER JOIN members ON loans.memberId = members.id
    WHERE loans.isRepaid = 0
    ORDER BY loans.id DESC
""")
    fun getActiveLoansWithMember(): LiveData<List<LoanWithMember>>



    @Query("SELECT * FROM members WHERE id = :memberId LIMIT 1")
    fun getMemberByIdSync(memberId: Int): Member?
}