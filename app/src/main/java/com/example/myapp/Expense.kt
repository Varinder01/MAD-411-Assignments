package com.example.myapp

import android.icu.util.Currency

data class Expense(
    val name: String,
    val amount: String,
    val date: String,
    val currency: Currency,
    val convertedCost: Double

)
