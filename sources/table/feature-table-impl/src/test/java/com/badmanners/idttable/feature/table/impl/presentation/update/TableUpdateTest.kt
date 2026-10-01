package com.badmanners.idttable.feature.table.impl.presentation.update

import com.badmanners.idttable.feature.table.impl.presentation.commands.TableCommand
import com.badmanners.idttable.feature.table.impl.presentation.events.TableEvent
import com.badmanners.idttable.feature.table.impl.presentation.news.TableNews
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState.Content.Cell
import com.badmanners.idttable.tea.CommandsFlowHandler
import com.badmanners.idttable.tea.TeaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TableUpdateTest {

    private val grid = listOf(listOf("a", "b"), listOf("c", "d"))

    private fun store(): TeaStore<TableState, TableEvent, TableCommand, TableNews> =
        TeaStore(
            initialState = TableState.Loading,
            initialCommands = listOf(TableCommand.Load(2, 2)),
            commandsFlowHandlers = listOf(fakeLoadHandler(grid)),
            update = TableUpdate(TableUiEventUpdate(), TableResultEventUpdate())
        )

    private fun fakeLoadHandler(cells: List<List<String>>) =
        CommandsFlowHandler<TableCommand, TableEvent> { commands ->
            commands.map { command ->
                when (command) {
                    is TableCommand.Load -> TableEvent.CommandResultEvent.LoadSuccess(cells)
                }
            }
        }

    private suspend fun loadedStore(
        store: TeaStore<TableState, TableEvent, TableCommand, TableNews>,
        scope: CoroutineScope
    ): TableState.Content {
        store.launchIn(scope)
        return store.state.first { it is TableState.Content } as TableState.Content
    }

    @Test
    fun `cell click highlights cell`() = runTest {
        val store = store()
        loadedStore(store, backgroundScope)

        store.dispatch(TableEvent.UiEvent.CellClicked(0, 1))

        val content = store.state.first {
            (it as? TableState.Content)?.highlightedCell == Cell(0, 1)
        } as TableState.Content
        assertEquals(Cell(0, 1), content.highlightedCell)
    }

    @Test
    fun `clicking highlighted cell again clears highlight`() = runTest {
        val store = store()
        loadedStore(store, backgroundScope)

        store.dispatch(TableEvent.UiEvent.CellClicked(0, 1))
        store.state.first { (it as? TableState.Content)?.highlightedCell == Cell(0, 1) }
        store.dispatch(TableEvent.UiEvent.CellClicked(0, 1))

        val content = store.state.first {
            (it as? TableState.Content)?.highlightedCell == null
        } as TableState.Content
        assertNull(content.highlightedCell)
    }

    @Test
    fun `double click starts editing with current text`() = runTest {
        val store = store()
        loadedStore(store, backgroundScope)

        store.dispatch(TableEvent.UiEvent.CellDoubleClicked(0, 1))

        val content =
            store.state.first { (it as? TableState.Content)?.editingCell != null } as TableState.Content
        assertEquals(Cell(0, 1), content.editingCell)
        assertEquals("b", content.editingText)
    }

    @Test
    fun `editing done commits new text and leaves edit mode`() = runTest {
        val store = store()
        loadedStore(store, backgroundScope)

        store.dispatch(TableEvent.UiEvent.CellClicked(0, 0))
        store.dispatch(TableEvent.UiEvent.CellDoubleClicked(0, 1))
        store.dispatch(TableEvent.UiEvent.EditingTextChanged("X"))
        store.dispatch(TableEvent.UiEvent.EditingDone)

        val content = store.state.first {
            (it as? TableState.Content)?.cells?.get(0)?.get(1) == "X"
        } as TableState.Content
        assertEquals("X", content.cells[0][1])
        assertNull(content.editingCell)
        assertEquals(Cell(0, 0), content.highlightedCell)
    }

    @Test
    fun `editing cancelled leaves edit mode without committing`() = runTest {
        val store = store()
        loadedStore(store, backgroundScope)

        store.dispatch(TableEvent.UiEvent.CellClicked(0, 0))
        store.dispatch(TableEvent.UiEvent.CellDoubleClicked(0, 1))
        store.dispatch(TableEvent.UiEvent.EditingTextChanged("X"))
        store.dispatch(TableEvent.UiEvent.EditingCancelled)

        val content = store.state.first {
            (it as? TableState.Content)?.editingCell == null &&
                    (it as? TableState.Content)?.highlightedCell == Cell(0, 0)
        } as TableState.Content
        assertNull(content.editingCell)
        assertEquals("b", content.cells[0][1])
        assertEquals(Cell(0, 0), content.highlightedCell)
    }
}
