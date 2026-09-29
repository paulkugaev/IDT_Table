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

/**
 * Maps the store [InputState] to the UI-ready [InputUiState], resolving error resource ids into
 * concrete strings. String resolution is delegated to the injected [stringProvider] so the mapper
 * never touches [android.content.Context] and stays unit-testable.
 */
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
        // Empty-field errors appear only after a submit attempt; range errors surface as the user types.
        return if (error == RowsError.EMPTY && !showErrors) {
            null
        } else {
            when (error) {
                RowsError.EMPTY -> R.string.input_error_empty
                RowsError.OUT_OF_RANGE -> R.string.input_error_rows_out_of_range
            }
        }
    }

    @StringRes
    private fun InputState.columnsErrorRes(): Int? {
        val error = (validation as? Invalid)?.columnsError ?: return null
        return if (error == ColumnsError.EMPTY && !showErrors) {
            null
        } else {
            when (error) {
                ColumnsError.EMPTY -> R.string.input_error_empty
                ColumnsError.OUT_OF_RANGE -> R.string.input_error_columns_out_of_range
            }
        }
    }
}