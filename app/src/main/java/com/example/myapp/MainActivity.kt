package com.example.myapp


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity




class MainActivity : AppCompatActivity() {

    private lateinit var nameText: EditText
    private lateinit var buttonshow: Button
    private lateinit var nameTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameText = findViewById(R.id.name)
        buttonshow = findViewById(R.id.button)
        nameTextView = findViewById(R.id.name_text_view)
    }

    fun ShowName(view: View) {
        val nameCalc = nameText.text.toString()
        nameTextView.text = "the name is: $nameCalc"
    }
}

