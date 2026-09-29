package com.badmanners.idttable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.badmanners.common_ui.ui.theme.IDTTableTheme
import com.badmanners.idttable.ui.HomeScreen
import com.github.terrakok.modo.Modo.rememberRootScreen
import com.github.terrakok.modo.stack.DefaultStackScreen
import com.github.terrakok.modo.stack.StackNavModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IDTTableTheme {
                val rootScreen = rememberRootScreen {
                    DefaultStackScreen(StackNavModel(HomeScreen()))
                }
                rootScreen.Content(modifier = Modifier.fillMaxSize())
            }
        }
    }
}