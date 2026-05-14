package com.example.mahilashaktiunnati

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mahilashaktiunnati.data.model.LoanWithMember
import com.example.mahilashaktiunnati.viewmodel.MainViewModel
import java.text.DecimalFormat

class RepaymentActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var activeLoans: List<LoanWithMember> = emptyList()

    private var primaryColor = "#2D7454"
    private var accentColor = "#7B5E8E"

    private val df = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repayment)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val loanSpinner = findViewById<Spinner>(R.id.loanSpinner)
        val loanInfoText = findViewById<TextView>(R.id.loanInfoText)
        val repaymentAmountEt = findViewById<EditText>(R.id.repaymentAmountEt)
        val saveRepaymentBtn = findViewById<Button>(R.id.saveRepaymentBtn)

        applySavedTheme()

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }

        viewModel.getActiveLoansWithMember().observe(this) { loans ->

            activeLoans = loans

            val displayList = if (loans.isEmpty()) {

                listOf("No active loans")

            } else {

                loans.map {
                    "${it.name} | Total: ₹${df.format(it.totalRepayable)}"
                }
            }

            loanSpinner.adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                displayList
            )

            if (loans.isNotEmpty()) {

                val firstLoan = loans[0]

                val outstanding =
                    firstLoan.totalRepayable - firstLoan.amountPaid

                loanInfoText.text =
                    "Principal: ₹${df.format(firstLoan.principal)}" +
                            "\nTotal Repayable: ₹${df.format(firstLoan.totalRepayable)}" +
                            "\nPaid: ₹${df.format(firstLoan.amountPaid)}" +
                            "\nOutstanding: ₹${df.format(outstanding)}"

            } else {

                loanInfoText.text = "No active loans available"
            }
        }

        loanSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {

                    if (activeLoans.isNotEmpty()) {

                        val loan = activeLoans[position]

                        val outstanding =
                            loan.totalRepayable - loan.amountPaid

                        loanInfoText.text =
                            "Principal: ₹${df.format(loan.principal)}" +
                                    "\nTotal Repayable: ₹${df.format(loan.totalRepayable)}" +
                                    "\nPaid: ₹${df.format(loan.amountPaid)}" +
                                    "\nOutstanding: ₹${df.format(outstanding)}"
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        saveRepaymentBtn.setOnClickListener {

            if (activeLoans.isEmpty()) {

                ToastHelper.show(this, "No active loan available")

                return@setOnClickListener
            }

            val selectedLoan =
                activeLoans[loanSpinner.selectedItemPosition]

            val amount =
                repaymentAmountEt.text.toString().toDoubleOrNull()

            if (amount == null || amount <= 0) {

                ToastHelper.show(this, "Enter valid repayment amount")

                return@setOnClickListener
            }

            val outstanding =
                selectedLoan.totalRepayable - selectedLoan.amountPaid

            val roundedOutstanding =
                df.format(outstanding).toDouble()

            if (amount > roundedOutstanding + 0.01) {

                ToastHelper.show(this, "Amount is greater than outstanding balance")

                return@setOnClickListener
            }

            viewModel.repayLoan(
                selectedLoan.toLoan(),
                amount
            )

            ToastHelper.show(this, "Repayment recorded")

            finish()
        }
    }

    private fun applySavedTheme() {

        val prefs =
            getSharedPreferences("app_settings", MODE_PRIVATE)

        val theme =
            prefs.getString("theme", "Default Green & Purple")

        val isPurple = theme == "Purple" || theme == "Purple Accent"
        val isGreen = theme == "Green" || theme == "Green Only"

        primaryColor =
            if (isPurple)
                "#7B5E8E"
            else
                "#2D7454"

        accentColor =
            if (isGreen)
                "#2D7454"
            else
                "#7B5E8E"

        findViewById<Button>(R.id.backBtn)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.repaymentTitleTv)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.loanInfoText)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<Button>(R.id.saveRepaymentBtn)
            .backgroundTintList =
            ColorStateList.valueOf(
                Color.parseColor(primaryColor)
            )
    }

    override fun onResume() {
        super.onResume()
        applySavedTheme()
    }
}