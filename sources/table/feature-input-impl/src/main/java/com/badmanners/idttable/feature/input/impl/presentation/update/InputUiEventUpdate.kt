package com.badmanners.idttable.feature.input.impl.presentation.update

import com.badmanners.idttable.domain.validator.TableSizeValidationState
import com.badmanners.idttable.domain.validator.TableSizeValidator
import com.badmanners.idttable.feature.input.impl.InputState
import com.badmanners.idttable.feature.input.impl.presentation.commands.InputCommand
import com.badmanners.idttable.feature.input.impl.presentation.events.InputEvent.UiEvent
import com.badmanners.idttable.feature.input.impl.presentation.news.InputNews
import com.badmanners.idttable.feature.table.api.TableFeatureScreenProvider
import com.badmanners.idttable.tea.Update
import com.badmanners.idttable.tea.UpdateScope

class InputUiEventUpdate(
    private val validator: TableSizeValidator,
    private val tableScreenProvider: TableFeatureScreenProvider
) : Update<InputState, UiEvent, InputCommand, InputNews>() {

    override fun UpdateScope<InputState, InputCommand, InputNews>.update(event: UiEvent) {
        when (event) {
            is UiEvent.RowsChanged -> state {
                copy(rows = event.value, validation = validate(event.value, columns))
            }

            is UiEvent.ColumnsChanged -> state {
                copy(columns = event.value, validation = validate(rows, event.value))
            }

            UiEvent.BuildClicked -> {
                val isValid = currentState.validation is TableSizeValidationState.Valid

                if (isValid) {
                    val targetRows = currentState.rows.toIntOrNull() ?: 0
                    val targetColumns = currentState.columns.toIntOrNull() ?: 0
                    news(
                        InputNews.NavigateToTable(
                            tableScreenProvider.tableScreen(
                                targetRows,
                                targetColumns
                            )
                        )
                    )
                }
            }
        }
    }

    private fun validate(rows: String, columns: String): TableSizeValidationState =
        validator.validate(rows.toIntOrNull(), columns.toIntOrNull())
}
