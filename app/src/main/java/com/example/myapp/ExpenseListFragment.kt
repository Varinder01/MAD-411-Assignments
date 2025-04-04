package com.example.myapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ExpenseListFragment : Fragment(R.layout.expense_list_fragment) {

    private lateinit var expenseNameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var addExpenseButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var financialTipsButton: Button

    // New UI for currency
    private lateinit var conversionNeededCheckBox: CheckBox
    private lateinit var currencySpinner: Spinner
    private lateinit var convertedCostTextView: TextView

    private val expensesList = mutableListOf<Expense>()

    private lateinit var footerFragment: FooterFragment
    private lateinit var headerFragment: HeaderFragment

    private val cadRatesMap = mutableMapOf<String, Double>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseNameEditText = view.findViewById(R.id.expense_name)
        amountEditText = view.findViewById(R.id.amount)
        addExpenseButton = view.findViewById(R.id.add_expense_button)
        recyclerView = view.findViewById(R.id.recyclerview_expenses)
        financialTipsButton = view.findViewById(R.id.financial_tips_button)

        conversionNeededCheckBox = view.findViewById(R.id.conversion_needed_checkbox)
        currencySpinner = view.findViewById(R.id.currency_spinner)
        convertedCostTextView = view.findViewById(R.id.converted_cost_textview)

        recyclerView.layoutManager = LinearLayoutManager(context)
        expenseAdapter = ExpenseAdapter(expensesList)
        recyclerView.adapter = expenseAdapter


        footerFragment = FooterFragment()
        headerFragment = HeaderFragment()

        val fragmentManager: FragmentManager = childFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.footer_container, footerFragment)
            .commit()

        // Add Header Fragment
        fragmentManager.beginTransaction()
            .replace(R.id.header_container, headerFragment)
            .commit()

        expensesList.addAll(expenseAdapter.expensesFromFile(requireContext()))
        expenseAdapter.notifyDataSetChanged()

        fetchCadRatesAndSetupSpinner()

        addExpenseButton.setOnClickListener {
            addExpense()
        }

        financialTipsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easyfinancial.com"))
            startActivity(intent)
        }
    }

    private fun fetchCadRatesAndSetupSpinner() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getExchangeRates("cad")
                if (response.isSuccessful) {
                    response.body()?.let { jsonObj ->

                        val date = jsonObj["date"].asString
                        val cadJson = jsonObj.getAsJsonObject("cad")


                        for ((key, value) in cadJson.entrySet()) {
                            cadRatesMap[key] = value.asDouble
                        }

                        cadRatesMap["CAD"] = 1.0


                        val currencyList = cadRatesMap.keys.sorted()
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            currencyList.toList()
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        currencySpinner.adapter = adapter


                        val defaultIndex = currencyList.indexOf("CAD")
                        if (defaultIndex >= 0) {
                            currencySpinner.setSelection(defaultIndex)
                        }

                        setupConversionWatcher()
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch CAD rates", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error fetching currency rates", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupConversionWatcher() {

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                updateConvertedCostPreview()
            }
        }


        amountEditText.addTextChangedListener {
            updateConvertedCostPreview()
        }


        conversionNeededCheckBox.setOnCheckedChangeListener { _, _ ->
            updateConvertedCostPreview()
        }
    }

    private fun updateConvertedCostPreview() {
        val amountStr = amountEditText.text.toString()
        if (amountStr.isEmpty()) {
            convertedCostTextView.text = "Converted Cost: 0.0"
            return
        }
        val amountVal = amountStr.toDoubleOrNull() ?: 0.0

        val selectedCurrency = currencySpinner.selectedItem?.toString() ?: "CAD"
        val conversionNeeded = conversionNeededCheckBox.isChecked


        val finalAmount = if (conversionNeeded) {
            cadRatesMap[selectedCurrency]?.let { rate ->
                amountVal * rate
            } ?: amountVal
        } else {
            amountVal
        }

        convertedCostTextView.text = "Converted Cost: $finalAmount"
    }

    private fun addExpense() {
        val name = expenseNameEditText.text.toString()
        val amountString = amountEditText.text.toString()

        if (name.isNotEmpty() && amountString.isNotEmpty()) {
            try {
                val originalAmountCad = amountString.toDouble() // user input is CAD
                val isConversionNeeded = conversionNeededCheckBox.isChecked
                val selectedCurrency = currencySpinner.selectedItem?.toString() ?: "CAD"

                val rate = cadRatesMap[selectedCurrency] ?: 1.0
                val convertedCost = if (isConversionNeeded) {
                    originalAmountCad * rate
                } else {
                    // no conversion
                    originalAmountCad
                }

                // Build the new Expense
                val newExpense = Expense(
                    name = name,
                    amount = originalAmountCad,
                    date = "2025-03-27",
                    currency = selectedCurrency,
                    convertedCost = convertedCost
                )

                expensesList.add(newExpense)
                expenseAdapter.notifyItemInserted(expensesList.size - 1)


                footerFragment.addExpense(originalAmountCad)


                expenseAdapter.expensesToFile(requireContext(), expensesList)


                expenseNameEditText.text.clear()
                amountEditText.text.clear()

            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please enter both name and amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calcSum(): Double {

        return expensesList.sumOf { it.amount }
    }

    override fun onStart() {
        super.onStart()
        footerFragment.addExpense(calcSum())
    }
}
