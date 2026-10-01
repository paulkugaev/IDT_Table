package com.badmanners.idttable.feature.table.impl.presentation.command_handlers

import com.badmanners.idttable.domain.model.TableDataDomain
import com.badmanners.idttable.domain.repository.TableRepository
import com.badmanners.idttable.feature.table.impl.presentation.commands.TableCommand
import com.badmanners.idttable.feature.table.impl.presentation.events.TableEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoadTableCommandHandlerTest {

    private val repository = mockk<TableRepository>()
    private val handler = LoadTableCommandHandler(repository)

    @Test
    fun `load success maps domain grid to cells`() = runTest {
        val domain = TableDataDomain(
            rows = listOf(
                TableDataDomain.TableRowDomain(
                    cells = listOf(
                        TableDataDomain.TableRowDomain.TableCellDomain("a"),
                        TableDataDomain.TableRowDomain.TableCellDomain("b")
                    )
                )
            )
        )
        coEvery { repository.loadTable(1, 2) } returns domain

        val result = handler.handle(flowOf(TableCommand.Load(1, 2))).first()

        assertEquals(
            TableEvent.CommandResultEvent.LoadSuccess(listOf(listOf("a", "b"))),
            result
        )
    }

    @Test
    fun `load failure maps to error event`() = runTest {
        coEvery { repository.loadTable(1, 2) } throws RuntimeException("boom")

        val result = handler.handle(flowOf(TableCommand.Load(1, 2))).first() as
                TableEvent.CommandResultEvent.LoadFailure

        assertEquals("boom", result.throwable.message)
    }
}
