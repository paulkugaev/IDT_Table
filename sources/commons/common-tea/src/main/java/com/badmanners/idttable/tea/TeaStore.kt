package com.badmanners.idttable.tea

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Default [Store] implementation. The [UiEvent] type is the subtype of [Event] that the UI is
 * allowed to [dispatch]; command results are ordinary [Event]s fed back from [CommandsFlowHandler]s.
 *
 * Both dispatched events and command results are pushed to a single [events] channel and applied
 * sequentially by one coroutine started in [launchIn], so state transitions never run concurrently.
 * Commands produced by [update] are broadcast to every [CommandsFlowHandler] subscriber. Initial
 * commands are re-emitted to each subscriber via [onSubscription], so they are never lost. A store
 * may be launched only once; [launchIn] fails otherwise.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TeaStore<State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any>(
    initialState: State,
    private val initialCommands: List<Command> = emptyList(),
    private val commandsFlowHandlers: List<CommandsFlowHandler<Command, Event>>,
    private val update: Update<State, Event, Command, News>
) : Store<State, UiEvent, News> {

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<State> = _state.asStateFlow()

    private val _news = Channel<News>(Channel.BUFFERED)
    override val news: Flow<News> = _news.receiveAsFlow()

    private val events = Channel<Event>(Channel.BUFFERED)
    private val commands = Channel<Command>(Channel.BUFFERED)

    private val launched = AtomicBoolean(false)

    override fun dispatch(event: UiEvent) {
        events.trySend(event)
    }

    override fun launchIn(scope: CoroutineScope) {
        if (launched.getAndSet(true)) error("Store has already been launched")

        val commandsFlow = commands.receiveAsFlow()
            .shareIn(scope, SharingStarted.Lazily, replay = 0)

        scope.launch {
            commandsFlowHandlers
                .map { handler ->
                    handler.handle(commandsFlow.onSubscription {
                        emitAll(
                            initialCommands.asFlow()
                        )
                    })
                }
                .asFlow()
                .flattenMerge()
                .catch { throwable ->
                    if (throwable is CancellationException) throw throwable
                    throw CommandsFlowHandlerException(throwable)
                }
                .collect { events.trySend(it) }
        }

        scope.launch {
            events.receiveAsFlow().collect(::apply)
        }
    }

    private fun apply(event: Event) {
        val result = update.apply(_state.value, event)
        result.state?.let { _state.value = it }
        result.commands.forEach { commands.trySend(it) }
        result.news.forEach { _news.trySend(it) }
    }
}
