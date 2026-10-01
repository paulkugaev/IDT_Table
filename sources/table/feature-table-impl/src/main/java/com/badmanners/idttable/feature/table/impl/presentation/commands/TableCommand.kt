package com.badmanners.idttable.feature.table.impl.presentation.commands

sealed interface TableCommand {

    data class Load(val rows: Int, val columns: Int) : TableCommand
}