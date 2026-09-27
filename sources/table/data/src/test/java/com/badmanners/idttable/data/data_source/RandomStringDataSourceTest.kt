package com.badmanners.idttable.data.data_source

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class RandomStringDataSourceTest {

    private val alphabet = (('A'..'Z') + ('a'..'z') + ('0'..'9')).toCharArray()

    @Test
    fun generatedLengthIsWithinInclusiveBounds() {
        val dataSource = RandomStringDataSource(random = Random(1), minLength = 6, maxLength = 10)

        repeat(200) {
            val text = dataSource.nextString()
            assertTrue("length=${text.length}", text.length in 6..10)
        }
    }

    @Test
    fun fixedMinMaxLengthProducesExactLength() {
        val dataSource = RandomStringDataSource(random = Random(3), minLength = 8, maxLength = 8)

        repeat(50) {
            assertEquals(8, dataSource.nextString().length)
        }
    }

    @Test
    fun generatedCharsComeFromAlphabet() {
        val dataSource = RandomStringDataSource(random = Random(2))

        repeat(200) {
            val text = dataSource.nextString()
            assertTrue(text.isNotEmpty())
            assertTrue(text.all { it in alphabet })
        }
    }
}