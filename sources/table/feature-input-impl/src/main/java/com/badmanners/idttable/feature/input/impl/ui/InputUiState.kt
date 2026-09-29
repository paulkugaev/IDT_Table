package com.badmanners.idttable.feature.input.impl.ui

data class InputUiState(
    val rows: String,
    val columns: String,
    val rowsError: String?,
    val columnsError: String?,
    val buildEnabled: Boolean
)