package com.badmanners.idttable.feature.table.impl.ui

import com.badmanners.common_ui.ui.StringProvider
import com.badmanners.idttable.feature.table.impl.R
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState.Content.Cell
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TableStateMapperTest {

    private val mapper = TableStateMapper(StringProvider { "res_$it" })

    @Test
    fun `loading maps to loading`() {
        assertEquals(TableUiState.Loading, mapper.map(TableState.Loading))
    }

    @Test
    fun `content maps to content with cell ui`() {
        val state = TableState.Content(
            cells = persistentListOf(persistentListOf("a", "b")),
            highlightedCell = Cell(0, 0),
            editingCell = Cell(0, 1),
            editingText = "editing"
        )

        val uiState = mapper.map(state) as TableUiState.Content

        val cell00 = uiState.cells[0][0]
        assertEquals("a", cell00.text)
        assertEquals(0, cell00.row)
        assertEquals(0, cell00.column)
        assertTrue(cell00.isHighlighted)
        assertFalse(cell00.isEditing)

        val cell01 = uiState.cells[0][1]
        assertEquals("b", cell01.text)
        assertEquals(0, cell01.row)
        assertEquals(1, cell01.column)
        assertFalse(cell01.isHighlighted)
        assertTrue(cell01.isEditing)

        assertTrue(uiState.isEditing)
        assertEquals("editing", uiState.editingText)
    }

    @Test
    fun `error maps to resolved message`() {
        assertEquals(
            TableUiState.Error("res_${R.string.table_error_load}"),
            mapper.map(TableState.Error)
        )
    }
}