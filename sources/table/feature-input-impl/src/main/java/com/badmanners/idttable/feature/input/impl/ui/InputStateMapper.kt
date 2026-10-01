package com.badmanners.idttable.feature.input.impl.ui

import androidx.annotation.StringRes
import com.badmanners.common_ui.ui.StringProvider
import com.badmanners.idttable.domain.validator.TableSizeValidationState.ColumnsError
import com.badmanners.idttable.domain.validator.TableSizeValidationState.Invalid
import com.badmanners.idttable.domain.validator.TableSizeValidationState.RowsError
import com.badmanners.idttable.domain.validator.TableSizeValidationState.Valid
import com.badmanners.idttable.feature.input.impl.InputState
import com.badmanners.idttable.feature.input.impl.R
import javax.inject.Inject

class InputStateMapper @Inject constructor(
    private val stringProvider: StringProvider
) {

    fun map(state: InputState): InputUiState = InputUiState(
        rows = state.rows,
        columns = state.columns,
        rowsError = state.rowsErrorRes()?.let(stringProvider::get),
        columnsError = state.columnsErrorRes()?.let(stringProvider::get),
        buildEnabled = state.validation is Valid
    )

    @StringRes
    private fun InputState.rowsErrorRes(): Int? {
        val error = (validation as? Invalid)?.rowsError ?: return null
        return when (error) {
            RowsError.EMPTY -> null
            RowsError.OUT_OF_RANGE -> R.string.input_error_rows_out_of_range
        }
    }

    @StringRes
    private fun InputState.columnsErrorRes(): Int? {
        val error = (validation as? Invalid)?.columnsError ?: return null
        return when (error) {
            ColumnsError.EMPTY -> null
            ColumnsError.OUT_OF_RANGE -> R.string.input_error_columns_out_of_range
        }
    }
}