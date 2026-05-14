package com.example.mahilashaktiunnati

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mahilashaktiunnati.viewmodel.MainViewModel

class AddMemberActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var selectedImageUri: Uri? = null

    private val PICK_IMAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)

        applySavedTheme()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val backBtn = findViewById<Button>(R.id.backBtn)
        val nameEt = findViewById<EditText>(R.id.nameEt)
        val idEt = findViewById<EditText>(R.id.idEt)
        val phoneEt = findViewById<EditText>(R.id.phoneEt)
        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val photoBox = findViewById<LinearLayout>(R.id.photoBox)

        backBtn.setOnClickListener { finish() }

        photoBox.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }

        saveBtn.setOnClickListener {

            val name = nameEt.text.toString().trim()
            val uniqueId = idEt.text.toString().trim()
            val phone = phoneEt.text.toString().trim()

            if (name.isEmpty() || uniqueId.isEmpty() || phone.isEmpty()) {
                ToastHelper.show(this, "Fill all fields")
                return@setOnClickListener
            }

            viewModel.addMember(
                name,
                uniqueId,
                phone,
                selectedImageUri?.toString() ?: ""
            )

            ToastHelper.show(this, "Member Added")
            finish()
        }
    }

    private fun applySavedTheme() {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val selectedTheme = prefs.getString("theme", "Default Green & Purple")

        val isPurple = selectedTheme == "Purple" || selectedTheme == "Purple Accent"

        val primaryColor =
            if (isPurple) "#7B5E8E" else "#2D7454"

        findViewById<Button>(R.id.backBtn)
            .setTextColor(android.graphics.Color.parseColor(primaryColor))

        findViewById<TextView>(R.id.titleText)
            .setTextColor(android.graphics.Color.parseColor(primaryColor))

        findViewById<Button>(R.id.saveBtn)
            .backgroundTintList =
            android.content.res.ColorStateList.valueOf(
                android.graphics.Color.parseColor(primaryColor)
            )
    }

    override fun onResume() {
        super.onResume()
        applySavedTheme()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data

            val photoBox = findViewById<LinearLayout>(R.id.photoBox)
            photoBox.removeAllViews()

            val imageView = ImageView(this)
            imageView.layoutParams = LinearLayout.LayoutParams(150, 150)
            imageView.setImageURI(selectedImageUri)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP

            photoBox.addView(imageView)
        }
    }
}