package com.badmanners.idttable.feature.input.impl.presentation.update

import com.badmanners.idttable.feature.input.impl.InputState
import com.badmanners.idttable.feature.input.impl.presentation.commands.InputCommand
import com.badmanners.idttable.feature.input.impl.presentation.events.InputEvent
import com.badmanners.idttable.feature.input.impl.presentation.news.InputNews
import com.badmanners.idttable.tea.Update
import com.badmanners.idttable.tea.UpdateScope

class InputUpdate(
    private val uiEventUpdate: InputUiEventUpdate
) : Update<InputState, InputEvent, InputCommand, InputNews>() {

    override fun UpdateScope<InputState, InputCommand, InputNews>.update(event: InputEvent) {
        when (event) {
            is InputEvent.UiEvent -> with(uiEventUpdate) { update(event) }
        }
    }
}
