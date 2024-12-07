package com.ncorti.aoc2024

fun main() {
    fun performMove(map: Array<CharArray>, seen: Array<IntArray>) {
        val x = map.indexOfFirst { it.contains('v') || it.contains('^') || it.contains('>') || it.contains('<') }
        val y = map[x].indexOfFirst { it == 'v' || it == '^' || it == '>' || it == '<' }
        seen[x][y] = 1
        val current = map[x][y]
        when (current) {
            'v' -> {
                if (map[x + 1][y] == '.') {
                    map[x][y] = '.'
                    map[x + 1][y] = 'v'
                } else {
                    map[x][y] = '<'
                }
            }

            '^' -> {
                if (map[x - 1][y] == '.') {
                    map[x][y] = '.'
                    map[x - 1][y] = '^'
                } else {
                    map[x][y] = '>'
                }
            }

            '>' -> {
                if (map[x][y + 1] == '.') {
                    map[x][y] = '.'
                    map[x][y + 1] = '>'
                } else {
                    map[x][y] = 'v'
                }
            }

            '<' -> {
                if (map[x][y - 1] == '.') {
                    map[x][y] = '.'
                    map[x][y - 1] = '<'
                } else {
                    map[x][y] = '^'
                }
            }
        }
    }

    fun isAtTheBorder(map: Array<CharArray>): Boolean {
        if (map.first().contains('^') || map.last().contains('v')) {
            return true
        }
        map.forEach {
            if (it.first() == '<' || it.last() == '>') {
                return true
            }
        }
        return false
    }

    fun part1(): Int {
        val map = getInputAsText("06") {
            split("\n").filter { it.isNotBlank() }.map { it.toCharArray() }
        }.toTypedArray()
        val seen = Array(map.size) {
            IntArray(map.size) { 0 }
        }
        var count = 0
        var end = false
        while (!end) {
            count++
            performMove(map, seen)
            if (isAtTheBorder(map)) {
                end = true
            }
        }

        return seen.sumOf { it.sum() } + 1
    }

    fun part2(): Int {
        val map = getInputAsText("06") {
            split("\n").filter { it.isNotBlank() }.map { it.toCharArray() }
        }.toTypedArray()
        val seen = Array(map.size) {
            IntArray(map.size) { 0 }
        }
        var count = 0
        for (i in map.indices) {
            for (j in map.indices) {
                if (map[i][j] != '.') continue
                val newMap = map.map { it.copyOf() }.toTypedArray()
                newMap[i][j] = '#'
                val steps = mutableSetOf<Triple<Char, Int, Int>>()
                while (true) {
                    val x = newMap.indexOfFirst {
                        it.contains('v') || it.contains('^') || it.contains('>') || it.contains('<')
                    }
                    val y = newMap[x].indexOfFirst { it == 'v' || it == '^' || it == '>' || it == '<' }
                    val currentStep = Triple(newMap[x][y], x, y)
                    if (currentStep in steps) {
                        count++
                        break
                    } else {
                        steps.add(currentStep)
                    }
                    performMove(newMap, seen)
                    if (isAtTheBorder(newMap)) {
                        break
                    }
                }
            }
        }
        return count
    }

    println(part1())
    println(part2())
}
