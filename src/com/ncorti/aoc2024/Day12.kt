package com.ncorti.aoc2024

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }.map { line ->
            line.toCharArray()
        }
    }.toTypedArray()

    val directions = listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)

    fun countSides(areaTrace: Array<BooleanArray>): Long {
        var sides = 0L
        val topRow = areaTrace.first()
        var i = 0
        while (i < topRow.size) {
            if (topRow[i]) {
                sides++
                while (i < topRow.size && topRow[i]) i++
            } else {
                i++
            }
        }
        val bottomRow = areaTrace.last()
        i = 0
        while (i < bottomRow.size) {
            if (bottomRow[i]) {
                sides++
                while (i < bottomRow.size && bottomRow[i]) i++
            } else {
                i++
            }
        }
        // Left row
        i = 0
        while (i < areaTrace.size) {
            if (areaTrace[i].first()) {
                sides++
                while (i < areaTrace.size && areaTrace[i].first()) i++
            } else {
                i++
            }
        }
        // Right row
        i = 0
        while (i < areaTrace.size) {
            if (areaTrace[i].last()) {
                sides++
                while (i < areaTrace.size && areaTrace[i].last()) i++
            } else {
                i++
            }
        }

        // Scan top to bottom
        for (i in 1 until areaTrace.size) {
            var j = 0
            while (j < areaTrace.size) {
                if (areaTrace[i][j] && !areaTrace[i - 1][j]) {
                    sides++
                    while (j < areaTrace.size && areaTrace[i][j] && !areaTrace[i - 1][j]) j++
                } else {
                    j++
                }
            }
        }
        // Scan bottom to top
        for (i in areaTrace.size - 2 downTo 0) {
            var j = 0
            while (j < areaTrace.size) {
                if (areaTrace[i][j] && !areaTrace[i + 1][j]) {
                    sides++
                    while (j < areaTrace.size && areaTrace[i][j] && !areaTrace[i + 1][j]) j++
                } else {
                    j++
                }
            }
        }
        // Scan left to right
        for (j in 1 until areaTrace.size) {
            i = 0
            while (i < areaTrace.size) {
                if (areaTrace[i][j] && !areaTrace[i][j - 1]) {
                    sides++
                    while (i < areaTrace.size && areaTrace[i][j] && !areaTrace[i][j - 1]) i++
                } else {
                    i++
                }
            }
        }
        // Scan right to left
        for (j in areaTrace.size - 2 downTo 0) {
            i = 0
            while (i < areaTrace.size) {
                if (areaTrace[i][j] && !areaTrace[i][j + 1]) {
                    sides++
                    while (i < areaTrace.size && areaTrace[i][j] && !areaTrace[i][j + 1]) i++
                } else {
                    i++
                }
            }
        }
        return sides
    }


    fun startVisit(
        map: Array<CharArray>, startx: Int, starty: Int, seen: Array<BooleanArray>, useSides: Boolean = false
    ): Pair<Long, Long> {
        var area = 0L
        var perim = 0L
        var x = startx
        var y = starty
        val char = map[x][y]
        val visitNext = mutableListOf(x to y)
        val areaTrace = Array(map.size) { BooleanArray(map.size) { false } }
        do {
            val next = visitNext.removeFirst()
            x = next.first
            y = next.second
            if (seen[x][y]) {
                continue
            }
            seen[x][y] = true
            areaTrace[x][y] = true
            area++
            directions.forEach { (xi, yi) ->
                if (x + xi >= 0 && x + xi < map.size && y + yi >= 0 && y + yi < map.size) {
                    if (map[x + xi][y + yi] == char) {
                        if (!seen[x + xi][y + yi]) {
                            visitNext.add(x + xi to y + yi)
                        }
                    } else {
                        perim++
                    }
                } else {
                    perim++
                }
            }
        } while (visitNext.isNotEmpty())
        return if (useSides) {
            area to countSides(areaTrace)
        } else {
            area to perim
        }
    }

    fun part(inputFile: String, part2: Boolean = false): Long {
        val map = getInput(inputFile)
        var x = 0
        var y = 0
        val seen = Array(map.size) {
            BooleanArray(map.size) { false }
        }
        var total = 0L
        while (seen.any { it.any { !it } }) {
            if (!seen[x][y]) {
                if (part2) {
                    val (area, sides) = startVisit(map, x, y, seen, useSides = true)
                    total += area * sides
                } else {
                    val (area, perimeter) = startVisit(map, x, y, seen, useSides = part2)
                    total += area * perimeter
                }
            }
            x++
            if (x > map.size - 1) {
                x = 0
                y++
            }
        }
        return total
    }

    println(part("12-sample"))
    println(part("12"))
    println(part("12-sample", part2 = true))
    println(part("12", part2 = true))
}
