package com.badmanners.common_ui.ui

import androidx.annotation.StringRes

fun interface StringProvider {

    fun get(@StringRes resId: Int): String
}