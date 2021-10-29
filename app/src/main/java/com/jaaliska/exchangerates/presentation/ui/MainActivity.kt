package com.jaaliska.exchangerates.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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