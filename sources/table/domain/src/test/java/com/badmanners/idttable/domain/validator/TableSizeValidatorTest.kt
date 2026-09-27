package com.badmanners.idttable.domain.validator

import com.badmanners.idttable.domain.config.TableConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TableSizeValidatorTest {

    private val validator: TableSizeValidator = TableSizeValidatorImpl()

    @Test
    fun validSizesWithinLimits() {
        assertTrue(validator.validate(1, 1) is TableSizeValidationState.Valid)
        assertTrue(validator.validate(500, 3) is TableSizeValidationState.Valid)
        assertTrue(
            validator.validate(
                TableConfig.MAX_ROWS,
                TableConfig.MAX_COLUMNS
            ) is TableSizeValidationState.Valid
        )
    }

    @Test
    fun boundaryValuesAreValid() {
        assertTrue(validator.validate(1, TableConfig.MAX_COLUMNS) is TableSizeValidationState.Valid)
        assertTrue(validator.validate(TableConfig.MAX_ROWS, 1) is TableSizeValidationState.Valid)
    }

    @Test
    fun nullInputIsEmptyError() {
        val result = validator.validate(null, null) as TableSizeValidationState.Invalid
        assertEquals(TableSizeValidationState.RowsError.EMPTY, result.rowsError)
        assertEquals(TableSizeValidationState.ColumnsError.EMPTY, result.columnsError)
    }

    @Test
    fun rowsOutOfRange() {
        assertEquals(
            TableSizeValidationState.RowsError.OUT_OF_RANGE,
            (validator.validate(0, 3) as TableSizeValidationState.Invalid).rowsError
        )
        assertEquals(
            TableSizeValidationState.RowsError.OUT_OF_RANGE,
            (validator.validate(1001, 3) as TableSizeValidationState.Invalid).rowsError
        )
    }

    @Test
    fun columnsOutOfRange() {
        assertEquals(
            TableSizeValidationState.ColumnsError.OUT_OF_RANGE,
            (validator.validate(5, 0) as TableSizeValidationState.Invalid).columnsError
        )
        assertEquals(
            TableSizeValidationState.ColumnsError.OUT_OF_RANGE,
            (validator.validate(5, 7) as TableSizeValidationState.Invalid).columnsError
        )
    }

    @Test
    fun mixedCasesSetOnlyTheFailingAxis() {
        val rowsEmpty = validator.validate(null, 3) as TableSizeValidationState.Invalid
        assertEquals(TableSizeValidationState.RowsError.EMPTY, rowsEmpty.rowsError)
        assertNull(rowsEmpty.columnsError)

        val rowsOut = validator.validate(0, 3) as TableSizeValidationState.Invalid
        assertEquals(TableSizeValidationState.RowsError.OUT_OF_RANGE, rowsOut.rowsError)
        assertNull(rowsOut.columnsError)

        val columnsEmpty = validator.validate(5, null) as TableSizeValidationState.Invalid
        assertNull(columnsEmpty.rowsError)
        assertEquals(TableSizeValidationState.ColumnsError.EMPTY, columnsEmpty.columnsError)

        val columnsOut = validator.validate(5, 7) as TableSizeValidationState.Invalid
        assertNull(columnsOut.rowsError)
        assertEquals(TableSizeValidationState.ColumnsError.OUT_OF_RANGE, columnsOut.columnsError)
    }

    @Test
    fun bothAxesOutOfRange() {
        val result = validator.validate(0, 7) as TableSizeValidationState.Invalid
        assertEquals(TableSizeValidationState.RowsError.OUT_OF_RANGE, result.rowsError)
        assertEquals(TableSizeValidationState.ColumnsError.OUT_OF_RANGE, result.columnsError)
    }
}
