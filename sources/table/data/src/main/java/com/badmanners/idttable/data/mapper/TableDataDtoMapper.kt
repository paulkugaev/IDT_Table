package com.badmanners.idttable.data.mapper

import com.badmanners.idttable.data.model.TableDataDto
import com.badmanners.idttable.domain.model.TableDataDomain

internal interface TableDataDtoMapper {
    fun toDomain(dto: TableDataDto): TableDataDomain
}