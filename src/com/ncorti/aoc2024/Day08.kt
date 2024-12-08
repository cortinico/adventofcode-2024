package com.ncorti.aoc2024

fun main() {
    fun getInput() = getInputAsText("08") {
        split("\n").filter { it.isNotBlank() }.map { it.toCharArray() }
    }

    fun part1(): Int {
        val map = getInput()
        val chars = map.flatMap {
            it.distinct()
        }.distinct().filter { it != '.' }
        val antinodes = Array(map.size) {
            IntArray(map.size) { 0 }
        }

        chars.forEach { char ->
            val charLocations = mutableListOf<Pair<Int, Int>>()
            map.forEachIndexed { i, line ->
                line.forEachIndexed { j, c ->
                    if (map[i][j] == char) {
                        charLocations.add(i to j)
                    }
                }
            }
            for (i in charLocations.indices) {
                for (j in i + 1 until charLocations.size) {
                    val (x1, y1) = charLocations[i]
                    val (x2, y2) = charLocations[j]
                    val (xa1, ya1) = x1 + (x1 - x2) to y1 + (y1 - y2)
                    val (xa2, ya2) = x2 + (x2 - x1) to y2 + (y2 - y1)
                    if (xa1 >= 0 && xa1 < antinodes.size && ya1 >= 0 && ya1 < antinodes.size) {
                        antinodes[xa1][ya1] = 1
                    }
                    if (xa2 >= 0 && xa2 < antinodes.size && ya2 >= 0 && ya2 < antinodes.size) {
                        antinodes[xa2][ya2] = 1
                    }
                }
            }
        }
        return antinodes.sumOf { it.sum() }
    }

    fun part2(): Int {
        val map = getInput()
        val chars = map.flatMap {
            it.distinct()
        }.distinct().filter { it != '.' }
        val antinodes = Array(map.size) {
            IntArray(map.size) { 0 }
        }

        chars.forEach { char ->
            val charLocations = mutableListOf<Pair<Int, Int>>()
            map.forEachIndexed { i, line ->
                line.forEachIndexed { j, c ->
                    if (map[i][j] == char) {
                        charLocations.add(i to j)
                    }
                }
            }
            for (i in charLocations.indices) {
                for (j in i + 1 until charLocations.size) {
                    val (x1, y1) = charLocations[i]
                    val (x2, y2) = charLocations[j]
                    var (xa1, ya1) = x1 + (x1 - x2) to y1 + (y1 - y2)
                    var (xa2, ya2) = x2 + (x2 - x1) to y2 + (y2 - y1)
                    antinodes[x1][y1] = 1
                    antinodes[x2][y2] = 1
                    while (xa1 >= 0 && xa1 < antinodes.size && ya1 >= 0 && ya1 < antinodes.size) {
                        antinodes[xa1][ya1] = 1
                        xa1 += (x1 - x2)
                        ya1 += (y1 - y2)
                    }
                    while (xa2 >= 0 && xa2 < antinodes.size && ya2 >= 0 && ya2 < antinodes.size) {
                        antinodes[xa2][ya2] = 1
                        xa2 += (x2 - x1)
                        ya2 += (y2 - y1)
                    }
                }
            }
        }
        return antinodes.sumOf { it.sum() }
    }

    println(part1())
    println(part2())
}
