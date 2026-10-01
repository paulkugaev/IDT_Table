package com.badmanners.idttable.feature.input.impl

import com.badmanners.idttable.domain.validator.TableSizeValidationState
import com.badmanners.idttable.domain.validator.TableSizeValidationState.ColumnsError
import com.badmanners.idttable.domain.validator.TableSizeValidationState.Invalid
import com.badmanners.idttable.domain.validator.TableSizeValidationState.RowsError
import com.badmanners.idttable.domain.validator.TableSizeValidationState.Valid
import com.badmanners.idttable.domain.validator.TableSizeValidator
import com.badmanners.idttable.feature.input.impl.presentation.commands.InputCommand
import com.badmanners.idttable.feature.input.impl.presentation.events.InputEvent
import com.badmanners.idttable.feature.input.impl.presentation.news.InputNews
import com.badmanners.idttable.feature.input.impl.presentation.update.InputUiEventUpdate
import com.badmanners.idttable.feature.input.impl.presentation.update.InputUpdate
import com.badmanners.idttable.feature.table.api.TableFeatureScreenProvider
import com.badmanners.idttable.tea.TeaStore
import com.github.terrakok.modo.Screen
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InputUpdateTest {

    private val validator = FakeValidator()
    private val tableScreenProvider = mockk<TableFeatureScreenProvider>()
    private val screen = mockk<Screen>()

    private fun store() = TeaStore<InputState, InputEvent, InputCommand, InputNews>(
        initialState = InputState(),
        commandsFlowHandlers = emptyList(),
        update = InputUpdate(InputUiEventUpdate(validator, tableScreenProvider))
    )

    @Test
    fun `rows change updates text and validation`() = runTest {
        val store = store().also { it.launchIn(backgroundScope) }

        store.dispatch(InputEvent.UiEvent.RowsChanged("5"))

        assertEquals("5", store.state.first { it.rows == "5" }.rows)
        assertEquals(Invalid(null, ColumnsError.EMPTY), store.state.first().validation)
    }

    @Test
    fun `columns change updates text and validation`() = runTest {
        val store = store().also { it.launchIn(backgroundScope) }

        store.dispatch(InputEvent.UiEvent.ColumnsChanged("4"))

        assertEquals("4", store.state.first { it.columns == "4" }.columns)
        assertEquals(Invalid(RowsError.EMPTY, null), store.state.first().validation)
    }

    @Test
    fun `valid build emits navigate news`() = runTest {
        every { tableScreenProvider.tableScreen(10, 5) } returns screen
        val store = store().also { it.launchIn(backgroundScope) }

        store.dispatch(InputEvent.UiEvent.RowsChanged("10"))
        store.dispatch(InputEvent.UiEvent.ColumnsChanged("5"))
        store.dispatch(InputEvent.UiEvent.BuildClicked)

        assertEquals(InputNews.NavigateToTable(screen), store.news.first())
        verify { tableScreenProvider.tableScreen(10, 5) }
    }

    @Test
    fun `invalid build does not navigate`() = runTest {
        val store = store().also { it.launchIn(backgroundScope) }

        store.dispatch(InputEvent.UiEvent.RowsChanged("1001"))
        store.dispatch(InputEvent.UiEvent.BuildClicked)
        testScheduler.runCurrent()

        verify(exactly = 0) { tableScreenProvider.tableScreen(any(), any()) }
    }

    @Test
    fun `out of range columns is rejected`() = runTest {
        val store = store().also { it.launchIn(backgroundScope) }

        store.dispatch(InputEvent.UiEvent.RowsChanged("5"))
        store.dispatch(InputEvent.UiEvent.ColumnsChanged("7"))

        assertEquals(
            Invalid(null, ColumnsError.OUT_OF_RANGE),
            store.state.first { it.columns == "7" }.validation
        )
    }

    private class FakeValidator : TableSizeValidator {

        override fun validate(rows: Int?, columns: Int?): TableSizeValidationState {
            val rowsError = when {
                rows == null -> RowsError.EMPTY
                rows !in 1..1000 -> RowsError.OUT_OF_RANGE
                else -> null
            }
            val columnsError = when {
                columns == null -> ColumnsError.EMPTY
                columns !in 1..6 -> ColumnsError.OUT_OF_RANGE
                else -> null
            }
            return if (rowsError == null && columnsError == null) {
                Valid
            } else {
                Invalid(rowsError, columnsError)
            }
        }
    }
}
