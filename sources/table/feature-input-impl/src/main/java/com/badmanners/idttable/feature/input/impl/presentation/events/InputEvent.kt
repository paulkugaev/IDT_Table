package com.badmanners.idttable.feature.input.impl.presentation.events

sealed interface InputEvent {

    sealed interface UiEvent : InputEvent {

        data class RowsChanged(val value: String) : UiEvent

        data class ColumnsChanged(val value: String) : UiEvent

        data object BuildClicked : UiEvent
    }
}