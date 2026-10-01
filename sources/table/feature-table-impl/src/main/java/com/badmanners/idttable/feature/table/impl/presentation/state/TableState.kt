package com.badmanners.idttable.feature.table.impl.presentation.state

import kotlinx.collections.immutable.ImmutableList

sealed interface TableState {

    data object Loading : TableState

    data class Content(
        val cells: ImmutableList<ImmutableList<String>>,
        val highlightedCell: Cell? = null,
        val editingCell: Cell? = null,
        val editingText: String = ""
    ) : TableState {

        data class Cell(val row: Int, val column: Int)
    }

    data object Error : TableState
}