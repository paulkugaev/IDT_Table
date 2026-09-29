package com.badmanners.idttable.feature.input.impl.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.badmanners.idttable.feature.input.impl.InputViewModel
import com.badmanners.idttable.feature.input.impl.presentation.news.InputNews
import com.github.terrakok.modo.Screen
import com.github.terrakok.modo.ScreenKey
import com.github.terrakok.modo.generateScreenKey
import com.github.terrakok.modo.stack.LocalStackNavigation
import com.github.terrakok.modo.stack.forward
import kotlinx.parcelize.Parcelize

@Parcelize
class InputScreen(
    override val screenKey: ScreenKey = generateScreenKey()
) : Screen {

    @Composable
    override fun Content(modifier: Modifier) {
        val viewModel: InputViewModel = hiltViewModel()
        val navigation = LocalStackNavigation.current

        LaunchedEffect(viewModel) {
            viewModel.news.collect { news ->
                when (news) {
                    is InputNews.NavigateToTable -> navigation.forward(news.screen)
                }
            }
        }

        val state by viewModel.uiState.collectAsState()
        InputScreenContent(
            state = state,
            dispatch = viewModel::dispatch,
            modifier = modifier
        )
    }
}
