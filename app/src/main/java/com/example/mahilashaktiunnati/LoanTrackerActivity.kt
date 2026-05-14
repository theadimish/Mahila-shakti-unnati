package com.example.mahilashaktiunnati

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mahilashaktiunnati.viewmodel.MainViewModel

class LoanTrackerActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private var primaryColor = "#2D7454"
    private var accentColor = "#7B5E8E"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_tracker)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val activeLoanCountTv = findViewById<TextView>(R.id.activeLoanCountTv)
        val disbursedTv = findViewById<TextView>(R.id.disbursedTv)
        val loanRecycler = findViewById<RecyclerView>(R.id.loanRecycler)
        val addLoanBtn = findViewById<LinearLayout>(R.id.addLoanBtn)

        applySavedTheme()

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }

        addLoanBtn.setOnClickListener {
            startActivity(Intent(this, LoanActivity::class.java))
        }

        loanRecycler.layoutManager = LinearLayoutManager(this)

        val adapter = ActiveLoanAdapter(emptyList())
        loanRecycler.adapter = adapter

        viewModel.activeLoanCount.observe(this) { count ->
            activeLoanCountTv.text = count.toString()
        }

        viewModel.totalDisbursed.observe(this) { total ->
            disbursedTv.text = "₹${total ?: 0.0}"
        }

        viewModel.getActiveLoansWithMember().observe(this) { loans ->
            adapter.updateList(loans)
        }
    }

    private fun applySavedTheme() {

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val theme = prefs.getString("theme", "Default Green & Purple")

        val isPurple = theme == "Purple" || theme == "Purple Accent"
        val isGreen = theme == "Green" || theme == "Green Only"

        primaryColor =
            if (isPurple) "#7B5E8E" else "#2D7454"

        accentColor =
            if (isGreen) "#2D7454" else "#7B5E8E"

        findViewById<Button>(R.id.backBtn)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.loanTrackerTitleTv)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.activeLoanCountTv)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.disbursedTv)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.activePipelineTitleTv)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<LinearLayout>(R.id.addLoanBtn)
            .backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        findViewById<ImageView>(R.id.activeLoanIconIv)
            .setColorFilter(Color.parseColor(primaryColor))

        findViewById<ImageView>(R.id.disbursedIconIv)
            .setColorFilter(Color.parseColor(primaryColor))
    }



    override fun onResume() {
        super.onResume()
        applySavedTheme()
    }
}