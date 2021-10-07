package com.jaaliska.exchangerates.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jaaliska.exchangerates.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreen : Fragment(R.layout.main_fragment) {

    private val viewModel by viewModel<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init()
    }

}