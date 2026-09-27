package com.badmanners.idttable.data.mapper

import com.badmanners.idttable.data.model.TableDataDto
import org.junit.Assert.assertEquals
import org.junit.Test

class TableDataDtoMapperTest {

    private val mapper: TableDataDtoMapper = TableDataDtoMapperImpl()

    @Test
    fun mapsDimensionsFromValues() {
        val dto = TableDataDto(
            values = listOf(
                listOf("a", "b", "c"),
                listOf("d", "e", "f")
            )
        )

        val domain = mapper.toDomain(dto)

        assertEquals(2, domain.rows.size)
        assertEquals(3, domain.rows.first().cells.size)
    }

    @Test
    fun mapsEachValueToACell() {
        val dto = TableDataDto(
            values = listOf(
                listOf("a", "b"),
                listOf("c", "d")
            )
        )

        val domain = mapper.toDomain(dto)

        assertEquals("a", domain.rows[0].cells[0].text)
        assertEquals("b", domain.rows[0].cells[1].text)
        assertEquals("c", domain.rows[1].cells[0].text)
        assertEquals("d", domain.rows[1].cells[1].text)
    }
}