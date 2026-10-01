package com.badmanners.idttable.feature.table.impl.presentation.update

import com.badmanners.idttable.feature.table.impl.presentation.commands.TableCommand
import com.badmanners.idttable.feature.table.impl.presentation.events.TableEvent
import com.badmanners.idttable.feature.table.impl.presentation.news.TableNews
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState
import com.badmanners.idttable.tea.Update
import com.badmanners.idttable.tea.UpdateScope

class TableUpdate(
    private val uiEventUpdate: TableUiEventUpdate,
    private val resultEventUpdate: TableResultEventUpdate
) : Update<TableState, TableEvent, TableCommand, TableNews>() {

    override fun UpdateScope<TableState, TableCommand, TableNews>.update(event: TableEvent) {
        when (event) {
            is TableEvent.UiEvent -> with(uiEventUpdate) { update(event) }
            is TableEvent.CommandResultEvent -> with(resultEventUpdate) { update(event) }
        }
    }
}