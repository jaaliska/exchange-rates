package com.jaaliska.exchangerates.presentation.ui.screens.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaaliska.exchangerates.R
import com.jaaliska.exchangerates.presentation.ui.common.list.item.ItemAdapter
import com.jaaliska.exchangerates.presentation.ui.common.text_formatter.MoneyValueFilter
import com.jaaliska.exchangerates.presentation.utils.observe
import kotlinx.android.synthetic.main.fragment_screen_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeScreen : Fragment(R.layout.fragment_screen_home) {

    private val viewModel by viewModel<HomeViewModel>()

    private val homeItemAdapter by lazy { ItemAdapter(onItemClick = viewModel::onItemSelected) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            setupUpdateDate()
            setupLoading()
            setupAnchor()
            setupError()
            setupList()

            swipeRefresh.setOnRefreshListener { onRefreshItems() }
        }
    }


    private fun BaseHomeViewModel.setupError() {
        error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, requireContext().getString(it), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun BaseHomeViewModel.setupUpdateDate() {
        updateDate.observe(viewLifecycleOwner) { date ->
            textUpdateDate.text = date?.let { getString(R.string.the_last_update_was_at, it) } ?: ""
        }
    }

    private fun BaseHomeViewModel.setupLoading() {
        isLoading.observe(viewLifecycleOwner) {
            swipeRefresh.isRefreshing = it
        }
    }

    private fun BaseHomeViewModel.setupAnchor() {
        anchor.observe(viewLifecycleOwner) {
            if (it != null) {
                textTitle.text = it.title
                textSubtitle.text = it.subtitle
            }
        }

        textAmount.filters = arrayOf(MoneyValueFilter())
        textAmount.doAfterTextChanged {
            val newAmount = it.toString().toDoubleOrNull()
            newAmount?.let(viewModel::onAmountChanged)
        }
    }

    private fun BaseHomeViewModel.setupList() = with(recyclerItems) {
        adapter = homeItemAdapter
        layoutManager = LinearLayoutManager(context)

        addItemDecoration(
            DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation)
        )

        items.observe(viewLifecycleOwner, homeItemAdapter::submitList)
    }
}