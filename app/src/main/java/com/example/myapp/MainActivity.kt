package com.example.myapp

//Name-VarinderPalSingh
//studentnumber-0834091
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("ActivityLifecycle", "onCreate called")

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
    override fun onStart() {
        super.onStart()
        Log.d("ActivityLifecycle", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ActivityLifecycle", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ActivityLifecycle", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ActivityLifecycle", "onDestroy called")

        val financialTipsButton: Button = findViewById(R.id.financial_tips_button)

        financialTipsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.softwareadvice.com/accounting/expense-management-comparison/price-quotes-and-free-recommendations/?utm_source=bing-search&utm_medium=ppc&utm_term=expense%20tracking%20software&utm_matchtype=p&network=o&adpos=&ad=&sitelink=&location=125317&gclsrc=3p.ds&&targetid=kwd-73873771533545:loc-32&campaign=361242290&adgroup=1181975872125945&utm_campaign=:1:SA:2:COM:3:ENG:4:US:5:BAU:6:SOF:7:Desktop:8:PH:9:Expense_Report:13:Acct&msclkid=b7d03fc4ec9b113217885818d8b9787c&utm_content=Expense%20Management%20-%20Software%20-%20Online"))
            startActivity(intent)
        }

    } }




