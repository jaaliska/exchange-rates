package com.jaaliska.exchangerates.app.di

import org.koin.core.qualifier.StringQualifier

val REMOTE = StringQualifier("remote")
val LOCAL = StringQualifier("local")