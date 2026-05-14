package com.example.mahilashaktiunnati

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mahilashaktiunnati.R
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    private lateinit var groupNameSubTv: TextView
    private lateinit var languageSubTv: TextView
    private lateinit var themeSubTv: TextView

    private var primaryColor = "#2D7454"
    private var accentColor = "#7B5E8E"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        prefs = getSharedPreferences("app_settings", MODE_PRIVATE)

        groupNameSubTv = findViewById(R.id.groupNameSubTv)
        languageSubTv = findViewById(R.id.languageSubTv)
        themeSubTv = findViewById(R.id.themeSubTv)

        findViewById<TextView>(R.id.backBtn).setOnClickListener {
            finish()
        }

        loadSettings()
        applySavedTheme()

        findViewById<LinearLayout>(R.id.profileSettingsRow).setOnClickListener {
            showProfileDialog()
        }

        findViewById<LinearLayout>(R.id.languageRow).setOnClickListener {
            showLanguageDialog()
        }

        findViewById<LinearLayout>(R.id.themeRow).setOnClickListener {
            showThemeDialog()
        }

        findViewById<LinearLayout>(R.id.logoutRow).setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setPositiveButton("Logout") { _, _ ->

                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)

                    ToastHelper.show(this, "Logged out successfully")
                }

                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun loadSettings() {
        val groupName = prefs.getString("group_name", "Mahila Shakti Unnati")
        val language = prefs.getString("language", "English (India)")
        val theme = prefs.getString("theme", "Purple")

        groupNameSubTv.text = groupName
        languageSubTv.text = language
        themeSubTv.text = theme
    }

    private fun applySavedTheme() {
        val theme = prefs.getString("theme", "Purple")

        primaryColor =
            if (theme == "Purple")
                "#7B5E8E"
            else
                "#2D7454"

        accentColor =
            if (theme == "Green")
                "#2D7454"
            else
                "#7B5E8E"

        findViewById<TextView>(R.id.backBtn)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.settingsTitleTv)
            .setTextColor(Color.parseColor(accentColor))

        findViewById<TextView>(R.id.appSettingsTitleTv)
            .setTextColor(Color.parseColor("#B2A8B8"))

        findViewById<ImageView>(R.id.profileIconIv)
            .setColorFilter(Color.parseColor(primaryColor))

        findViewById<ImageView>(R.id.languageIconIv)
            .setColorFilter(Color.parseColor(primaryColor))

        findViewById<ImageView>(R.id.themeIconIv)
            .setColorFilter(Color.parseColor(primaryColor))
    }

    private fun showProfileDialog() {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 30, 50, 20)

        val titleTv = TextView(this)
        titleTv.text = "Profile Settings"
        titleTv.setTextColor(Color.parseColor(accentColor))
        titleTv.textSize = 24f
        titleTv.setTypeface(null, Typeface.BOLD)
        layout.addView(titleTv)

        val groupEt = EditText(this)
        groupEt.hint = "Enter Group Name"
        groupEt.setText(prefs.getString("group_name", "Mahila Shakti Unnati"))
        groupEt.textSize = 16f
        groupEt.setPadding(18, 18, 18, 18)
        groupEt.setBackgroundResource(R.drawable.bg_input)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 22, 0, 0)

        layout.addView(groupEt, params)

        val dialog = AlertDialog.Builder(this)
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                prefs.edit()
                    .putString("group_name", groupEt.text.toString())
                    .apply()

                loadSettings()
                applySavedTheme()
                ToastHelper.show(this, "Profile saved")
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog)
        }

        dialog.show()
    }

    private fun showLanguageDialog() {

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 30, 50, 26)

        val titleTv = TextView(this)
        titleTv.text = "Language"
        titleTv.setTextColor(Color.parseColor(accentColor))
        titleTv.textSize = 24f
        titleTv.setTypeface(null, Typeface.BOLD)

        layout.addView(titleTv)

        val englishTv = TextView(this)
        englishTv.text = "English (India)"
        englishTv.textSize = 18f
        englishTv.setTypeface(null, Typeface.BOLD)
        englishTv.setTextColor(Color.parseColor(accentColor))
        englishTv.setPadding(0, 40, 0, 10)

        layout.addView(englishTv)

        val futureTv = TextView(this)
        futureTv.text = "More languages coming in future updates"
        futureTv.textSize = 14f
        futureTv.setTextColor(Color.parseColor("#888888"))
        futureTv.setPadding(0, 10, 0, 0)

        layout.addView(futureTv)

        val dialog = AlertDialog.Builder(this)
            .setView(layout)
            .create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog)
        }

        dialog.show()
    }

    private fun showThemeDialog() {
        val themes = arrayOf(
            "Green",
            "Purple"
        )

        val savedTheme = prefs.getString("theme", "Purple")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 30, 50, 26)

        val titleTv = TextView(this)
        titleTv.text = "Theme Selector"
        titleTv.setTextColor(Color.parseColor(accentColor))
        titleTv.textSize = 24f
        titleTv.setTypeface(null, Typeface.BOLD)
        layout.addView(titleTv)

        themes.forEach { themeName ->
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.gravity = android.view.Gravity.CENTER_VERTICAL
            row.setPadding(0, 30, 0, 6)

            val dot = TextView(this)
            dot.text = "●"
            dot.textSize = 22f
            dot.setTextColor(
                Color.parseColor(
                    when (themeName) {
                        "Green" -> "#2D7454"
                        "Purple" -> "#7B5E8E"
                        else -> "#D4AF37"
                    }
                )
            )

            val optionTv = TextView(this)
            optionTv.text = themeName
            optionTv.textSize = 18f
            optionTv.setTextColor(Color.parseColor("#222222"))
            optionTv.setPadding(18, 0, 0, 0)

            if (savedTheme == themeName) {
                optionTv.setTypeface(null, Typeface.BOLD)
                optionTv.setTextColor(Color.parseColor(accentColor))
            }

            row.addView(dot)
            row.addView(optionTv)
            layout.addView(row)
        }

        val dialog = AlertDialog.Builder(this)
            .setView(layout)
            .create()

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog)

            for (i in 1 until layout.childCount) {
                layout.getChildAt(i).setOnClickListener { view ->
                    val row = view as LinearLayout
                    val selectedTheme = (row.getChildAt(1) as TextView).text.toString()

                    prefs.edit().putString("theme", selectedTheme).apply()

                    loadSettings()
                    applySavedTheme()

                    ToastHelper.show(this, "Theme saved")
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        loadSettings()
        applySavedTheme()
    }
}