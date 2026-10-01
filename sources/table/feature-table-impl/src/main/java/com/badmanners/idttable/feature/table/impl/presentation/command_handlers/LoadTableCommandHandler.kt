package com.badmanners.idttable.feature.table.impl.presentation.command_handlers

import com.badmanners.idttable.domain.repository.TableRepository
import com.badmanners.idttable.feature.table.impl.presentation.commands.TableCommand
import com.badmanners.idttable.feature.table.impl.presentation.events.TableEvent
import com.badmanners.idttable.tea.CommandsFlowHandler
import com.badmanners.idttable.tea.runCatchingCancellable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class LoadTableCommandHandler @Inject constructor(
    private val repository: TableRepository
) : CommandsFlowHandler<TableCommand, TableEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<TableCommand>): Flow<TableEvent> {
        return commands.filterIsInstance<TableCommand.Load>()
            .mapLatest { command ->
                runCatchingCancellable {
                    repository.loadTable(command.rows, command.columns)
                        .rows.map { row ->
                            row.cells.map { cell ->
                                cell.text
                            }
                        }
                }.fold(
                    onSuccess = { cells -> TableEvent.CommandResultEvent.LoadSuccess(cells) },
                    onFailure = { throwable -> TableEvent.CommandResultEvent.LoadFailure(throwable) }
                )
            }
    }
}
