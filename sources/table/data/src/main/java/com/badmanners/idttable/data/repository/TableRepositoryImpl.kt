package com.badmanners.idttable.data.repository

import com.badmanners.idttable.data.data_source.RandomStringDataSource
import com.badmanners.idttable.data.mapper.TableDataDtoMapper
import com.badmanners.idttable.data.model.TableDataDto
import com.badmanners.idttable.domain.model.TableDataDomain
import com.badmanners.idttable.domain.repository.TableRepository
import javax.inject.Inject

class TableRepositoryImpl @Inject constructor(
    private val dataSource: RandomStringDataSource,
    private val dtoMapper: TableDataDtoMapper
) : TableRepository {

    override suspend fun loadTable(rows: Int, columns: Int): TableDataDomain {
        // We assume the data source (e.g. a backend) always returns a valid grid for the requested
        // size, so the response is mapped as-is without defensive validation.
        val dto = TableDataDto(
            values = List(rows) { List(columns) { dataSource.nextString() } }
        )
        return dtoMapper.toDomain(dto)
    }
}