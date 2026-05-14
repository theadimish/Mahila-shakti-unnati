package com.example.mahilashaktiunnati

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

object ToastHelper {

    fun show(context: Context, message: String) {

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.HORIZONTAL
        layout.gravity = Gravity.CENTER_VERTICAL
        layout.setPadding(30, 18, 30, 18)

        val background = GradientDrawable()
        background.setColor(Color.parseColor("#5B2C6F"))
        background.cornerRadius = 40f
        layout.background = background

        val logo = ImageView(context)
        logo.setImageResource(R.mipmap.ic_launcher)

        val logoParams = LinearLayout.LayoutParams(42, 42)
        logoParams.setMargins(0, 0, 16, 0)
        layout.addView(logo, logoParams)

        val textView = TextView(context)
        textView.text = message
        textView.setTextColor(Color.WHITE)
        textView.textSize = 15f
        textView.gravity = Gravity.CENTER_VERTICAL

        layout.addView(textView)

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 140)
        toast.show()
    }
}