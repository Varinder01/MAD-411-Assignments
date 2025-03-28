package com.example.myapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExpenseListFragment : Fragment(R.layout.expense_list_fragment) {

    private lateinit var expenseNameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var addExpenseButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var financialTipsButton: Button

    private val expensesList = mutableListOf<Expense>()

    private lateinit var footerFragment: FooterFragment
    private lateinit var headerFragment: HeaderFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseNameEditText = view.findViewById(R.id.expense_name)
        amountEditText = view.findViewById(R.id.amount)
        addExpenseButton = view.findViewById(R.id.add_expense_button)
        recyclerView = view.findViewById(R.id.recyclerview_expenses)
        financialTipsButton = view.findViewById(R.id.financial_tips_button)

        recyclerView.layoutManager = LinearLayoutManager(context) // Use context here
        expenseAdapter = ExpenseAdapter(expensesList)
        recyclerView.adapter = expenseAdapter

        // Initialize Fragments
        footerFragment = FooterFragment()
        headerFragment = HeaderFragment()

        // Get FragmentManager
        val fragmentManager: FragmentManager = childFragmentManager

        // Add Footer Fragment
        fragmentManager.beginTransaction()
            .replace(R.id.footer_container, footerFragment)
            .commit()

        // Add Header Fragment
        fragmentManager.beginTransaction()
            .replace(R.id.header_container, headerFragment)
            .commit()

        // Load expenses from file
        expensesList.addAll(expenseAdapter.expensesFromFile(requireContext()))
        expenseAdapter.notifyDataSetChanged()

        addExpenseButton.setOnClickListener {
            addExpense()
        }

        financialTipsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easyfinancial.com"))
            startActivity(intent)


        }
    }

    private fun calcSum(): Double{
        return expensesList.sumByDouble { it.amount.toDouble() }
    }

    private fun addExpense() {
        val name = expenseNameEditText.text.toString()
        val amount = amountEditText.text.toString()

        if (name.isNotEmpty() && amount.isNotEmpty()) {
            try {
                val expenseAmount = amount.toDouble()
                val newExpense = Expense(name, amount, "2025-03-27")
                expensesList.add(newExpense)
                expenseAdapter.notifyItemInserted(expensesList.size - 1)

                footerFragment.addExpense(expenseAmount)
                expenseAdapter.expensesToFile(requireContext(), expensesList)

                // Clear input fields after adding expense
                expenseNameEditText.text.clear()
                amountEditText.text.clear()

            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please enter both name and amount", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()

        footerFragment.addExpense(calcSum())
    }
}