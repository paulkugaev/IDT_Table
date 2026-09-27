package com.badmanners.idttable.tea

/**
 * Result of folding an [Event] against the current [State]: an optional next [State], a list of
 * [Command]s to run and a list of one-shot [News] to dispatch once.
 */
data class UpdateResult<out State, out Command, out News>(
    val state: State? = null,
    val commands: List<Command> = emptyList(),
    val news: List<News> = emptyList()
)