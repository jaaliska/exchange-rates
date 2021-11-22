package com.jaaliska.exchangerates.tests

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.jaaliska.exchangerates.app.di.app
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.rules.TestRule
import org.koin.test.AutoCloseKoinTest
import org.koin.test.category.CheckModuleTest
import org.koin.test.check.checkKoinModules
import org.koin.test.mock.MockProviderRule
import org.mockito.Mockito

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@Category(CheckModuleTest::class)
class ModulesCheckTest : AutoCloseKoinTest() {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

//    @org.junit.Before
//    fun setup() {
//        Dispatchers.setMain(TestCoroutineDispatcher())
//    }

    @Test
    fun checkAllModules() = checkKoinModules(app) {
        withInstance<Context>()
        withInstance<Application>()
        withInstance<SavedStateHandle>()
    }
}