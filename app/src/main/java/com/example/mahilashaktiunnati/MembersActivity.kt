package com.example.mahilashaktiunnati

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mahilashaktiunnati.data.entity.Member
import com.example.mahilashaktiunnati.viewmodel.MainViewModel

class MembersActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var memberList: List<Member> = emptyList()
    private var filteredList: List<Member> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val membersListView = findViewById<ListView>(R.id.membersListView)
        val memberSearchEt = findViewById<EditText>(R.id.memberSearchEt)
        val addMemberSmallBtn = findViewById<TextView>(R.id.addMemberSmallBtn)

        applySavedTheme()

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }

        addMemberSmallBtn.setOnClickListener {
            startActivity(Intent(this, AddMemberActivity::class.java))
        }

        viewModel.members.observe(this) { members ->
            memberList = members
            updateList(membersListView, memberSearchEt.text.toString())
        }

        memberSearchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateList(membersListView, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        membersListView.setOnItemClickListener { _, _, position, _ ->
            if (filteredList.isNotEmpty()) {
                val member = filteredList[position]

                val intent = Intent(this, SavingsHistoryActivity::class.java)
                intent.putExtra("memberId", member.id)
                startActivity(intent)
            }
        }

        membersListView.setOnItemLongClickListener { _, _, position, _ ->
            if (filteredList.isNotEmpty()) {
                val member = filteredList[position]

                AlertDialog.Builder(this)
                    .setTitle("Delete Member")
                    .setMessage("Delete ${member.name}?")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.deleteMember(member)
                        ToastHelper.show(this, "Member deleted")
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            true
        }
    }

    private fun updateList(listView: ListView, query: String) {

        filteredList = if (query.isBlank()) {
            memberList
        } else {
            memberList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.phone.contains(query, ignoreCase = true) ||
                        it.uniqueId.contains(query, ignoreCase = true)
            }
        }

        val adapter = object : ArrayAdapter<Member>(
            this,
            R.layout.item_member_card,
            filteredList
        ) {
            override fun getView(
                position: Int,
                convertView: View?,
                parent: android.view.ViewGroup
            ): View {
                val view = convertView ?: layoutInflater.inflate(
                    R.layout.item_member_card,
                    parent,
                    false
                )

                val member = filteredList[position]

                val initialText = view.findViewById<TextView>(R.id.memberInitialText)
                val photoIv = view.findViewById<ImageView>(R.id.memberPhotoIv)
                val nameText = view.findViewById<TextView>(R.id.memberNameText)
                val phoneText = view.findViewById<TextView>(R.id.memberPhoneText)

                initialText.text = member.name.firstOrNull()?.uppercase() ?: "?"
                nameText.text = member.name
                phoneText.text = "Phone: ${member.phone}"

                if (member.photoUri.isNotEmpty()) {
                    photoIv.visibility = View.VISIBLE
                    initialText.visibility = View.GONE
                    photoIv.setImageURI(Uri.parse(member.photoUri))
                } else {
                    photoIv.visibility = View.GONE
                    initialText.visibility = View.VISIBLE
                }

                val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
                val theme = prefs.getString("theme", "Default Green & Purple")

                val isPurple = theme == "Purple" || theme == "Purple Accent"

                val accentColor = if (isPurple) "#7B5E8E" else "#2D7454"
                val lightAccent = if (isPurple) "#F0EAF5" else "#EAF2EE"

                initialText.setTextColor(Color.parseColor(accentColor))
                nameText.setTextColor(Color.parseColor(accentColor))
                initialText.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(lightAccent))

                return view
            }
        }

        listView.adapter = adapter
    }

    private fun applySavedTheme() {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val theme = prefs.getString("theme", "Default Green & Purple")

        val isPurple = theme == "Purple" || theme == "Purple Accent"

        val primaryColor =
            if (isPurple) "#7B5E8E" else "#2D7454"

        findViewById<TextView>(R.id.membersTitleTv)
            .setTextColor(Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.addMemberSmallBtn)
            .backgroundTintList = ColorStateList.valueOf(Color.parseColor(primaryColor))

        findViewById<Button>(R.id.backBtn)
            .setTextColor(Color.parseColor(primaryColor))
    }

    override fun onResume() {
        super.onResume()
        applySavedTheme()
    }
}