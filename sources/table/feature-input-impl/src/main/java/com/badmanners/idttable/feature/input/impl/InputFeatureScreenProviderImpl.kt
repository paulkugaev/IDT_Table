package com.badmanners.idttable.feature.input.impl

import com.badmanners.idttable.feature.input.api.InputFeatureScreenProvider
import com.badmanners.idttable.feature.input.impl.ui.InputScreen
import com.github.terrakok.modo.Screen
import javax.inject.Inject

class InputFeatureScreenProviderImpl @Inject constructor() : InputFeatureScreenProvider {

    override fun inputScreen(): Screen = InputScreen()
}
