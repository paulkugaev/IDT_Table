package com.badmanners.idttable.feature.input.impl.ui

import com.badmanners.common_ui.ui.StringProvider
import com.badmanners.idttable.domain.validator.TableSizeValidationState.ColumnsError
import com.badmanners.idttable.domain.validator.TableSizeValidationState.Invalid
import com.badmanners.idttable.domain.validator.TableSizeValidationState.RowsError
import com.badmanners.idttable.domain.validator.TableSizeValidationState.Valid
import com.badmanners.idttable.feature.input.impl.InputState
import com.badmanners.idttable.feature.input.impl.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class InputStateMapperTest {

    private val mapper = InputStateMapper(StringProvider { "res_$it" })

    @Test
    fun `valid input maps to enabled build with no errors`() {
        val uiState = mapper.map(InputState(rows = "10", columns = "5", validation = Valid))

        assertEquals("10", uiState.rows)
        assertEquals("5", uiState.columns)
        assertNull(uiState.rowsError)
        assertNull(uiState.columnsError)
        assertTrue(uiState.buildEnabled)
    }

    @Test
    fun `out of range error surfaces as resolved string`() {
        val state = InputState(
            rows = "0",
            columns = "7",
            validation = Invalid(RowsError.OUT_OF_RANGE, ColumnsError.OUT_OF_RANGE)
        )

        val uiState = mapper.map(state)

        assertEquals("res_${R.string.input_error_rows_out_of_range}", uiState.rowsError)
        assertEquals("res_${R.string.input_error_columns_out_of_range}", uiState.columnsError)
        assertFalse(uiState.buildEnabled)
    }

    @Test
    fun `empty input yields no error`() {
        val uiState = mapper.map(
            InputState(validation = Invalid(RowsError.EMPTY, ColumnsError.EMPTY))
        )

        assertNull(uiState.rowsError)
        assertNull(uiState.columnsError)
        assertFalse(uiState.buildEnabled)
    }
}