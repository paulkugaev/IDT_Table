package com.badmanners.idttable.feature.table.impl.presentation.update

import com.badmanners.idttable.feature.table.impl.presentation.commands.TableCommand
import com.badmanners.idttable.feature.table.impl.presentation.events.TableEvent
import com.badmanners.idttable.feature.table.impl.presentation.news.TableNews
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState
import com.badmanners.idttable.tea.Update
import com.badmanners.idttable.tea.UpdateScope
import kotlinx.collections.immutable.toImmutableList

class TableResultEventUpdate :
    Update<TableState, TableEvent.CommandResultEvent, TableCommand, TableNews>() {

    override fun UpdateScope<TableState, TableCommand, TableNews>.update(event: TableEvent.CommandResultEvent) {
        when (event) {
            is TableEvent.CommandResultEvent.LoadSuccess -> state {
                TableState.Content(cells = event.cells.map { it.toImmutableList() }
                    .toImmutableList())
            }

            is TableEvent.CommandResultEvent.LoadFailure -> state { TableState.Error }
        }
    }
}
