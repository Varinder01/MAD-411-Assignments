package com.example.myapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class FooterFragment : Fragment(R.layout.fragment_footer) {
    private lateinit var totalTextView: TextView
    private var totalAmount: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalTextView = view.findViewById(R.id.total_text_view)
        updateTotal()
    }


    fun updateTotal() {
        Log.d("UpdateTotal", totalTextView.text.toString())
        totalTextView.text = "Total Expenses: $${totalAmount}"
    }


    fun addExpense(expenseAmount: Double) {
        totalAmount += expenseAmount
        updateTotal()
    }
    fun deleteExpense(expenseAmount: Double) {
        totalAmount -= expenseAmount
        updateTotal()
    }
}
