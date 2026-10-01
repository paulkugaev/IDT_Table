package com.badmanners.idttable.tea

/**
 * Pure [UpdateScope.update] function for a store. Subclasses describe, per [Event], the resulting
 * [State] transition, the [Command]s to run and the one-shot [News] to emit.
 *
 * The [UpdateScope] receiver holds the current state, so a transition can be written as
 * `state { copy(...) }`. Because [UpdateScope] is a shared type, reducers can delegate to
 * sub-updates that operate on the same scope, which keeps the umbrella `update` thin.
 */
abstract class Update<State : Any, Event : Any, Command : Any, News : Any> {

    @Suppress("MemberNameEqualsClassName")
    abstract fun UpdateScope<State, Command, News>.update(event: Event)

    internal fun apply(state: State, event: Event): UpdateResult<State, Command, News> {
        val scope = UpdateScope<State, Command, News>(state)
        scope.update(event)
        return scope.build()
    }
}

/**
 * Builder shared by an [Update] and any sub-updates it delegates to. Collects the optional next
 * [State], the [Command]s to run and the one-shot [News] to emit for a single [UpdateScope.update]
 * invocation.
 */
class UpdateScope<State : Any, Command : Any, News : Any>(val currentState: State) {

    private var nextState: State? = null
    private val commandsList = mutableListOf<Command>()
    private val newsList = mutableListOf<News>()

    fun state(block: State.() -> State) {
        nextState = currentState.block()
    }

    fun commands(vararg commands: Command) {
        commandsList.addAll(commands)
    }

    fun news(vararg news: News) {
        newsList.addAll(news)
    }

    fun build(): UpdateResult<State, Command, News> =
        UpdateResult(state = nextState, commands = commandsList, news = newsList)
}
