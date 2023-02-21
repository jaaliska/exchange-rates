package com.jaaliska.exchangerates.presentation.utils.ui_kit

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.Flow

fun setBlinkingAnimation(view: View) {
    val anim: Animation = AlphaAnimation(0.6f, 1.0f)
    anim.duration = 500
    anim.repeatMode = Animation.REVERSE
    anim.repeatCount = Animation.INFINITE
    view.startAnimation(anim)
}

fun Context.showProgress(isLoading: Flow<Boolean>, lifecycleOwner: LifecycleOwner) {
    ShowProgressBinding(isLoading, lifecycleOwner, this)
}