package com.badmanners.idttable.feature.table.api

import com.github.terrakok.modo.Screen

interface TableFeatureScreenProvider {

    fun tableScreen(rows: Int, columns: Int): Screen
}
