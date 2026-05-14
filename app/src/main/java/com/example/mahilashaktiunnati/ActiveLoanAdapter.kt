package com.example.mahilashaktiunnati

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mahilashaktiunnati.data.model.LoanWithMember

class ActiveLoanAdapter(
    private var loans: List<LoanWithMember>
) : RecyclerView.Adapter<ActiveLoanAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val initial: TextView = view.findViewById(R.id.loanInitialTv)
        val name: TextView = view.findViewById(R.id.loanNameTv)
        val date: TextView = view.findViewById(R.id.loanDateTv)
        val principal: TextView = view.findViewById(R.id.loanPrincipalTv)
        val interest: TextView = view.findViewById(R.id.loanInterestTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_active_loan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = loans.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loan = loans[position]

        val prefs = holder.itemView.context.getSharedPreferences(
            "app_settings",
            android.content.Context.MODE_PRIVATE
        )

        val theme = prefs.getString("theme", "Default Green & Purple")

        val isPurple = theme == "Purple" || theme == "Purple Accent"

        val primaryColor = if (isPurple) "#7B5E8E" else "#2D7454"

        holder.initial.text = loan.name.firstOrNull()?.uppercase() ?: "?"
        holder.name.text = loan.name
        holder.date.text = "ACTIVE SINCE ${loan.date}"

        holder.principal.text =
            if (loan.principal % 1 == 0.0)
                "₹${loan.principal.toInt()}"
            else
                "₹%.2f".format(loan.principal)

        holder.interest.text =
            if (loan.interest % 1 == 0.0)
                "₹${loan.interest.toInt()}"
            else
                "₹%.2f".format(loan.interest)

        holder.initial.setTextColor(Color.parseColor(primaryColor))
        holder.name.setTextColor(Color.parseColor(primaryColor))
        holder.interest.setTextColor(Color.parseColor(primaryColor))
    }

    fun updateList(newList: List<LoanWithMember>) {
        loans = newList
        notifyDataSetChanged()
    }
}