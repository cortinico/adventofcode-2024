package com.ncorti.aoc2024

import java.util.*

data class GraphVisit18(val x: Int, val y: Int, val distance: Int)

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }.map {
            val (t1, t2) = it.split(",").map { it.toInt() }
            t2 to t1
        }
    }

    fun shortestPath(map: Array<IntArray>, startx: Int, starty: Int, endx: Int, endy: Int): Int {
        val distanceMap = Array(map.size) { IntArray(map.size) { Int.MAX_VALUE } }
        distanceMap[0][0] = 0
        val queue = PriorityQueue<GraphVisit18> { a, b -> a.distance - b.distance }
        queue.add(GraphVisit18(startx, starty, 0))
        map.forEachIndexed { i, line ->
            line.forEachIndexed { j, _ ->
                if (i != 0 || j != 0) {
                    queue.add(GraphVisit18(i, j, Int.MAX_VALUE))
                }
            }
        }

        while (queue.isNotEmpty()) {
            val next = queue.poll()
            val (x, y, dist) = next
            distanceMap[x][y] = dist
            if (x == endx && y == endy) {
                if (dist == Int.MAX_VALUE) {
                    error("Cannot find min path")
                }
                return dist
            }
            val neighbors = listOf(
                x to y - 1, x to y + 1, x - 1 to y, x + 1 to y
            )
            neighbors.forEach { neighbor ->
                val (nx, ny) = neighbor
                if (nx in map.indices && ny in map.indices && map[nx][ny] == 0) {
                    val newDist = dist + 1
                    if (newDist < distanceMap[nx][ny]) {
                        queue.removeIf { it.x == nx && it.y == ny }
                        queue.add(GraphVisit18(nx, ny, newDist))
                    }
                }
            }
        }
        error("Shortest path not found")
    }

    fun part1(inputFile: String, size: Int, limit: Int): Int {
        val input = getInput(inputFile).filterIndexed { index, pair -> index < limit }
        val map = Array(size) { IntArray(size) { 0 } }
        input.forEach { map[it.first][it.second] = 1 }
        return shortestPath(map, 0, 0, size - 1, size - 1)
    }

    fun part2(inputFile: String, size: Int, limit: Int): String {
        val input = getInput(inputFile)
        val firstBytes = input.filterIndexed { index, pair -> index < limit }
        val remainingBytes = input.filterIndexed { index, pair -> index >= limit }

        val map = Array(size) { IntArray(size) { 0 } }
        firstBytes.forEach { map[it.first][it.second] = 1 }
        remainingBytes.forEachIndexed { _, (x, y) ->
            map[x][y] = 1
            val canFindPath = shortestPath(map, 0, 0, size - 1, size - 1)
            if (canFindPath == Int.MAX_VALUE || canFindPath < 0) return "$y,$x"
        }
        error("There is always a path")
    }

    println(part1("18-sample", size = 7, limit = 12))
    println(part1("18", size = 71, limit = 1024))
    println(part2("18-sample", size = 7, limit = 12))
    println(part2("18", size = 71, limit = 1024))
}
