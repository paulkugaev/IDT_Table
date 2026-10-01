package com.badmanners.idttable.feature.table.impl.ui

import com.badmanners.common_ui.ui.StringProvider
import com.badmanners.idttable.feature.table.impl.R
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState
import com.badmanners.idttable.feature.table.impl.ui.TableUiState.Content.Cell
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState.Content.Cell as CellDomain

class TableStateMapper @Inject constructor(
    private val stringProvider: StringProvider
) {

    fun map(state: TableState): TableUiState = when (state) {
        TableState.Loading -> TableUiState.Loading
        is TableState.Content -> TableUiState.Content(
            cells = state.cells.mapIndexed { rowIndex, row ->
                row.mapIndexed { columnIndex, text ->
                    val cell = CellDomain(rowIndex, columnIndex)
                    Cell(
                        text = text,
                        row = rowIndex,
                        column = columnIndex,
                        isHighlighted = state.highlightedCell == cell,
                        isEditing = state.editingCell == cell
                    )
                }.toImmutableList()
            }.toImmutableList(),
            isEditing = state.editingCell != null,
            editingText = state.editingText
        )

        TableState.Error -> TableUiState.Error(stringProvider.get(R.string.table_error_load))
    }
}
