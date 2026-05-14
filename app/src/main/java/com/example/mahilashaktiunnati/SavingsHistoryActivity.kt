package com.example.mahilashaktiunnati

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mahilashaktiunnati.data.entity.Savings
import com.example.mahilashaktiunnati.utils.formatCurrency
import com.example.mahilashaktiunnati.viewmodel.MainViewModel

class SavingsHistoryActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private fun applySavedTheme() {

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val selectedTheme = prefs.getString("theme", "Default Green & Purple")

        val isPurple = selectedTheme == "Purple" || selectedTheme == "Purple Accent"

        val primaryColor =
            if (isPurple) "#7B5E8E"
            else "#2D7454"

        findViewById<Button>(R.id.backBtn)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.titleText)
            .setTextColor(Color.parseColor(primaryColor))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savings_history)

        applySavedTheme()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val memberId = intent.getIntExtra("memberId", -1)
        val listView = findViewById<ListView>(R.id.historyList)

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }

        viewModel.getSavingsForMember(memberId).observe(this) { list ->
            val sortedList = list.sortedByDescending { it.id }

            val adapter = object : ArrayAdapter<Savings>(
                this,
                R.layout.item_contribution_history,
                sortedList
            ) {
                override fun getView(
                    position: Int,
                    convertView: android.view.View?,
                    parent: android.view.ViewGroup
                ): android.view.View {
                    val view = convertView ?: layoutInflater.inflate(
                        R.layout.item_contribution_history,
                        parent,
                        false
                    )

                    val saving = sortedList[position]

                    val amountText = view.findViewById<TextView>(R.id.historyAmountText)
                    val dateText = view.findViewById<TextView>(R.id.historyDateText)
                    val statusText = view.findViewById<TextView>(R.id.historyStatusText)

                    amountText.text = formatCurrency(saving.amount)
                    dateText.text = "Date: ${saving.weekDate}"
                    statusText.text = saving.status.uppercase()

                    val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
                    val selectedTheme = prefs.getString("theme", "Default Green & Purple")

                    val isPurple = selectedTheme == "Purple" || selectedTheme == "Purple Accent"

                    val primaryColor =
                        if (isPurple) "#7B5E8E"
                        else "#2D7454"

                    amountText.setTextColor(Color.parseColor(primaryColor))

                    if (saving.status.equals("Paid", ignoreCase = true)) {

                        statusText.setTextColor(Color.parseColor(primaryColor))

                        statusText.setBackgroundColor(
                            Color.parseColor(
                                if (isPurple)
                                    "#F0EAF5"
                                else
                                    "#DDFBE8"
                            )
                        )

                    } else {

                        statusText.setTextColor(Color.parseColor("#D66A00"))

                        statusText.setBackgroundColor(
                            Color.parseColor("#FFF2D9")
                        )
                    }

                    return view
                }
            }

            listView.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        applySavedTheme()
    }
}