package com.ncorti.aoc2024

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }.map {
            it.split(" v=", "p=", ",").filter { runCatching { it.toInt() }.isSuccess }.map { it.toInt() }
                .toMutableList()
        }
    }

    fun performStep(input: List<MutableList<Int>>, map: Array<IntArray>, rows: Int, columns: Int) {
        for (i in input.indices) {
            val line = input[i]
            val (currx, curry, stepx, stepy) = line
            map[curry][currx]--
            var nextx = (currx + stepx) % columns
            while (nextx < 0) nextx += columns
            var nexty = (curry + stepy) % rows
            while (nexty < 0) nexty += rows
            map[nexty][nextx]++
            line[0] = nextx
            line[1] = nexty
        }
    }

    fun part(inputFile: String, rows: Int, columns: Int, part2: Boolean): Long {
        val input = getInput(inputFile)
        val map = Array(rows) {
            IntArray(columns) { 0 }
        }
        input.forEach { (x, y, _, _) ->
            map[y][x]++
        }
        var iter = 1L
        while (true) {
            performStep(input, map, rows, columns)
            var found = true
            for (i in (rows / 2) - 3 until (rows / 2) + 3) {
                for (j in (columns / 2) - 3 until (columns / 2) + 3) {
                    if (map[i][j] != 1) {
                        found = false
                    }
                }
            }
            if (part2 && found || !part2 && iter == 10000L) {
                break
            }
            iter++
        }
        var q1 = 0L
        for (i in 0 until rows / 2) {
            for (j in 0 until columns / 2) {
                q1 += map[i][j]
            }
        }
        var q2 = 0L
        for (i in (rows / 2) + 1 until rows) {
            for (j in (columns / 2) + 1 until columns) {
                q2 += map[i][j]
            }
        }
        var q3 = 0L
        for (i in (rows / 2) + 1 until rows) {
            for (j in 0 until columns / 2) {
                q3 += map[i][j]
            }
        }
        var q4 = 0L
        for (i in 0 until rows / 2) {
            for (j in (columns / 2) + 1 until columns) {
                q4 += map[i][j]
            }
        }
        return if (!part2) {
            q1 * q2 * q3 * q4
        } else {
            iter
        }
    }

    println(part("14-sample", rows = 7, columns = 11, part2 = false))
    println(part("14", rows = 103, columns = 101, part2 = false))
    println(part("14", rows = 103, columns = 101, part2 = true))
}
