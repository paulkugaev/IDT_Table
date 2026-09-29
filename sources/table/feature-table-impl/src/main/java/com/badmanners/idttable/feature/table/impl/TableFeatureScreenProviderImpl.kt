package com.badmanners.idttable.feature.table.impl

import com.badmanners.idttable.feature.table.api.TableFeatureScreenProvider
import com.github.terrakok.modo.Screen
import javax.inject.Inject

class TableFeatureScreenProviderImpl @Inject constructor() : TableFeatureScreenProvider {

    override fun tableScreen(rows: Int, columns: Int): Screen = TableScreen(rows, columns)
}