package com.example.myapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

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

            val bundle = Bundle().apply {
                putString("EXPENSE_NAME", expense.name)
                putString("EXPENSE_AMOUNT", expense.amount)
                putString("EXPENSE_DATE", expense.date)
            }
            holder.itemView.findNavController().navigate(R.id.action_expenseListFragment_to_expenseDetailsFragment, bundle)
        }




        holder.deleteButton.setOnClickListener {
            expenses.removeAt(position)
            notifyItemRemoved(position)

            //updated expense to file
            expensesToFile(holder.itemView.context, expenses)
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

// code to iterate with file
     fun expensesToFile(context: Context, expensesList: List<Expense>) {
        try {
            val json = Gson().toJson(expensesList)
            context.openFileOutput("expenses.json", Context.MODE_PRIVATE).use { output ->
                output.write(json.toByteArray())
            }
            Log.d("FileStorage", "Expenses saved successfully")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error saving expenses: ${e.message}")
        }
    }

     fun expensesFromFile(context: Context): MutableList<Expense> {
        val expensesList: MutableList<Expense> = mutableListOf()
        try {
            val file = File(context.filesDir, "expenses.json")
            if (!file.exists()) return expensesList

            val json = file.readText()
            val type = object : TypeToken<List<Expense>>() {}.type
            val loadedExpenses: List<Expense> = Gson().fromJson(json, type)
            expensesList.addAll(loadedExpenses)
            Log.d("FileStorage", "Expenses loaded successfully")
        } catch (e: FileNotFoundException) {
            Log.e("FileStorage", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error reading file: ${e.message}")
        }
        return expensesList
    }
}
