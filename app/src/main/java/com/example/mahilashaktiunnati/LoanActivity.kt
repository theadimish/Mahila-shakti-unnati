package com.example.mahilashaktiunnati

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mahilashaktiunnati.data.entity.Member
import com.example.mahilashaktiunnati.viewmodel.MainViewModel
import java.text.DecimalFormat

class LoanActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var memberList: List<Member> = emptyList()
    private var eligibleAmount: Double = 0.0

    private val df = DecimalFormat("#.##")

    private var primaryColor = "#2D7454"
    private var accentColor = "#7B5E8E"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val backBtn = findViewById<Button>(R.id.backBtn)
        val borrowerSpinner = findViewById<Spinner>(R.id.borrowerSpinner)
        val eligibilityText = findViewById<TextView>(R.id.eligibilityText)
        val principalEt = findViewById<EditText>(R.id.principalEt)
        val rateEt = findViewById<EditText>(R.id.rateEt)
        val monthsEt = findViewById<EditText>(R.id.monthsEt)
        val resultText = findViewById<TextView>(R.id.resultText)
        val applyLoanBtn = findViewById<Button>(R.id.applyLoanBtn)

        applySavedTheme()

        backBtn.setOnClickListener {
            finish()
        }

        fun updateCalculation() {
            val principal = principalEt.text.toString().toDoubleOrNull()
            val rate = rateEt.text.toString().toDoubleOrNull()
            val months = monthsEt.text.toString().toIntOrNull()

            if (principal == null || rate == null || months == null || principal <= 0 || rate <= 0 || months <= 0) {
                resultText.text = "Interest and repayable amount will appear here"
                return
            }

            val durationYears = months / 12.0
            val interest = (principal * rate * durationYears) / 100
            val total = principal + interest

            resultText.text = """
                Interest: ₹${df.format(interest)}
                Total Repayable: ₹${df.format(total)}
            """.trimIndent()
        }

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateCalculation()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        principalEt.addTextChangedListener(watcher)
        rateEt.addTextChangedListener(watcher)
        monthsEt.addTextChangedListener(watcher)

        viewModel.members.observe(this) { members ->
            memberList = members

            val names = if (members.isEmpty()) {
                listOf("No members available")
            } else {
                listOf("Choose Borrower") + members.map { it.name }
            }

            borrowerSpinner.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                names
            )
        }

        borrowerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    eligibilityText.text = "Eligible Loan: ₹0"
                    eligibleAmount = 0.0
                    return
                }

                if (memberList.isNotEmpty()) {
                    val selectedMember = memberList[position - 1]

                    viewModel.getPaidSavingsForMember(selectedMember.id)
                        .observe(this@LoanActivity) { savings ->
                            val paidSavings = savings ?: 0.0
                            eligibleAmount = paidSavings * 3
                            eligibilityText.text = "Eligible Loan: ₹${df.format(eligibleAmount)}"
                        }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        applyLoanBtn.setOnClickListener {
            if (memberList.isEmpty()) {
                ToastHelper.show(this, "Add a member first")
                return@setOnClickListener
            }

            if (borrowerSpinner.selectedItemPosition == 0) {
                ToastHelper.show(this, "Please choose a borrower")
                return@setOnClickListener
            }

            val selectedMember = memberList[borrowerSpinner.selectedItemPosition - 1]

            val principal = principalEt.text.toString().toDoubleOrNull()
            val rate = rateEt.text.toString().toDoubleOrNull()
            val months = monthsEt.text.toString().toIntOrNull()

            if (principal == null || principal <= 0) {
                principalEt.error = "Enter valid principal amount"
                return@setOnClickListener
            }

            if (principal > eligibleAmount) {
                principalEt.error = "Max allowed: ₹${df.format(eligibleAmount)}"
                ToastHelper.show(this, "Loan exceeds eligibility!")
                return@setOnClickListener
            }

            if (rate == null || rate <= 0) {
                rateEt.error = "Enter valid interest rate"
                return@setOnClickListener
            }

            if (months == null || months <= 0) {
                monthsEt.error = "Enter valid duration"
                return@setOnClickListener
            }

            updateCalculation()

            viewModel.applyLoan(
                memberId = selectedMember.id,
                principal = principal,
                rate = rate,
                durationMonths = months
            ) { success, message ->

                ToastHelper.show(this, message)

                if (success) {
                    finish()
                }
            }
        }
    }

    private fun applySavedTheme() {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val theme = prefs.getString("theme", "Default Green & Purple")

        val isPurple = theme == "Purple" || theme == "Purple Accent"
        val isGreen = theme == "Green" || theme == "Green Only"

        primaryColor = if (isPurple) "#7B5E8E" else "#2D7454"
        accentColor = if (isGreen) "#2D7454" else "#7B5E8E"

        findViewById<Button>(R.id.backBtn)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.issueCapitalTitleTv)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.eligibilityText)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.resultText)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<Button>(R.id.applyLoanBtn)
            .backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))
    }

    override fun onResume() {
        super.onResume()
        applySavedTheme()
    }
}