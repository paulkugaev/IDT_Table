package com.badmanners.idttable.tea

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
private class TestStoreViewModel : StoreViewModel<TestState, TestEvent, TestNews>() {

    override fun createStore(): Store<TestState, TestEvent, TestNews> = testStore()
}

@OptIn(ExperimentalCoroutinesApi::class)
class StoreViewModelTest {

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun exposesInitialState() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val vm = TestStoreViewModel()

        assertEquals(TestState(), vm.state.value)
    }

    @Test
    fun dispatchesUiEvent() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val vm = TestStoreViewModel()

        vm.dispatch(TestEvent.UiEvent.Increment)
        vm.dispatch(TestEvent.UiEvent.Increment)

        assertEquals(TestState(count = 2), vm.state.first { it.count == 2 })
    }

    @Test
    fun launchesStoreCommands() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        val vm = TestStoreViewModel()

        vm.dispatch(TestEvent.UiEvent.Load)

        assertEquals(TestState(count = 10), vm.state.first { it.count == 10 })
    }
}