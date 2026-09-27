package com.badmanners.idttable.data.repository

import com.badmanners.idttable.data.data_source.RandomStringDataSource
import com.badmanners.idttable.data.mapper.TableDataDtoMapper
import com.badmanners.idttable.data.model.TableDataDto
import com.badmanners.idttable.domain.model.TableDataDomain
import com.badmanners.idttable.domain.model.TableDataDomain.TableRowDomain
import com.badmanners.idttable.domain.model.TableDataDomain.TableRowDomain.TableCellDomain
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class TableRepositoryImplTest {

    private val dataSource = mockk<RandomStringDataSource>()
    private val dtoMapper = mockk<TableDataDtoMapper>()
    private val repository = TableRepositoryImpl(dataSource, dtoMapper)

    @Test
    fun loadTableBuildsDtoAndReturnsMappedResult() = runBlocking {
        val expected = TableDataDomain(
            rows = listOf(
                TableRowDomain(
                    cells = listOf(
                        TableCellDomain(text = "x"),
                        TableCellDomain(text = "x"),
                        TableCellDomain(text = "x")
                    )
                ),
                TableRowDomain(
                    cells = listOf(
                        TableCellDomain(text = "x"),
                        TableCellDomain(text = "x"),
                        TableCellDomain(text = "x")
                    )
                )
            )
        )
        val capturedDto = mutableListOf<TableDataDto>()
        every { dataSource.nextString() } returns "x"
        every { dtoMapper.toDomain(capture(capturedDto)) } returns expected

        val result = repository.loadTable(rows = 2, columns = 3)

        assertEquals(expected, result)
        verify(exactly = 6) { dataSource.nextString() }

        val dto = capturedDto.single()
        assertEquals(2, dto.values.size)
        dto.values.forEach { row ->
            assertEquals(3, row.size)
            row.forEach { assertEquals("x", it) }
        }
    }

    @Test
    fun loadTableSupportsMaximumValidSize() = runBlocking {
        val capturedDto = mutableListOf<TableDataDto>()
        every { dataSource.nextString() } returns "x"
        every { dtoMapper.toDomain(capture(capturedDto)) } returns TableDataDomain(rows = emptyList())

        repository.loadTable(rows = 1000, columns = 6)

        verify(exactly = 1000 * 6) { dataSource.nextString() }

        val dto = capturedDto.single()
        assertEquals(1000, dto.values.size)
        dto.values.forEach { row -> assertEquals(6, row.size) }
    }
}
