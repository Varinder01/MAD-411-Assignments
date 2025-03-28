package com.example.myapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var expenseNameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var addExpenseButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var financialTipsButton: Button

    private val expensesList = mutableListOf<Expense>()

    private lateinit var footerFragment: FooterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expenseNameEditText = findViewById(R.id.expense_name)
        amountEditText = findViewById(R.id.amount)
        addExpenseButton = findViewById(R.id.add_expense_button)
        recyclerView = findViewById(R.id.recyclerview_expenses)
        financialTipsButton = findViewById(R.id.financial_tips_button)

        recyclerView.layoutManager = LinearLayoutManager(this)
        expenseAdapter = ExpenseAdapter(expensesList)
        recyclerView.adapter = expenseAdapter


        footerFragment = FooterFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.footer_container, footerFragment)
            .commit()


        val headerFragment = HeaderFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.header_container, headerFragment)
            .commit()

        // code for getting expenses from file when view added
        expensesList.addAll(expenseAdapter.expensesFromFile(this))
        expenseAdapter.notifyDataSetChanged()

        addExpenseButton.setOnClickListener {
            addExpense()
        }

        financialTipsButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easyfinancial.com"))
            startActivity(intent)
        }
    }

    private fun addExpense() {
        val name = expenseNameEditText.text.toString()
        val amount = amountEditText.text.toString()

        if (name.isNotEmpty() && amount.isNotEmpty()) {
            val expenseAmount = amount.toDouble()
            val newExpense = Expense(name, amount, "2025-03-27")
            expensesList.add(newExpense)
            expenseAdapter.notifyItemInserted(expensesList.size - 1)


            footerFragment.addExpense(expenseAmount)
            // re updating the file when added
            expenseAdapter.expensesToFile(this, expensesList)
        } else {
            Toast.makeText(this, "Please enter both name and amount", Toast.LENGTH_SHORT).show()
        }
    }
}
