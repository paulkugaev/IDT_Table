package com.badmanners.idttable.tea

/**
 * Pure [UpdateScope.update] function for a store. Subclasses describe, per [Event], the resulting
 * [State] transition, the [Command]s to run and the one-shot [News] to emit.
 *
 * The [UpdateScope] receiver holds the current state, so a transition can be written as
 * `state { copy(...) }`.
 */
abstract class Update<State : Any, Event : Any, Command : Any, News : Any> {

    @Suppress("MemberNameEqualsClassName")
    abstract fun UpdateScope.update(event: Event)

    internal fun apply(state: State, event: Event): UpdateResult<State, Command, News> {
        val scope = UpdateScope(state)
        scope.update(event)
        return scope.build()
    }

    inner class UpdateScope(private val currentState: State) {

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
}