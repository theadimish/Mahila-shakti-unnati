package com.example.mahilashaktiunnati.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: Double?): String {
    val value = amount ?: 0.0
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

fun formatDate(date: String): String {
    return try {
        val input = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val output = java.text.SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        output.format(input.parse(date)!!)
    } catch (e: Exception) {
        date
    }
}