package com.badmanners.idttable.data.mapper

import com.badmanners.idttable.data.model.TableDataDto
import com.badmanners.idttable.domain.model.TableDataDomain
import com.badmanners.idttable.domain.model.TableDataDomain.TableRowDomain
import com.badmanners.idttable.domain.model.TableDataDomain.TableRowDomain.TableCellDomain
import javax.inject.Inject

class TableDataDtoMapperImpl @Inject constructor() : TableDataDtoMapper {

    override fun toDomain(dto: TableDataDto): TableDataDomain =
        TableDataDomain(
            rows = dto.values.map { rowValues ->
                TableRowDomain(
                    cells = rowValues.map { value -> TableCellDomain(text = value) }
                )
            }
        )
}