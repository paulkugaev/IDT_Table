package com.badmanners.idttable.feature.table.impl

import androidx.lifecycle.viewModelScope
import com.badmanners.idttable.feature.table.impl.presentation.command_handlers.LoadTableCommandHandler
import com.badmanners.idttable.feature.table.impl.presentation.commands.TableCommand
import com.badmanners.idttable.feature.table.impl.presentation.events.TableEvent
import com.badmanners.idttable.feature.table.impl.presentation.news.TableNews
import com.badmanners.idttable.feature.table.impl.presentation.state.TableState
import com.badmanners.idttable.feature.table.impl.presentation.update.TableResultEventUpdate
import com.badmanners.idttable.feature.table.impl.presentation.update.TableUiEventUpdate
import com.badmanners.idttable.feature.table.impl.presentation.update.TableUpdate
import com.badmanners.idttable.feature.table.impl.ui.TableStateMapper
import com.badmanners.idttable.feature.table.impl.ui.TableUiState
import com.badmanners.idttable.tea.Store
import com.badmanners.idttable.tea.StoreViewModel
import com.badmanners.idttable.tea.TeaStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = TableViewModel.Factory::class)
class TableViewModel @AssistedInject constructor(
    private val loadTableCommandHandler: LoadTableCommandHandler,
    private val tableStateMapper: TableStateMapper,
    @Assisted("rows") private val rows: Int,
    @Assisted("columns") private val columns: Int
) : StoreViewModel<TableState, TableEvent, TableNews>() {

    val uiState: StateFlow<TableUiState> = state
        .map(tableStateMapper::map)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            TableState.Loading.let(tableStateMapper::map)
        )

    override fun createStore(): Store<TableState, TableEvent, TableNews> =
        TeaStore(
            initialState = TableState.Loading,
            initialCommands = listOf(TableCommand.Load(rows, columns)),
            commandsFlowHandlers = listOf(loadTableCommandHandler),
            update = TableUpdate(TableUiEventUpdate(), TableResultEventUpdate())
        )

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("rows") rows: Int,
            @Assisted("columns") columns: Int
        ): TableViewModel
    }
}
