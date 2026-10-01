package com.badmanners.idttable.feature.table.impl.presentation.update

import com.badmanners.idttable.feature.table.impl.presentation.commands.TableCommand
import com.badmanners.idttable.feature.table.impl.presentation.events.TableEvent
import com.badmanners.idttable.feature.table.impl.presentation.news.TableNews
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState.Content.Cell
import com.badmanners.idttable.tea.Update
import com.badmanners.idttable.tea.UpdateScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class TableUiEventUpdate : Update<TableState, TableEvent.UiEvent, TableCommand, TableNews>() {

    @Suppress("CyclomaticComplexMethod")
    override fun UpdateScope<TableState, TableCommand, TableNews>.update(event: TableEvent.UiEvent) {
        when (event) {
            TableEvent.UiEvent.Reload -> {
                // TODO reload logic
            }

            is TableEvent.UiEvent.CellClicked -> state {
                if (this is TableState.Content) {
                    val cell = Cell(event.row, event.column)
                    copy(highlightedCell = if (highlightedCell == cell) null else cell)
                } else {
                    this
                }
            }

            is TableEvent.UiEvent.CellDoubleClicked -> state {
                if (this is TableState.Content) {
                    val cell = Cell(event.row, event.column)
                    val currentText =
                        cells.getOrNull(cell.row)?.getOrNull(cell.column).orEmpty()
                    copy(editingCell = cell, editingText = currentText)
                } else {
                    this
                }
            }

            is TableEvent.UiEvent.EditingTextChanged -> state {
                if (this is TableState.Content && editingCell != null) copy(editingText = event.text) else this
            }

            TableEvent.UiEvent.EditingDone -> state {
                if (this is TableState.Content && editingCell != null) {
                    copy(
                        cells = cells.updateCell(editingCell, editingText),
                        editingCell = null,
                        editingText = ""
                    )
                } else {
                    this
                }
            }

            TableEvent.UiEvent.EditingCancelled -> state {
                if (this is TableState.Content && editingCell != null) {
                    copy(editingCell = null, editingText = "")
                } else {
                    this
                }
            }
        }
    }

    private fun ImmutableList<ImmutableList<String>>.updateCell(
        cell: Cell,
        newText: String
    ): ImmutableList<ImmutableList<String>> =
        mapIndexed { rowIndex, row ->
            if (rowIndex != cell.row) {
                row
            } else {
                row.mapIndexed { colIndex, text ->
                    if (colIndex == cell.column) newText else text
                }.toImmutableList()
            }
        }.toImmutableList()
}
