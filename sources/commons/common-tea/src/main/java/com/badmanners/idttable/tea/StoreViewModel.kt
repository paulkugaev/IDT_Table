package com.badmanners.idttable.tea

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Base class for hosting a [Store] in a [ViewModel]. Subclasses build a [Store] via [createStore];
 * it is launched in the host's [viewModelScope] on first access.
 *
 * Exposes the store's [state], [news] and [dispatch] straight to the UI, so the feature only needs
 * to define the pure store and its commands.
 */
abstract class StoreViewModel<State : Any, Event : Any, News : Any> : ViewModel() {

    private val store: Store<State, Event, News> by lazy {
        createStore().also { it.launchIn(viewModelScope) }
    }

    val state: StateFlow<State> get() = store.state

    val news: Flow<News> get() = store.news

    fun dispatch(event: Event) {
        store.dispatch(event)
    }

    protected abstract fun createStore(): Store<State, Event, News>
}