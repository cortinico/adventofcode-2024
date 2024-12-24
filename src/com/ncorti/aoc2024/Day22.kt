package com.ncorti.aoc2024

fun main() {

    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }.map { it.toLong() }
    }

    fun computeNextSecretNumber(start: Long): Long {
        val result = start * 64L
        val step1 = (start xor result) % 16777216L
        val result2 = step1 / 32L
        val step2 = (step1 xor result2) % 16777216L
        val result3 = step2 * 2048L
        return (step2 xor result3) % 16777216L
    }

    data class Sequence(val n1: Int, val n2: Int, val n3: Int, val n4: Int)

    fun part1(inputFile: String): Long {
        val input = getInput(inputFile).toMutableList()
        for (i in input.indices) {
            repeat(2000) {
                input[i] = computeNextSecretNumber(input[i])
            }
        }
        return input.sum()
    }

    fun part2(inputFile: String): Long {
        val input = getInput(inputFile).toMutableList()
        val digits = Array(input.size) { mutableListOf<Int>() }
        val maps = Array(input.size) { mutableMapOf<Sequence, Long>() }
        input.forEachIndexed { index, number ->
            digits[index].add((number % 10L).toInt())
        }
        for (i in input.indices) {
            repeat(2000) {
                input[i] = computeNextSecretNumber(input[i])
                digits[i].add((input[i] % 10L).toInt())
            }
            for (j in 4 until digits[i].size) {
                val sequence = Sequence(
                    digits[i][j - 3] - digits[i][j - 4],
                    digits[i][j - 2] - digits[i][j - 3],
                    digits[i][j - 1] - digits[i][j - 2],
                    digits[i][j] - digits[i][j - 1]
                )
                if (sequence !in maps[i]) {
                    maps[i][sequence] = digits[i][j].toLong()
                }
            }
        }
        var mostBananas = 0L
        val keySet = mutableSetOf<Sequence>()
        maps.forEach { keySet.addAll(it.keys) }
        keySet.forEach { key ->
            val bananas = maps.sumOf { map -> map[key] ?: 0L }
            if (bananas > mostBananas) mostBananas = bananas
        }
        return mostBananas
    }

    println(part1("22-sample"))
    println(part1("22"))
    println(part2("22-sample"))
    println(part2("22"))
}
