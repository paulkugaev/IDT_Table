package com.badmanners.idttable.feature.input.impl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badmanners.common_ui.ui.theme.IDTTableTheme
import com.badmanners.idttable.feature.input.impl.R
import com.badmanners.idttable.feature.input.impl.presentation.events.InputEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InputScreenContent(
    state: InputUiState,
    dispatch: (InputEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.input_title)) },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.rows,
                onValueChange = { dispatch(InputEvent.UiEvent.RowsChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.input_rows_label)) },
                singleLine = true,
                isError = state.rowsError != null,
                supportingText = state.rowsError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = state.columns,
                onValueChange = { dispatch(InputEvent.UiEvent.ColumnsChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.input_columns_label)) },
                singleLine = true,
                isError = state.columnsError != null,
                supportingText = state.columnsError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = { dispatch(InputEvent.UiEvent.BuildClicked) },
                enabled = state.buildEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.input_build_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun InputScreenContentDefaultPreview() {
    IDTTableTheme(dynamicColor = false) {
        InputScreenContent(
            state = InputUiState(
                rows = "10",
                columns = "5",
                rowsError = null,
                columnsError = null,
                buildEnabled = true
            ),
            dispatch = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun InputScreenContentErrorsPreview() {
    IDTTableTheme(dynamicColor = false) {
        InputScreenContent(
            state = InputUiState(
                rows = "0",
                columns = "7",
                rowsError = "Rows must be 1–1000",
                columnsError = "Columns must be 1–6",
                buildEnabled = false
            ),
            dispatch = {}
        )
    }
}
