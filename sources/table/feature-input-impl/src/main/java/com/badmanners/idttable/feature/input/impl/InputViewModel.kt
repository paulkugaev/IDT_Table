package com.badmanners.idttable.feature.input.impl

import androidx.lifecycle.viewModelScope
import com.badmanners.idttable.domain.validator.TableSizeValidator
import com.badmanners.idttable.feature.input.impl.presentation.events.InputEvent
import com.badmanners.idttable.feature.input.impl.presentation.news.InputNews
import com.badmanners.idttable.feature.input.impl.presentation.update.InputUiEventUpdate
import com.badmanners.idttable.feature.input.impl.presentation.update.InputUpdate
import com.badmanners.idttable.feature.input.impl.ui.InputStateMapper
import com.badmanners.idttable.feature.input.impl.ui.InputUiState
import com.badmanners.idttable.feature.table.api.TableFeatureScreenProvider
import com.badmanners.idttable.tea.Store
import com.badmanners.idttable.tea.StoreViewModel
import com.badmanners.idttable.tea.TeaStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InputViewModel @Inject constructor(
    private val validator: TableSizeValidator,
    private val tableScreenProvider: TableFeatureScreenProvider,
    private val inputStateMapper: InputStateMapper
) : StoreViewModel<InputState, InputEvent, InputNews>() {

    private val initialState = InputState()

    private val uiEventUpdate: InputUiEventUpdate by lazy {
        InputUiEventUpdate(validator, tableScreenProvider)
    }

    val uiState: StateFlow<InputUiState> = state
        .map(inputStateMapper::map)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            state.value.let(inputStateMapper::map)
        )

    override fun createStore(): Store<InputState, InputEvent, InputNews> =
        TeaStore(
            initialState = initialState,
            update = InputUpdate(uiEventUpdate)
        )
}
