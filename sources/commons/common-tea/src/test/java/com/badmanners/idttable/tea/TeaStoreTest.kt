package com.badmanners.idttable.tea

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TeaStoreTest {

    @Test
    fun startsWithInitialState() {
        val store = testStore()

        assertEquals(TestState(), store.state.value)
    }

    @Test
    fun dispatchAppliesStateTransition() = runTest {
        val store = testStore()
        store.launchIn(backgroundScope)

        store.dispatch(TestEvent.UiEvent.Increment)
        store.dispatch(TestEvent.UiEvent.Increment)

        assertEquals(TestState(count = 2), store.state.first { it.count == 2 })
    }

    @Test
    fun dispatchEmitsNews() = runTest {
        val store = testStore()
        store.launchIn(backgroundScope)

        store.dispatch(TestEvent.UiEvent.Fail)

        assertEquals(TestNews("boom"), store.news.first())
    }

    @Test
    fun commandResultUpdatesState() = runTest {
        val store = testStore()
        store.launchIn(backgroundScope)

        store.dispatch(TestEvent.UiEvent.Load)

        assertEquals(TestState(count = 10), store.state.first { it.count == 10 })
    }

    @Test
    fun initialCommandsAreProcessed() = runTest {
        val store = testStore(initialCommands = listOf(TestCommand.Stub(7)))
        store.launchIn(backgroundScope)

        assertEquals(TestState(count = 7), store.state.first { it.count == 7 })
    }

    @Test
    fun allInitialCommandsAreProcessed() = runTest {
        val store = testStore(
            initialCommands = listOf(TestCommand.Stub(1), TestCommand.Stub(2), TestCommand.Stub(3))
        )
        store.launchIn(backgroundScope)

        // Each command adds its index, so count == 1+2+3 only if every one was applied.
        assertEquals(TestState(count = 6), store.state.first { it.count == 6 })
    }

    @Test
    fun launchInTwiceThrows() {
        val store = testStore()
        store.launchIn(CoroutineScope(Dispatchers.Unconfined))

        assertThrows(IllegalStateException::class.java) {
            store.launchIn(CoroutineScope(Dispatchers.Unconfined))
        }
    }
}