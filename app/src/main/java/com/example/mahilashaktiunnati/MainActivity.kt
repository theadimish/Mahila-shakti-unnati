package com.example.mahilashaktiunnati

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.mahilashaktiunnati.data.entity.Member
import com.example.mahilashaktiunnati.utils.formatCurrency
import com.google.firebase.auth.FirebaseAuth
import com.example.mahilashaktiunnati.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var totalCapitalText: TextView
    private lateinit var activeLoansText: TextView
    private lateinit var disbursedText: TextView
    private lateinit var pendingText: TextView
    private lateinit var memberCountText: TextView

    private lateinit var dashboardMembersList: ListView
    private lateinit var dashboardAddMemberBtn: TextView
    private lateinit var dashboardSearchEt: EditText

    private lateinit var addTestMemberBtn: Button
    private lateinit var recordSavingsBtn: Button
    private lateinit var addLoanBtn: Button
    private lateinit var repaymentBtn: Button
    private lateinit var viewMembersBtn: Button
    private lateinit var exportReportBtn: Button
    private lateinit var chatbotBtn: ImageView

    private var dashboardMembers: List<Member> = emptyList()
    private var currentMemberCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val savedGroupName = prefs.getString("group_name", "Mahila Shakti") ?: "Mahila Shakti"
        findViewById<TextView>(R.id.appTitleText).text = savedGroupName

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        totalCapitalText = findViewById(R.id.totalCapitalText)
        activeLoansText = findViewById(R.id.activeLoansText)
        disbursedText = findViewById(R.id.disbursedText)
        pendingText = findViewById(R.id.pendingText)
        memberCountText = findViewById(R.id.memberCountText)

        dashboardMembersList = findViewById(R.id.dashboardMembersList)
        dashboardAddMemberBtn = findViewById(R.id.dashboardAddMemberBtn)
        dashboardSearchEt = findViewById(R.id.dashboardSearchEt)

        addTestMemberBtn = findViewById(R.id.addTestMemberBtn)
        recordSavingsBtn = findViewById(R.id.recordSavingsBtn)
        addLoanBtn = findViewById(R.id.addLoanBtn)
        repaymentBtn = findViewById(R.id.repaymentBtn)
        viewMembersBtn = findViewById(R.id.viewMembersBtn)
        exportReportBtn = findViewById(R.id.exportReportBtn)
        chatbotBtn = findViewById(R.id.chatbotBtn)

        addTestMemberBtn.visibility = android.view.View.GONE
        recordSavingsBtn.visibility = android.view.View.GONE
        addLoanBtn.visibility = android.view.View.GONE
        repaymentBtn.visibility = android.view.View.GONE
        viewMembersBtn.visibility = android.view.View.GONE
        exportReportBtn.visibility = android.view.View.GONE

        applySavedTheme()

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val menuBtn = findViewById<TextView>(R.id.menuBtn)

        menuBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        findViewById<LinearLayout>(R.id.menuDashboard).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        findViewById<LinearLayout>(R.id.menuMembers).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, MembersActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.menuSavings).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, SavingsActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.menuLoan).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, LoanTrackerActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.menuRepayment).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, RepaymentActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.menuExport).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, ReportActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.menuSettings).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.menuLogout).setOnClickListener {

            drawerLayout.closeDrawer(GravityCompat.START)

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            ToastHelper.show(this, "Logged out successfully")
        }

        observeDashboardData()

        dashboardAddMemberBtn.setOnClickListener {
            startActivity(Intent(this, AddMemberActivity::class.java))
        }

        dashboardSearchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateDashboardMembersList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        dashboardMembersList.setOnItemClickListener { _, _, position, _ ->
            val filteredMembers = getFilteredMembers(dashboardSearchEt.text.toString())
            if (filteredMembers.isNotEmpty()) {
                val member = filteredMembers[position]
                val intent = Intent(this, SavingsHistoryActivity::class.java)
                intent.putExtra("memberId", member.id)
                startActivity(intent)
            }
        }

        addTestMemberBtn.setOnClickListener {
            startActivity(Intent(this, AddMemberActivity::class.java))
        }

        recordSavingsBtn.setOnClickListener {
            startActivity(Intent(this, SavingsActivity::class.java))
        }

        addLoanBtn.setOnClickListener {
            startActivity(Intent(this, LoanTrackerActivity::class.java))
        }

        repaymentBtn.setOnClickListener {
            startActivity(Intent(this, RepaymentActivity::class.java))
        }

        viewMembersBtn.setOnClickListener {
            startActivity(Intent(this, MembersActivity::class.java))
        }

        exportReportBtn.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }

        chatbotBtn.setOnClickListener {
            startActivity(Intent(this, ChatbotActivity::class.java))
        }
    }

    private fun applySavedTheme() {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val theme = prefs.getString("theme", "Classic Green & Purple")

        val primaryColor = when (theme) {
            "Purple", "Purple Accent" -> "#7B5E8E"
            "Green", "Green Only" -> "#2D7454"
            else -> "#2D7454"
        }

        val accentColor = when (theme) {
            "Purple", "Purple Accent" -> "#7B5E8E"
            "Green", "Green Only" -> "#2D7454"
            else -> "#6D5A7A"
        }

        val smallBoxColor = when (theme) {
            "Purple", "Purple Accent" -> "#8B7399"
            else -> "#3D8564"
        }

        findViewById<TextView>(R.id.appTitleText)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.subTitleText)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<LinearLayout>(R.id.summaryCard)
            .setBackgroundColor(Color.parseColor(primaryColor))

        activeLoansText.setBackgroundColor(Color.parseColor(smallBoxColor))
        disbursedText.setBackgroundColor(Color.parseColor(smallBoxColor))
        pendingText.setBackgroundColor(Color.parseColor(smallBoxColor))

        addTestMemberBtn.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        recordSavingsBtn.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        addLoanBtn.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        repaymentBtn.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        viewMembersBtn.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        dashboardAddMemberBtn.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        exportReportBtn.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#D4AF37"))

        applyDrawerTheme(primaryColor, accentColor)
    }

    private fun applyDrawerTheme(primaryColor: String, accentColor: String) {
        val dashboard = findViewById<LinearLayout>(R.id.menuDashboard)
        dashboard.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        setDrawerItemColor(R.id.menuMembers, accentColor)
        setDrawerItemColor(R.id.menuSavings, accentColor)
        setDrawerItemColor(R.id.menuLoan, accentColor)
        setDrawerItemColor(R.id.menuRepayment, accentColor)
        setDrawerItemColor(R.id.menuExport, accentColor)
        setDrawerItemColor(R.id.menuSettings, accentColor)

        findViewById<TextView>(R.id.drawerGroupNameText)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.drawerSubText)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.profileCircle)
            .backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))
    }

    private fun setDrawerItemColor(layoutId: Int, color: String) {
        val layout = findViewById<LinearLayout>(layoutId)

        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)

            if (child is TextView) {
                child.setTextColor(Color.parseColor(color))
            }

            if (child is ImageView) {
                child.setColorFilter(Color.parseColor(color))
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val savedGroupName = prefs.getString("group_name", "Mahila Shakti") ?: "Mahila Shakti"

        findViewById<TextView>(R.id.appTitleText).text = savedGroupName

        applySavedTheme()
        updateDashboardMembersList(dashboardSearchEt.text.toString())
    }

    private fun getFilteredMembers(query: String): List<Member> {
        return if (query.isBlank()) {
            dashboardMembers
        } else {
            dashboardMembers.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.phone.contains(query, ignoreCase = true) ||
                        it.uniqueId.contains(query, ignoreCase = true)
            }
        }
    }

    private fun updateDashboardMembersList(query: String = "") {
        val filteredMembers = getFilteredMembers(query)

        val adapter = object : ArrayAdapter<Member>(
            this,
            R.layout.item_dashboard_member,
            filteredMembers
        ) {
            override fun getView(
                position: Int,
                convertView: android.view.View?,
                parent: android.view.ViewGroup
            ): android.view.View {

                val view = convertView ?: layoutInflater.inflate(
                    R.layout.item_dashboard_member,
                    parent,
                    false
                )

                val member = filteredMembers[position]

                val initialText = view.findViewById<TextView>(R.id.memberInitialText)
                val nameText = view.findViewById<TextView>(R.id.memberNameText)
                val phoneText = view.findViewById<TextView>(R.id.memberPhoneText)
                val statusText = view.findViewById<TextView>(R.id.memberStatusText)

                initialText.text = member.name.firstOrNull()?.uppercase() ?: "?"
                nameText.text = member.name
                phoneText.text = "Phone: ${member.phone}"

                val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
                val selectedTheme = prefs.getString("theme", "Default Green & Purple")

                val primaryColor =
                    if (selectedTheme == "Purple" || selectedTheme == "Purple Accent")
                        "#7B5E8E"
                    else
                        "#2D7454"

                val lightAccent =
                    if (selectedTheme == "Purple" || selectedTheme == "Purple Accent")
                        "#EFE6F5"
                    else
                        "#EAF2EE"

                initialText.setTextColor(Color.parseColor(primaryColor))
                nameText.setTextColor(Color.parseColor(primaryColor))
                initialText.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(lightAccent))

                viewModel.getLatestSavingsForMember(member.id)
                    .observe(this@MainActivity) { latestSaving ->

                        if (
                            latestSaving != null &&
                            latestSaving.status.equals("Paid", ignoreCase = true)
                        ) {
                            statusText.text = "PAID"
                            statusText.setTextColor(Color.parseColor(primaryColor))
                            statusText.setBackgroundColor(Color.parseColor(lightAccent))
                        } else {
                            statusText.text = "PENDING"
                            statusText.setTextColor(Color.parseColor("#D66A00"))
                            statusText.setBackgroundColor(Color.parseColor("#FFF2D9"))
                        }
                    }

                return view
            }
        }

        dashboardMembersList.adapter = adapter
    }

    private fun observeDashboardData() {
        viewModel.totalGroupCapital.observe(this) { total ->
            totalCapitalText.text =
                "Total Group Capital: ${formatCurrency(total)}"
        }

        viewModel.activeLoanCount.observe(this) { count ->
            activeLoansText.text = "Active Loans: $count"
        }

        viewModel.totalDisbursed.observe(this) { total ->
            disbursedText.text =
                "Total Disbursed: ${formatCurrency(total)}"
        }

        viewModel.pendingContributions.observe(this) { count ->
            pendingText.text = "Pending Contributions: $count"
        }

        viewModel.members.observe(this) { members ->
            dashboardMembers = members
            currentMemberCount = members.size
            memberCountText.text = "Weekly Ledger"

            updateDashboardMembersList(
                dashboardSearchEt.text.toString()
            )
        }
    }
}