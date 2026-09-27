package com.badmanners.idttable.domain.repository

import com.badmanners.idttable.domain.model.TableDataDomain

interface TableRepository {
    suspend fun loadTable(rows: Int, columns: Int): TableDataDomain
}