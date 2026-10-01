package com.badmanners.idttable.feature.table.impl.provider

import com.badmanners.idttable.feature.table.api.TableFeatureScreenProvider
import com.badmanners.idttable.feature.table.impl.ui.compose.TableScreen
import com.github.terrakok.modo.Screen
import javax.inject.Inject

class TableFeatureScreenProviderImpl @Inject constructor() : TableFeatureScreenProvider {

    override fun tableScreen(rows: Int, columns: Int): Screen = TableScreen(rows, columns)
}
