package com.example.tipsy

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tipsy.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil
import kotlin.math.cos

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener { calculateTip() }
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }
    }

    private fun calculateTip() {
        val cost = binding.costOfServiceEditText.text.toString().toDoubleOrNull()
        if (cost == null) {
            displayTip(0.0)
            displayTotal(0.0)
            return
        }
        val tipPercent = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_fifteen_percent -> 0.15
            R.id.option_eighteen_percent -> 0.18
            else -> 0.20
        }
        val tip = when (binding.roundUpSwitch.isChecked) {
            true -> ceil(cost * tipPercent)
            else -> cost * tipPercent
        }
        displayTip(tip)
        displayTotal(tip)
    }

    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipAmount.text = getString(R.string.tip_amount, formattedTip)
    }

    private fun displayTotal(tip: Double) {
        val cost = binding.costOfServiceEditText.text.toString().toDoubleOrNull() ?: 0.0
        val formattedTotal = NumberFormat.getCurrencyInstance().format(tip + cost)
        binding.totalAmount.text = getString(R.string.total_amount, formattedTotal)
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}