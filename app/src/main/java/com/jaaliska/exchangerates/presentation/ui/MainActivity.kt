package com.jaaliska.exchangerates.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.databinding.ActivityMainBinding
import com.jaaliska.exchangerates.presentation.ui.currencyChoice.CurrencyChoiceDialog

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarTitle.setText(R.string.app_name)
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_navigation_menu)
        val menu = popupMenu.menu
        binding.bottomNavigation.setupWithNavController(menu, navController)
    }

    companion object {
        const val CURRENCY_CHOICE_DIALOG = "CurrencyChoiceDialog"
    }

}