package com.badmanners.idttable.feature.input.impl.presentation.news

import com.badmanners.idttable.feature.table.api.TableFeatureScreenProvider
import com.github.terrakok.modo.Screen

/**
 * One-shot effects emitted by the input store. Navigation is described by the target [Screen],
 * which the input feature builds through the [TableFeatureScreenProvider] contract.
 */
sealed interface InputNews {

    data class NavigateToTable(val screen: Screen) : InputNews
}
