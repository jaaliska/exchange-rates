package com.jaaliska.exchangerates.presentation.ui

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.screens.currencyChoice.CurrencyChoiceDialog

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.change_currencies -> {
                CurrencyChoiceDialog().show(supportFragmentManager, CURRENCY_CHOICE_DIALOG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val CURRENCY_CHOICE_DIALOG = "CurrencyChoiceDialog"
    }
}