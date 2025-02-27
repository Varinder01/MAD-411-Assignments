package com.example.myapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)


        val expenseName = intent.getStringExtra("EXPENSE_NAME")
        val expenseAmount = intent.getStringExtra("EXPENSE_AMOUNT")
        val expenseDate = intent.getStringExtra("EXPENSE_DATE")


        val nameTextView: TextView = findViewById(R.id.expense_name_details)
        val amountTextView: TextView = findViewById(R.id.expense_amount_details)
        val dateTextView: TextView = findViewById(R.id.expense_date_details)

        nameTextView.text = expenseName
        amountTextView.text = expenseAmount
        dateTextView.text = expenseDate
    }
}
