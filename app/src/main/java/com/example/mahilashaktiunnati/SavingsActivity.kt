package com.example.mahilashaktiunnati

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mahilashaktiunnati.data.entity.Member
import com.example.mahilashaktiunnati.data.model.SavingsWithMember
import com.example.mahilashaktiunnati.viewmodel.MainViewModel

class SavingsActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var memberList: List<Member> = emptyList()
    private var selectedStatus = "Paid"

    private lateinit var paidToggle: TextView
    private lateinit var pendingToggle: TextView
    private lateinit var recordPaymentBtn: LinearLayout

    private var primaryColor = "#2D7454"
    private var accentColor = "#7B5E8E"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val memberSpinner = findViewById<Spinner>(R.id.memberSpinner)
        val amountEt = findViewById<EditText>(R.id.amountEt)
        paidToggle = findViewById(R.id.paidToggle)
        pendingToggle = findViewById(R.id.pendingToggle)
        recordPaymentBtn = findViewById(R.id.recordPaymentBtn)

        val recycler = findViewById<RecyclerView>(R.id.ledgerRecycler)
        val ledgerSearchEt = findViewById<EditText>(R.id.ledgerSearchEt)

        applySavedTheme()

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }

        fun updateStatusToggleUI() {
            if (selectedStatus == "Paid") {
                paidToggle.text = "Paid"
                paidToggle.setTextColor(Color.WHITE)
                paidToggle.background = getToggleBackground(primaryColor)

                pendingToggle.text = "Pending"
                pendingToggle.setTextColor(Color.parseColor(primaryColor))
                pendingToggle.background = getToggleBackground("#00000000")
            } else {
                pendingToggle.text = "Pending"
                pendingToggle.setTextColor(Color.WHITE)
                pendingToggle.background = getToggleBackground(primaryColor)

                paidToggle.text = "Paid"
                paidToggle.setTextColor(Color.parseColor(primaryColor))
                paidToggle.background = getToggleBackground("#00000000")
            }
        }

        fun selectPaid() {
            selectedStatus = "Paid"
            updateStatusToggleUI()
        }

        fun selectPending() {
            selectedStatus = "Pending"
            updateStatusToggleUI()
        }

        selectPaid()

        paidToggle.setOnClickListener {
            selectPaid()
        }

        pendingToggle.setOnClickListener {
            selectPending()
        }

        viewModel.members.observe(this) { members ->
            memberList = members

            val names = if (members.isEmpty()) {
                listOf("No members available")
            } else {
                listOf("Select Member") + members.map { it.name }
            }

            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                names
            )

            memberSpinner.adapter = adapter
        }

        recycler.layoutManager = LinearLayoutManager(this)

        var fullLedgerList: List<SavingsWithMember> = emptyList()
        val ledgerAdapter = SavingsHistoryAdapter(emptyList())
        recycler.adapter = ledgerAdapter

        fun filterLedger(query: String) {
            val filtered = if (query.isBlank()) {
                fullLedgerList
            } else {
                fullLedgerList.filter {
                    it.memberName.contains(query, ignoreCase = true) ||
                            it.status.contains(query, ignoreCase = true) ||
                            it.weekDate.contains(query, ignoreCase = true)
                }
            }

            ledgerAdapter.updateList(filtered)
        }

        viewModel.getAllSavingsWithMember().observe(this) { list ->
            fullLedgerList = list
            filterLedger(ledgerSearchEt.text.toString())
        }

        ledgerSearchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterLedger(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        recordPaymentBtn.setOnClickListener {

            if (memberList.isEmpty()) {
                ToastHelper.show(this, "Add a member first")
                return@setOnClickListener
            }

            val selectedIndex = memberSpinner.selectedItemPosition

            if (selectedIndex == 0) {
                ToastHelper.show(this, "Please select a member")
                return@setOnClickListener
            }

            val selectedMember = memberList[selectedIndex - 1]

            val amount = amountEt.text.toString().toDoubleOrNull()

            if (amount == null || amount <= 0) {
                amountEt.error = "Enter valid amount"
                return@setOnClickListener
            }

            viewModel.addSavings(
                memberId = selectedMember.id,
                amount = amount,
                status = selectedStatus
            )

            ToastHelper.show(this, "Savings recorded")
            finish()
        }
    }

    private fun getToggleBackground(color: String): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = 60f
        drawable.setColor(Color.parseColor(color))
        return drawable
    }

    private fun applySavedTheme() {

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val theme = prefs.getString("theme", "Default Green & Purple")

        val isPurple = theme == "Purple" || theme == "Purple Accent"
        val isGreen = theme == "Green" || theme == "Green Only"

        primaryColor =
            if (isPurple) "#7B5E8E"
            else "#2D7454"

        accentColor =
            if (isGreen) "#2D7454"
            else "#7B5E8E"

        findViewById<Button>(R.id.backBtn)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.savingsTitleTv)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<ImageView>(R.id.savingsIconIv)
            .setColorFilter(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.recordWeeklyTv)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<ImageView>(R.id.historyIconIv)
            .setColorFilter(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.ledgerHistoryTv)
            .setTextColor(Color.parseColor(accentColor))

        recordPaymentBtn.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        paidToggle.background = getToggleBackground(primaryColor)
        pendingToggle.background = getToggleBackground("#00000000")
        paidToggle.setTextColor(Color.WHITE)
        pendingToggle.setTextColor(Color.parseColor(primaryColor))
    }

    override fun onResume() {
        super.onResume()
        applySavedTheme()
    }
}