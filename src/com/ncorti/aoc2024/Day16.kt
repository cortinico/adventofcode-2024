package com.ncorti.aoc2024

import java.util.*

data class Distance(
    val x: Int, val y: Int, val distance: Long, val direction: Char, val seen: List<Pair<Int, Int>> = listOf()
)

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }.map {
            it.toCharArray()
        }
    }.toTypedArray()

    fun part(inputFile: String, part2: Boolean = false): Long {
        val map = getInput(inputFile)
        val minHipQueue = PriorityQueue<Distance>(compareBy { it.distance })
        val paths = mutableSetOf<Pair<Int, Int>>()
        val scores = mutableMapOf<Triple<Int, Int, Char>, Long>().withDefault { Long.MAX_VALUE }
        var lowestSoFar = Long.MAX_VALUE
        map.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                if (c == 'S') {
                    minHipQueue.add(Distance(i, j, 0L, direction = '>'))
                }
            }
        }

        while (minHipQueue.isNotEmpty()) {
            val next = minHipQueue.poll()
            val (x, y, distance, direction) = next
            if (distance > scores.getValue(Triple(x,y,direction))) continue
            scores[Triple(x,y,direction)] = distance

            if (map[x][y] == 'E') {
                if (distance > lowestSoFar) break
                paths.addAll(next.seen)
                lowestSoFar = distance
            }
            when (direction) {
                '>' -> {
                    if (y + 1 < map.size && map[x][y + 1] != '#') {
                        minHipQueue.add(Distance(x, y + 1, distance + 1L, '>', next.seen + (x to y)))
                    }
                    if (x + 1 < map.size && map[x + 1][y] != '#') {
                        minHipQueue.add(Distance(x + 1, y, distance + 1001L, direction = 'v', seen = next.seen + (x to y)))
                    }
                    if (x - 1 >= 0 && map[x - 1][y] != '#') {
                        minHipQueue.add(Distance(x - 1, y, distance + 1001L, direction = '^', seen = next.seen + (x to y)))
                    }
                }

                '<' -> {
                    if (y - 1 >= 0 && map[x][y - 1] != '#') {
                        minHipQueue.add(Distance(x, y - 1, distance + 1L, direction = '<', seen = next.seen + (x to y)))
                    }
                    if (x + 1 < map.size && map[x + 1][y] != '#') {
                        minHipQueue.add(Distance(x + 1, y, distance + 1001L, direction = 'v', seen = next.seen + (x to y)))
                    }
                    if (x - 1 >= 0 && map[x - 1][y] != '#') {
                        minHipQueue.add(Distance(x - 1, y, distance + 1001L, direction = '^', seen = next.seen + (x to y)))
                    }
                }

                'v' -> {
                    if (x + 1 < map.size && map[x + 1][y] != '#') {
                        minHipQueue.add(Distance(x + 1, y, distance + 1L, direction = 'v', seen = next.seen + (x to y)))
                    }
                    if (y + 1 < map.size && map[x][y + 1] != '#') {
                        minHipQueue.add(Distance(x, y + 1, distance + 1001L, direction = '>', seen = next.seen + (x to y)))
                    }
                    if (y - 1 >= 0 && map[x][y - 1] != '#') {
                        minHipQueue.add(Distance(x, y - 1, distance + 1001L, direction = '<', seen = next.seen + (x to y)))
                    }
                }

                '^' -> {
                    if (y + 1 < map.size && map[x][y + 1] != '#') {
                        minHipQueue.add(Distance(x, y + 1, distance + 1001L, direction = '>', seen = next.seen + (x to y)))
                    }
                    if (y - 1 >= 0 && map[x][y - 1] != '#') {
                        minHipQueue.add(Distance(x, y - 1, distance + 1001L, direction = '<', seen = next.seen + (x to y)))
                    }
                    if (x - 1 >= 0 && map[x - 1][y] != '#') {
                        minHipQueue.add(Distance(x - 1, y, distance + 1L, direction = '^', seen = next.seen + (x to y)))
                    }
                }
            }
        }
        return if (part2) {
            paths.size.toLong() + 1
        } else {
            lowestSoFar
        }
    }

    println(part("16-sample"))
    println(part("16"))
    println(part("16-sample", part2 = true))
    println(part("16", part2 = true))
}
