package com.example.mahilashaktiunnati

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mahilashaktiunnati.data.model.SavingsWithMember

class SavingsHistoryAdapter(
    private var list: List<SavingsWithMember>
) : RecyclerView.Adapter<SavingsHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val initial: TextView = view.findViewById(R.id.initialTv)
        val name: TextView = view.findViewById(R.id.nameTv)
        val detail: TextView = view.findViewById(R.id.detailTv)
        val status: TextView = view.findViewById(R.id.statusTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_savings_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        val prefs = holder.itemView.context.getSharedPreferences(
            "app_settings",
            android.content.Context.MODE_PRIVATE
        )

        val selectedTheme = prefs.getString("theme", "Default Green & Purple")

        val isPurple = selectedTheme == "Purple" || selectedTheme == "Purple Accent"

        val primaryColor = if (isPurple) "#7B5E8E" else "#2D7454"
        val lightAccent = if (isPurple) "#EFE6F5" else "#EAF2EE"

        holder.initial.text = item.memberName.firstOrNull()?.uppercase() ?: "?"
        holder.name.text = item.memberName
        holder.detail.text = "₹${item.amount} • ${item.weekDate}"
        holder.status.text = item.status.uppercase()

        holder.initial.setTextColor(Color.parseColor(primaryColor))
        holder.initial.backgroundTintList =
            android.content.res.ColorStateList.valueOf(Color.parseColor(lightAccent))

        holder.name.setTextColor(Color.parseColor(primaryColor))

        if (item.status.equals("Paid", ignoreCase = true)) {
            holder.status.setTextColor(Color.parseColor(primaryColor))
        } else {
            holder.status.setTextColor(Color.parseColor("#D66A00"))
        }
    }

    fun updateList(newList: List<SavingsWithMember>) {
        list = newList
        notifyDataSetChanged()
    }
}