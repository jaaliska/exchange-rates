package com.jaaliska.exchangerates.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
        setupActionBar()
        setupSmoothBottomMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_currencies -> {
                CurrencyChoiceDialog().show(supportFragmentManager, CURRENCY_CHOICE_DIALOG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupNavController() {
        navController = findNavController(R.id.navFragment)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarTitle.setText(R.string.app_name)
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_navigation_menu)
        val menu = popupMenu.menu
        bottomNavigation.setupWithNavController(menu, navController)
    }

    companion object {
        const val CURRENCY_CHOICE_DIALOG = "CurrencyChoiceDialog"
    }

}