package com.badmanners.idttable.tea

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

internal data class TestState(val count: Int = 0)

internal sealed interface TestEvent {

    sealed interface UiEvent : TestEvent {
        data object Increment : UiEvent
        data object Fail : UiEvent
        data object Load : UiEvent
    }

    data object Loaded : TestEvent
    data class Update(val index: Int) : TestEvent
}

internal sealed interface TestCommand {
    data object Load : TestCommand
    data class Stub(val index: Int) : TestCommand
}

internal data class TestNews(val message: String)

internal class TestUpdate : Update<TestState, TestEvent, TestCommand, TestNews>() {

    override fun UpdateScope.update(event: TestEvent) {
        when (event) {
            is TestEvent.UiEvent.Increment -> state { copy(count = count + 1) }
            is TestEvent.UiEvent.Fail -> news(TestNews("boom"))
            is TestEvent.UiEvent.Load -> commands(TestCommand.Load)
            is TestEvent.Loaded -> state { copy(count = count + 10) }
            is TestEvent.Update -> state { copy(count = count + event.index) }
        }
    }
}

internal fun testStore(
    initialCommands: List<TestCommand> = emptyList()
): TeaStore<TestState, TestEvent, TestEvent.UiEvent, TestCommand, TestNews> = TeaStore(
    initialState = TestState(),
    initialCommands = initialCommands,
    commandsFlowHandlers = listOf(
        CommandsFlowHandler { commands ->
            commands.filterIsInstance<TestCommand.Load>().map { TestEvent.Loaded }
        },
        CommandsFlowHandler { commands ->
            commands.filterIsInstance<TestCommand.Stub>().map { TestEvent.Update(it.index) }
        }
    ),
    update = TestUpdate()
)