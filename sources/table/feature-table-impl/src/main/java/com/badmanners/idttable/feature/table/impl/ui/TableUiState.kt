package com.badmanners.idttable.feature.table.impl.ui

import kotlinx.collections.immutable.ImmutableList

sealed interface TableUiState {

    data object Loading : TableUiState

    data class Content(
        val cells: ImmutableList<ImmutableList<Cell>>,
        val isEditing: Boolean = false,
        val editingText: String = ""
    ) : TableUiState {

        data class Cell(
            val text: String,
            val row: Int,
            val column: Int,
            val isHighlighted: Boolean,
            val isEditing: Boolean
        )
    }

    data class Error(val message: String) : TableUiState
}