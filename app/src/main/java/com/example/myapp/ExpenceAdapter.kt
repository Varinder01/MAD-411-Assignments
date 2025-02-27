package com.example.myapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private val expenses: MutableList<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.expenseNameTextView.text = expense.name
        holder.amountTextView.text = expense.amount


        holder.showDetailsButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, ExpenseDetailsActivity::class.java).apply {
                putExtra("EXPENSE_NAME", expense.name)
                putExtra("EXPENSE_AMOUNT", expense.amount)
                putExtra("EXPENSE_DATE", expense.date)
            }
            holder.itemView.context.startActivity(intent)
        }


        holder.deleteButton.setOnClickListener {
            expenses.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expenseNameTextView: TextView = itemView.findViewById(R.id.expense_name_text)
        val amountTextView: TextView = itemView.findViewById(R.id.expense_amount_text)
        val showDetailsButton: Button = itemView.findViewById(R.id.expense_show_details_button)
        val deleteButton: Button = itemView.findViewById(R.id.expense_delete_button)
    }
}
