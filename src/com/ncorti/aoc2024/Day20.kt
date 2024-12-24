package com.ncorti.aoc2024

import kotlin.math.abs

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }.map { it.toCharArray() }
    }.toTypedArray()

    fun manhattanDistance(a: Pair<Int, Int>, b: Pair<Int, Int>) = abs(a.first - b.first) + abs(a.second - b.second)

    fun part(inputFile: String, threshold: Int, part2: Boolean = false): Int {
        val map = getInput(inputFile)
        var beg = (0 to 0)
        var end = (0 to 0)
        map.forEachIndexed { x, line ->
            line.forEachIndexed { y, char ->
                when (char) {
                    'S' -> beg = x to y
                    'E' -> end = x to y
                }
            }
        }
        val path = mutableListOf(beg)
        while (true) {
            if (path.last() == end) break
            val (x, y) = path.last()
            if (x - 1 >= 0 && (map[x - 1][y] == '.' || map[x - 1][y] == 'E') && !path.contains(x - 1 to y)) {
                path.add(x - 1 to y)
            } else if (y - 1 >= 0 && (map[x][y - 1] == '.' || map[x][y - 1] == 'E') && !path.contains(x to y - 1)) {
                path.add(x to y - 1)
            } else if (x + 1 < map.size && (map[x + 1][y] == '.' || map[x + 1][y] == 'E') && !path.contains(x + 1 to y)) {
                path.add(x + 1 to y)
            } else if (y + 1 < map.size && (map[x][y + 1] == '.' || map[x][y + 1] == 'E') && !path.contains(x to y + 1)) {
                path.add(x to y + 1)
            }
        }
        val cheatRadius = if (part2) 20 else 2
        return path.indices.sumOf { start ->
            (start + threshold..path.lastIndex).count { end ->
                val physicalDistance = manhattanDistance(path[start], path[end])
                physicalDistance <= cheatRadius && physicalDistance <= end - start - threshold
            }
        }
    }

    println(part("20-sample", threshold = 50, part2 = false))
    println(part("20", threshold = 100, part2 = false))
    println(part("20-sample", threshold = 50, part2 = true))
    println(part("20", threshold = 100, part2 = true))
}
