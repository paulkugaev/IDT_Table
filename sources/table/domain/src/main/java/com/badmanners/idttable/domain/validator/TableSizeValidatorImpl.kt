package com.badmanners.idttable.domain.validator

import com.badmanners.idttable.domain.config.TableConfig
import com.badmanners.idttable.domain.validator.TableSizeValidationState.ColumnsError
import com.badmanners.idttable.domain.validator.TableSizeValidationState.Invalid
import com.badmanners.idttable.domain.validator.TableSizeValidationState.RowsError
import com.badmanners.idttable.domain.validator.TableSizeValidationState.Valid
import javax.inject.Inject

internal class TableSizeValidatorImpl @Inject constructor() : TableSizeValidator {

    override fun validate(rows: Int?, columns: Int?): TableSizeValidationState {
        val rowsError = when {
            rows == null -> RowsError.EMPTY
            rows !in TableConfig.MIN_ROWS..TableConfig.MAX_ROWS -> RowsError.OUT_OF_RANGE
            else -> null
        }
        val columnsError = when {
            columns == null -> ColumnsError.EMPTY
            columns !in TableConfig.MIN_COLUMNS..TableConfig.MAX_COLUMNS -> ColumnsError.OUT_OF_RANGE
            else -> null
        }
        return if (rowsError == null && columnsError == null) {
            Valid
        } else {
            Invalid(rowsError, columnsError)
        }
    }
}