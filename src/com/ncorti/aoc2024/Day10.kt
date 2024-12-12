package com.ncorti.aoc2024

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }.map { line ->
            line.toCharArray().map { it.toString().toInt() }.toIntArray()
        }
    }.toTypedArray()

    val directions = listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)

    fun computeScore(map: Array<IntArray>, startx: Int, starty: Int, distinct: Boolean) : Int {
        var score = 0
        val visitNext = mutableListOf(startx to starty)
        val seen = mutableListOf<Pair<Int,Int>>()
        while(visitNext.isNotEmpty()) {
            val (x, y) = visitNext.removeFirst()
            val currentValue = map[x][y]
            if (currentValue == 9) {
                if ((!distinct && !seen.contains(x to y)) || distinct) {
                    score++
                    seen.add(x to y)
                }
            } else {
                directions.forEach { (px, py) ->
                    val nx = x + px
                    val ny = y + py
                    if (nx >= 0 && nx < map.size && ny >= 0 && ny < map.size && map[nx][ny] == currentValue + 1) {
                        visitNext.add(nx to ny)
                    }
                }
            }
        }
        return score
    }

    fun part1(inputFile: String): Int {
        val map = getInput(inputFile)
        val trailheads = mutableListOf<Pair<Int,Int>>()
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] == 0) trailheads.add(i to j)
            }
        }
        return trailheads.sumOf { (x,y) ->
            computeScore(map, x, y, distinct = false)
        }
    }

    fun part2(inputFile: String): Int {
        val map = getInput(inputFile)
        val trailheads = mutableListOf<Pair<Int,Int>>()
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] == 0) trailheads.add(i to j)
            }
        }
        return trailheads.sumOf { (x,y) ->
            computeScore(map, x, y, distinct = true)
        }
    }

    println(part1("10-sample"))
    println(part1("10"))
    println(part2("10-sample"))
    println(part2("10"))
}
