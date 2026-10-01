package com.badmanners.idttable.feature.input.impl.presentation.news

import com.github.terrakok.modo.Screen

sealed interface InputNews {

    data class NavigateToTable(val screen: Screen) : InputNews
}
