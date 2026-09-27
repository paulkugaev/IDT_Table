package com.badmanners.idttable.domain.model

data class TableDataDomain(
    val rows: List<TableRowDomain>
) {
    data class TableRowDomain(
        val cells: List<TableCellDomain>
    ) {
        data class TableCellDomain(
            val text: String
        )
    }
}