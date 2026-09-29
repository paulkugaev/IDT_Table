package com.badmanners.idttable.feature.input.impl

import com.badmanners.idttable.domain.validator.TableSizeValidationState
import com.badmanners.idttable.domain.validator.TableSizeValidationState.ColumnsError
import com.badmanners.idttable.domain.validator.TableSizeValidationState.Invalid
import com.badmanners.idttable.domain.validator.TableSizeValidationState.RowsError

data class InputState(
    val rows: String = "",
    val columns: String = "",
    val showErrors: Boolean = false,
    val validation: TableSizeValidationState = Invalid(RowsError.EMPTY, ColumnsError.EMPTY)
)