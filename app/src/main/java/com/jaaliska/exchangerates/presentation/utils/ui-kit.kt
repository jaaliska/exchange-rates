package com.jaaliska.exchangerates.presentation.utils

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

fun setBlinkingAnimation(view: View) {
    val anim: Animation = AlphaAnimation(0.6f, 1.0f)
    anim.duration = 500
    anim.repeatMode = Animation.REVERSE
    anim.repeatCount = Animation.INFINITE
    view.startAnimation(anim)
}