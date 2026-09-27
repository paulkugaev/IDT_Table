package com.badmanners.idttable.tea

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * A screen model in the TEA (The Elm Architecture) style.
 *
 * [state] is the current UI state, [news] are one-shot events (navigation, toast) and [dispatch]
 * accepts user-driven events. Side effects are realized through commands: the implementation
 * converts dispatched events into commands, runs them and feeds the results back as new events.
 * [launchIn] starts command processing in the given [scope].
 */
interface Store<State : Any, Event : Any, News : Any> {

    val state: StateFlow<State>

    val news: Flow<News>

    fun dispatch(event: Event)

    fun launchIn(scope: CoroutineScope)
}