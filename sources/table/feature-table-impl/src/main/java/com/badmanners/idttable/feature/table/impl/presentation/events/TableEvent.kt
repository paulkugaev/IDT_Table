package com.badmanners.idttable.feature.table.impl.presentation.events

sealed interface TableEvent {

    sealed interface UiEvent : TableEvent {

        data object Reload : UiEvent

        data class CellClicked(val row: Int, val column: Int) : UiEvent

        data class CellDoubleClicked(val row: Int, val column: Int) : UiEvent

        data class EditingTextChanged(val text: String) : UiEvent

        data object EditingDone : UiEvent

        data object EditingCancelled : UiEvent
    }

    sealed interface CommandResultEvent : TableEvent {

        data class LoadSuccess(val cells: List<List<String>>) : CommandResultEvent

        data class LoadFailure(val throwable: Throwable) : CommandResultEvent
    }
}
