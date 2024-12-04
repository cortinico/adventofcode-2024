package com.ncorti.aoc2024

fun main() {

    val directionsP1 = listOf(
        listOf(Triple(1, 0, 'M'), Triple(2, 0, 'A'), Triple(3, 0, 'S')),
        listOf(Triple(-1, 0, 'M'), Triple(-2, 0, 'A'), Triple(-3, 0, 'S')),
        listOf(Triple(0, 1, 'M'), Triple(0, 2, 'A'), Triple(0, 3, 'S')),
        listOf(Triple(0, -1, 'M'), Triple(0, -2, 'A'), Triple(0, -3, 'S')),
        listOf(Triple(1, 1, 'M'), Triple(2, 2, 'A'), Triple(3, 3, 'S')),
        listOf(Triple(-1, 1, 'M'), Triple(-2, 2, 'A'), Triple(-3, 3, 'S')),
        listOf(Triple(1, -1, 'M'), Triple(2, -2, 'A'), Triple(3, -3, 'S')),
        listOf(Triple(-1, -1, 'M'), Triple(-2, -2, 'A'), Triple(-3, -3, 'S')),
    )

    fun foundXmas(input: Array<CharArray>, i: Int, j: Int): Int =
        directionsP1.count { (s1, s2, s3) ->
            i + s1.first >= 0 && i + s1.first < input.size &&
                i + s2.first >= 0 && i + s2.first < input.size &&
                i + s3.first >= 0 && i + s3.first < input.size &&
                j + s1.second >= 0 && j + s1.second < input.size &&
                j + s2.second >= 0 && j + s2.second < input.size &&
                j + s3.second >= 0 && j + s3.second < input.size &&
                input[i + s1.first][j + s1.second] == s1.third &&
                input[i + s2.first][j + s2.second] == s2.third &&
                input[i + s3.first][j + s3.second] == s3.third
        }

    fun part1(): Int {
        val input = getInputAsText("04") {
            split("\n").dropLast(1).map { it.toCharArray() }
        }.toTypedArray()
        return input.mapIndexed { i, row ->
            row.mapIndexed { j, c ->
                if (c == 'X') foundXmas(input, i, j) else 0
            }.sum()
        }.sum()
    }

    fun foundMas(input: Array<CharArray>, i: Int, j: Int): Boolean =
        (input[i-1][j-1] == 'M' && input[i+1][j+1] == 'S' && input[i+1][j-1] == 'M' && input[i-1][j+1] == 'S') ||
            (input[i-1][j-1] == 'M' && input[i+1][j+1] == 'S' && input[i+1][j-1] == 'S' && input[i-1][j+1] == 'M') ||
            (input[i-1][j-1] == 'S' && input[i+1][j+1] == 'M' && input[i+1][j-1] == 'M' && input[i-1][j+1] == 'S') ||
            (input[i-1][j-1] == 'S' && input[i+1][j+1] == 'M' && input[i+1][j-1] == 'S' && input[i-1][j+1] == 'M')

    fun part2(): Int {
        val input = getInputAsText("04") {
            split("\n").dropLast(1).map { it.toCharArray() }
        }.toTypedArray()

        var count = 0
        for (i in 1 until input.size - 1) {
            for (j in 1 until input[i].size - 1) {
                if (input[i][j] == 'A') {
                    if (foundMas(input, i, j)) count++
                }
            }
        }
        return count
    }

    println(part1())
    println(part2())
}
