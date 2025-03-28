package com.example.myapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class ExpenseDetailsFragment : Fragment(R.layout.fragment_expense_details) {

    private lateinit var nameTextView: TextView
    private lateinit var amountTextView: TextView
    private lateinit var dateTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameTextView = view.findViewById(R.id.expense_name_details)
        amountTextView = view.findViewById(R.id.expense_amount_details)
        dateTextView = view.findViewById(R.id.expense_date_details)

        val expenseName = arguments?.getString("EXPENSE_NAME")
        val expenseAmount = arguments?.getString("EXPENSE_AMOUNT")
        val expenseDate = arguments?.getString("EXPENSE_DATE")

        nameTextView.text = expenseName
        amountTextView.text = expenseAmount
        dateTextView.text = expenseDate
    }
}
