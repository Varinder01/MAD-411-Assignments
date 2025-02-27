package com.example.myapp

//Name-VarinderPalSingh
//studentnumber-0834091
import android.os.Bundle
import android.view.View
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

    private val expensesList = mutableListOf<Expense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        expenseNameEditText = findViewById(R.id.expense_name)
        amountEditText = findViewById(R.id.amount)
        addExpenseButton = findViewById(R.id.add_expense_button)
        recyclerView = findViewById(R.id.recyclerview_expenses)


        recyclerView.layoutManager = LinearLayoutManager(this)
        expenseAdapter = ExpenseAdapter(expensesList)
        recyclerView.adapter = expenseAdapter

        addExpenseButton.setOnClickListener {
            addExpense()
        }
    }

    private fun addExpense() {
        val name = expenseNameEditText.text.toString()
        val amount = amountEditText.text.toString()

        if (name.isNotEmpty() && amount.isNotEmpty()) {
            val newExpense = Expense(name, amount)
            expensesList.add(newExpense)


            expenseAdapter.notifyItemInserted(expensesList.size - 1)


    }
    }
}

