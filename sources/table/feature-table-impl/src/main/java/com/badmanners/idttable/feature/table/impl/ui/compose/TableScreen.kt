package com.badmanners.idttable.feature.table.impl.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.badmanners.idttable.feature.table.impl.TableViewModel
import com.badmanners.idttable.feature.table.impl.presentation.events.TableEvent
import com.github.terrakok.modo.Screen
import com.github.terrakok.modo.ScreenKey
import com.github.terrakok.modo.generateScreenKey
import com.github.terrakok.modo.stack.LocalStackNavigation
import com.github.terrakok.modo.stack.back
import kotlinx.parcelize.Parcelize

@Parcelize
class TableScreen(
    private val rows: Int,
    private val columns: Int,
    override val screenKey: ScreenKey = generateScreenKey()
) : Screen {

    @Composable
    override fun Content(modifier: Modifier) {
        val viewModel: TableViewModel =
            hiltViewModel<TableViewModel, TableViewModel.Factory> { factory ->
                factory.create(rows, columns)
            }
        val navigation = LocalStackNavigation.current

        val state by viewModel.uiState.collectAsState()
        TableScreenContent(
            state = state,
            dispatch = viewModel::dispatch,
            onRetry = { viewModel.dispatch(TableEvent.UiEvent.Reload) },
            onBack = { navigation.back() },
            modifier = modifier
        )
    }
}
