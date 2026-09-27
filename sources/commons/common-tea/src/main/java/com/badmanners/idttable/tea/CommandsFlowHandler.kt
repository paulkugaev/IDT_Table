package com.badmanners.idttable.tea

import kotlinx.coroutines.flow.Flow

/**
 * Interprets a flow of [Command]s into a flow of [Event]s. A command is a pure description of a
 * side effect; the handler owns the asynchronous work and maps a successful result back into an
 * event that re-enters the store's [update] function.
 */
fun interface CommandsFlowHandler<Command, Event> {

    fun handle(commands: Flow<Command>): Flow<Event>
}