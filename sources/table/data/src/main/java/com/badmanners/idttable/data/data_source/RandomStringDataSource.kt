package com.badmanners.idttable.data.data_source

import javax.inject.Inject
import kotlin.random.Random

class RandomStringDataSource @Inject constructor(
    private val random: Random = Random.Default,
    private val alphabet: CharArray = DEFAULT_ALPHABET,
    private val minLength: Int = 6,
    private val maxLength: Int = 10
) {

    private companion object {
        val DEFAULT_ALPHABET = (('A'..'Z') + ('a'..'z') + ('0'..'9'))
            .joinToString("")
            .toCharArray()
    }

    fun nextString(): String {
        val length = random.nextInt(minLength, maxLength + 1)
        return buildString(length) {
            repeat(length) { append(alphabet[random.nextInt(alphabet.size)]) }
        }
    }
}
