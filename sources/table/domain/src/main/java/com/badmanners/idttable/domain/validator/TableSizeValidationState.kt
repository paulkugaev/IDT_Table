package com.badmanners.idttable.domain.validator

sealed interface TableSizeValidationState {
    data object Valid : TableSizeValidationState

    data class Invalid(
        val rowsError: RowsError? = null,
        val columnsError: ColumnsError? = null
    ) : TableSizeValidationState

    enum class RowsError { EMPTY, OUT_OF_RANGE }

    enum class ColumnsError { EMPTY, OUT_OF_RANGE }
}