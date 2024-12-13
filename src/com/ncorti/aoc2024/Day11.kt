package com.ncorti.aoc2024

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }.map { line ->
            line.split(" ").map { it.toLong() }
        }
    }.first().toMutableList()

    fun part(input: String, iterations: Int): Long {
        var map = getInput(input).associateWith { 1L }.toMutableMap()
        repeat(iterations) {
            val newMap = mutableMapOf<Long, Long>()
            map.forEach { (key, _) ->
                val prevValue = map[key]!!
                when {
                    key == 0L -> newMap[1] = newMap.getOrDefault(1, 0L) + prevValue
                    key.toString().length % 2 == 0 -> {
                        val (first, second) = key.toString().chunked(key.toString().length / 2)
                        newMap[first.toLong()] = newMap.getOrDefault(first.toLong(), 0L) + prevValue
                        newMap[second.toLong()] = newMap.getOrDefault(second.toLong(), 0L) + prevValue
                    }

                    else -> newMap[key * 2024L] = newMap.getOrDefault(key * 2024L, 0) + prevValue
                }
            }
            map = newMap
        }
        return map.values.sum()
    }

    println(part("11-sample", iterations = 25))
    println(part("11", iterations = 25))
    println(part("11-sample", iterations = 75))
    println(part("11", iterations = 75))
}
