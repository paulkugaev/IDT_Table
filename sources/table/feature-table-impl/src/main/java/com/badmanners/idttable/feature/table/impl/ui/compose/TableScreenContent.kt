package com.badmanners.idttable.feature.table.impl.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.badmanners.common_ui.ui.theme.IDTTableTheme
import com.badmanners.idttable.feature.table.impl.R
import com.badmanners.idttable.feature.table.impl.presentation.events.TableEvent
import com.badmanners.idttable.feature.table.impl.ui.TableUiState
import com.badmanners.idttable.feature.table.impl.ui.TableUiState.Content.Cell
import kotlinx.collections.immutable.persistentListOf

private val HighlightColor = Color(0xFF4CAF50).copy(alpha = 0.25f)
private val MinColumnWidth = 96.dp
private val MaxColumnWidth = 300.dp
private val MinRowHeight = 48.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TableScreenContent(
    state: TableUiState,
    dispatch: (TableEvent.UiEvent) -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.table_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.table_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state) {
                TableUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is TableUiState.Error -> ErrorContent(
                    message = state.message,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center)
                )

                is TableUiState.Content -> {
                    if (state.isEditing) {
                        CellEditDialog(
                            text = state.editingText,
                            onTextChange = { dispatch(TableEvent.UiEvent.EditingTextChanged(it)) },
                            onConfirm = { dispatch(TableEvent.UiEvent.EditingDone) },
                            onDismiss = { dispatch(TableEvent.UiEvent.EditingCancelled) }
                        )
                    }
                    LazyTable(
                        state = state,
                        dispatch = dispatch,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = message)
        Button(onClick = onRetry) {
            Text(stringResource(R.string.table_retry))
        }
    }
}

@Composable
private fun LazyTable(
    state: TableUiState.Content,
    dispatch: (TableEvent.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        val columnCount = state.cells.firstOrNull()?.size ?: 0
        val tablet = currentWindowAdaptiveInfoV2().windowSizeClass
            .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
        val columnWidth = columnWidth(columnCount, maxWidth, tablet)
        val totalWidth = columnWidth * columnCount
        val fits = totalWidth <= maxWidth
        val horizontalScrollState = rememberScrollState()

        LazyColumn(
            modifier = if (fits) {
                Modifier
                    .align(Alignment.Center)
                    .width(totalWidth)
                    .fillMaxHeight()
            } else {
                Modifier
                    .fillMaxHeight()
                    .horizontalScroll(horizontalScrollState)
            }
        ) {
            items(state.cells.size) { rowIndex ->
                val row = state.cells[rowIndex]
                Row(
                    Modifier
                        .height(IntrinsicSize.Max)
                        .heightIn(min = MinRowHeight)
                ) {
                    row.forEach { cell ->
                        TableCell(
                            cell = cell,
                            columnWidth = columnWidth,
                            dispatch = dispatch
                        )
                    }
                }
            }
        }
    }
}

private fun columnWidth(columnCount: Int, availableWidth: Dp, tablet: Boolean): Dp {
    val perColumn = availableWidth / columnCount
    return if (tablet) {
        perColumn.coerceIn(MinColumnWidth, MaxColumnWidth)
    } else {
        perColumn.coerceAtLeast(MinColumnWidth)
    }
}

@Composable
private fun TableCell(
    cell: Cell,
    columnWidth: Dp,
    dispatch: (TableEvent.UiEvent) -> Unit
) {
    val background = when {
        cell.isEditing -> MaterialTheme.colorScheme.surfaceVariant
        cell.isHighlighted -> HighlightColor
        else -> MaterialTheme.colorScheme.surface
    }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(columnWidth)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
            .background(background)
            .pointerInput(cell.row, cell.column) {
                detectTapGestures(
                    onTap = {
                        dispatch(TableEvent.UiEvent.CellClicked(cell.row, cell.column))
                    },
                    onDoubleTap = {
                        dispatch(TableEvent.UiEvent.CellDoubleClicked(cell.row, cell.column))
                    }
                )
            }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = cell.text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun CellEditDialog(
    text: String,
    onTextChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.table_edit_title)) },
        text = {
            TextField(
                value = text,
                onValueChange = onTextChange
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.table_edit_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.table_edit_cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
internal fun TableScreenContentPreview() {
    IDTTableTheme(dynamicColor = false) {
        TableScreenContent(
            state = TableUiState.Content(
                cells = persistentListOf(
                    persistentListOf(
                        Cell("хаха", row = 0, column = 0, isHighlighted = true, isEditing = false),
                        Cell("хехе", row = 0, column = 1, isHighlighted = false, isEditing = false),
                        Cell("хохо", row = 0, column = 2, isHighlighted = false, isEditing = false)
                    ),
                    persistentListOf(
                        Cell(
                            "хаханьки",
                            row = 1,
                            column = 0,
                            isHighlighted = false,
                            isEditing = false
                        ),
                        Cell(
                            "хехеньки",
                            row = 1,
                            column = 1,
                            isHighlighted = false,
                            isEditing = false
                        ),
                        Cell(
                            "хохоньки",
                            row = 1,
                            column = 2,
                            isHighlighted = false,
                            isEditing = false
                        )
                    )
                )
            ),
            dispatch = {},
            onRetry = {},
            onBack = {}
        )
    }
}
