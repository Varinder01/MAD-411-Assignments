package com.example.myapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        val expenseNameText: TextView = findViewById(R.id.expense_name_text)
        val amountText: TextView = findViewById(R.id.amount_text)
        val dateText: TextView = findViewById(R.id.date_text)

        val expenseName = intent.getStringExtra("expense_name")
        val amount = intent.getStringExtra("amount")
        val date = intent.getStringExtra("date")

        expenseNameText.text = expenseName
        amountText.text = "$$amount"
        dateText.text = date
    }
   }

