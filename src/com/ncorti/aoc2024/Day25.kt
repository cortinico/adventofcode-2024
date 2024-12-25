package com.ncorti.aoc2024

fun main() {

    fun getInput(inputFile: String): Pair<List<List<Int>>, List<List<Int>>> {
        val raw = getInputAsText(inputFile) {
            split("\n\n")
        }
        val grids = raw.map { it.split("\n").map { it.toCharArray() }.toTypedArray() }
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        grids.forEach { grid ->
            val columnTotals = MutableList(grid[0].size) { 0 }
            for (i in 1 until grid.size - 1) {
                for (j in 0 until grid[0].size) {
                    if (grid[i][j] == '#') columnTotals[j]++
                }
            }
            if (grid[0].all { it == '#' }) {
                locks.add(columnTotals)
            } else if (grid[0].all { it == '.' }) {
                keys.add(columnTotals)
            } else {
                error("Invalid input")
            }
        }
        return locks.filter { it.isNotEmpty() } to keys.filter { it.isNotEmpty() }
    }


    fun part1(inputFile: String): Int {
        val (locks, keys) = getInput(inputFile)
        var count = 0
        locks.forEach { lock ->
            keys.forEach { key ->
                val total = Array(lock.size) { lock[it] + key[it] }
                if (total.all { it <= 5 }) count++
            }
        }
        return count
    }

    println(part1("25-sample"))
    println(part1("25"))
}
