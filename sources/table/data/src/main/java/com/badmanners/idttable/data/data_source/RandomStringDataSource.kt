package com.badmanners.idttable.data.data_source

import kotlin.random.Random

internal class RandomStringDataSource(
    private val random: Random = Random.Default,
    private val alphabet: CharArray = (('A'..'Z') + ('a'..'z') + ('0'..'9')).joinToString("")
        .toCharArray(),
    private val minLength: Int = 6,
    private val maxLength: Int = 10
) {

    fun nextString(): String {
        val length = random.nextInt(minLength, maxLength + 1)
        return buildString(length) {
            repeat(length) { append(alphabet[random.nextInt(alphabet.size)]) }
        }
    }
}
