package com.example.mahilashaktiunnati

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ChatbotActivity : AppCompatActivity() {

    private lateinit var chatText: TextView
    private lateinit var messageEt: EditText
    private lateinit var sendBtn: Button
    private lateinit var assistantHeader: TextView
    private lateinit var assistantSubHeader: TextView
    private lateinit var backBtn: TextView
    private lateinit var titleText: TextView

    private var primaryColor = "#2D7454"
    private var accentColor = "#7B5E8E"
    private var lightAccent = "#EAF2EE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        chatText = findViewById(R.id.chatText)
        messageEt = findViewById(R.id.messageEt)
        sendBtn = findViewById(R.id.sendBtn)
        assistantHeader = findViewById(R.id.assistantHeader)
        assistantSubHeader = findViewById(R.id.assistantSubHeader)
        backBtn = findViewById(R.id.backBtn)
        titleText = findViewById(R.id.titleText)

        applySavedTheme()

        backBtn.setOnClickListener {
            finish()
        }

        chatText.text =
            "Unnati Assistant: Namaste! I am Unnati Assistant.\nHow can I help your SHG group today?"

        sendBtn.setOnClickListener {
            val userMessage = messageEt.text.toString().trim()

            if (userMessage.isEmpty()) {
                messageEt.error = "Type a message"
                return@setOnClickListener
            }

            val botReply = getBotReply(userMessage)

            chatText.append("\n\nYou: $userMessage")
            chatText.append("\n\nUnnati Assistant: $botReply")

            messageEt.text.clear()
        }
    }

    private fun applySavedTheme() {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val theme = prefs.getString("theme", "Classic Green & Purple")

        primaryColor =
            if (theme == "Purple" || theme == "Purple Accent")
                "#7B5E8E"
            else
                "#2D7454"

        accentColor =
            if (theme == "Green" || theme == "Green Only")
                "#2D7454"
            else
                "#7B5E8E"

        lightAccent =
            if (theme == "Purple" || theme == "Purple Accent")
                "#F0EAF5"
            else
                "#EAF2EE"

        backBtn.setTextColor(Color.parseColor(primaryColor))
        titleText.setTextColor(Color.parseColor(accentColor))

        assistantHeader.setTextColor(Color.WHITE)
        assistantSubHeader.setTextColor(Color.WHITE)

        findViewById<android.widget.LinearLayout>(R.id.headerCard)
            .backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        sendBtn.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(primaryColor))

        chatText.setTextColor(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.botIcon)
            .backgroundTintList =
            ColorStateList.valueOf(Color.parseColor("#D4AF37"))
    }

    private fun getBotReply(message: String): String {
        val msg = message.lowercase()

        return when {
            msg.contains("saving") || msg.contains("savings") ->
                "Savings are weekly contributions made by SHG members. They increase the total group capital."

            msg.contains("loan") && msg.contains("eligible") ->
                "Loan eligibility is calculated based on the member's paid savings. Higher savings can increase loan eligibility."

            msg.contains("loan") ->
                "Loans can be issued to members based on eligibility. The app tracks principal, interest, total repayable amount, and repayment status."

            msg.contains("repayment") || msg.contains("pay") ->
                "Repayment means paying back the borrowed loan amount. The app updates paid amount and outstanding balance automatically."

            msg.contains("report") ->
                "The report shows total members, gross savings, active loans, disbursed amount, and pending contributions."

            msg.contains("member") ->
                "Members are SHG participants. You can add members, view them, and check their contribution history."

            msg.contains("pending") ->
                "Pending contributions are savings entries that are not yet marked as paid."

            msg.contains("capital") ->
                "Total group capital is calculated from the total paid savings collected from members."

            msg.contains("hello") || msg.contains("hi") || msg.contains("namaste") ->
                "Hello! Ask me about savings, loans, repayments, members, reports, or group capital."

            else ->
                "I can help with SHG savings, loans, repayments, members, reports, and capital tracking. Try asking: What is loan eligibility?"
        }
    }

    override fun onResume() {
        super.onResume()
        applySavedTheme()
    }
}