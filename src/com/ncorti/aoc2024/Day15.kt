package com.ncorti.aoc2024

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        val lines = split("\n").filter { it.isNotBlank() }
        val steps = lines.last().toCharArray()
        val map = lines.dropLast(1).map {
            it.toCharArray()
        }.toTypedArray()
        map to steps
    }

    fun findRobot(map: Array<CharArray>): Pair<Int, Int> {
        map.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                if (c == '@') return i to j
            }
        }
        error("robot not found")
    }

    fun performStep(map: Array<CharArray>, direction: Char) {
        val (x, y) = findRobot(map)
        when (direction) {
            '<' -> {
                when (map[x][y - 1]) {
                    '.' -> {
                        map[x][y - 1] = '@'
                        map[x][y] = '.'
                    }

                    'O' -> {
                        var nexty = y - 2
                        while (nexty >= 0 && map[x][nexty] == 'O') {
                            nexty--
                        }
                        if (map[x][nexty] == '.') {
                            map[x][nexty] = 'O'
                            map[x][y - 1] = '@'
                            map[x][y] = '.'
                        }
                    }
                }
            }

            '>' -> {
                when (map[x][y + 1]) {
                    '.' -> {
                        map[x][y + 1] = '@'
                        map[x][y] = '.'
                    }

                    'O' -> {
                        var nexty = y + 2
                        while (nexty < map.size && map[x][nexty] == 'O') {
                            nexty++
                        }
                        if (map[x][nexty] == '.') {
                            map[x][nexty] = 'O'
                            map[x][y + 1] = '@'
                            map[x][y] = '.'
                        }
                    }
                }
            }

            '^' -> {
                when (map[x - 1][y]) {
                    '.' -> {
                        map[x - 1][y] = '@'
                        map[x][y] = '.'
                    }

                    'O' -> {
                        var nextx = x - 2
                        while (nextx >= 0 && map[nextx][y] == 'O') {
                            nextx--
                        }
                        if (map[nextx][y] == '.') {
                            map[nextx][y] = 'O'
                            map[x - 1][y] = '@'
                            map[x][y] = '.'
                        }
                    }
                }
            }

            'v' -> {
                when (map[x + 1][y]) {
                    '.' -> {
                        map[x + 1][y] = '@'
                        map[x][y] = '.'
                    }

                    'O' -> {
                        var nextx = x + 2
                        while (nextx < map.size && map[nextx][y] == 'O') {
                            nextx++
                        }
                        if (map[nextx][y] == '.') {
                            map[nextx][y] = 'O'
                            map[x + 1][y] = '@'
                            map[x][y] = '.'
                        }
                    }
                }
            }
        }
    }

    fun enlargeMap(originalMap: Array<CharArray>) {
        originalMap.forEachIndexed { index, line ->
            originalMap[index] = line.flatMap {
                when (it) {
                    '#' -> listOf('#', '#')
                    'O' -> listOf('[', ']')
                    '@' -> listOf('@', '.')
                    else -> listOf(".", ".")
                }
            }.joinToString("").toCharArray()
        }
    }

    fun canMove(map: Array<CharArray>, x: Int, y: Int, up: Boolean): Boolean {
        return if (up) {
            when {
                map[x - 1][y] == '.' && map[x - 1][y + 1] == '.' -> true
                map[x - 1][y] == '[' && map[x - 1][y + 1] == ']' -> canMove(map, x - 1, y, true)
                map[x - 1][y] == ']' && map[x - 1][y + 1] == '.' -> canMove(map, x - 1, y - 1, true)
                map[x - 1][y] == '.' && map[x - 1][y + 1] == '[' -> canMove(map, x - 1, y + 1, true)
                map[x - 1][y] == ']' && map[x - 1][y + 1] == '[' -> canMove(map, x - 1, y - 1, true) && canMove(
                    map,
                    x - 1,
                    y + 1,
                    true
                )

                else -> false
            }
        } else {
            when {
                map[x + 1][y] == '.' && map[x + 1][y + 1] == '.' -> true
                map[x + 1][y] == '[' && map[x + 1][y + 1] == ']' -> canMove(map, x + 1, y, false)
                map[x + 1][y] == ']' && map[x + 1][y + 1] == '.' -> canMove(map, x + 1, y - 1, false)
                map[x + 1][y] == '.' && map[x + 1][y + 1] == '[' -> canMove(map, x + 1, y + 1, false)
                map[x + 1][y] == ']' && map[x + 1][y + 1] == '[' -> canMove(map, x + 1, y - 1, false) && canMove(
                    map,
                    x + 1,
                    y + 1,
                    false
                )

                else -> false
            }
        }
    }

    fun moveMultipleBlocks(map: Array<CharArray>, x: Int, y: Int, up: Boolean) {
        fun moveSingleBlockUp() {
            map[x - 1][y] = '['
            map[x - 1][y + 1] = ']'
            map[x][y] = '.'
            map[x][y + 1] = '.'
        }

        fun moveSingleBlockDown() {
            map[x + 1][y] = '['
            map[x + 1][y + 1] = ']'
            map[x][y] = '.'
            map[x][y + 1] = '.'
        }

        if (up) {
            when {
                map[x - 1][y] == '.' && map[x - 1][y + 1] == '.' -> moveSingleBlockUp()
                map[x - 1][y] == '[' && map[x - 1][y + 1] == ']' -> {
                    moveMultipleBlocks(map, x - 1, y, true)
                    moveSingleBlockUp()
                }

                map[x - 1][y] == ']' && map[x - 1][y + 1] == '.' -> {
                    moveMultipleBlocks(map, x - 1, y - 1, true)
                    moveSingleBlockUp()
                }

                map[x - 1][y] == '.' && map[x - 1][y + 1] == '[' -> {
                    moveMultipleBlocks(map, x - 1, y + 1, true)
                    moveSingleBlockUp()
                }

                map[x - 1][y] == ']' && map[x - 1][y + 1] == '[' -> {
                    moveMultipleBlocks(map, x - 1, y - 1, true)
                    moveMultipleBlocks(map, x - 1, y + 1, true)
                    moveSingleBlockUp()
                }

                else -> error("Invalid map")
            }
        } else {
            when {
                map[x + 1][y] == '.' && map[x + 1][y + 1] == '.' -> moveSingleBlockDown()
                map[x + 1][y] == '[' && map[x + 1][y + 1] == ']' -> {
                    moveMultipleBlocks(map, x + 1, y, false)
                    moveSingleBlockDown()
                }

                map[x + 1][y] == ']' && map[x + 1][y + 1] == '.' -> {
                    moveMultipleBlocks(map, x + 1, y - 1, false)
                    moveSingleBlockDown()
                }

                map[x + 1][y] == '.' && map[x + 1][y + 1] == '[' -> {
                    moveMultipleBlocks(map, x + 1, y + 1, false)
                    moveSingleBlockDown()
                }

                map[x + 1][y] == ']' && map[x + 1][y + 1] == '[' -> {
                    moveMultipleBlocks(map, x + 1, y - 1, false)
                    moveMultipleBlocks(map, x + 1, y + 1, false)
                    moveSingleBlockDown()
                }

                else -> error("Invalid map")
            }
        }
    }

    fun performStepEnlarged(map: Array<CharArray>, direction: Char) {
        val (x, y) = findRobot(map)
        when (direction) {
            '<' -> {
                when (map[x][y - 1]) {
                    '.' -> {
                        map[x][y - 1] = '@'
                        map[x][y] = '.'
                    }

                    ']' -> {
                        var nexty = y - 2
                        while (nexty >= 0 && (map[x][nexty] == ']' || map[x][nexty] == '[')) {
                            nexty--
                        }
                        if (map[x][nexty] == '.') {
                            while (nexty != y - 1) {
                                map[x][nexty] = map[x][nexty + 1]
                                nexty++
                            }
                            map[x][y - 1] = '@'
                            map[x][y] = '.'
                        }
                    }
                }
            }

            '>' -> {
                when (map[x][y + 1]) {
                    '.' -> {
                        map[x][y + 1] = '@'
                        map[x][y] = '.'
                    }

                    '[' -> {
                        var nexty = y + 2
                        while (nexty < map[x].size && (map[x][nexty] == ']' || map[x][nexty] == '[')) {
                            nexty++
                        }
                        if (map[x][nexty] == '.') {
                            while (nexty != y + 1) {
                                map[x][nexty] = map[x][nexty - 1]
                                nexty--
                            }
                            map[x][y + 1] = '@'
                            map[x][y] = '.'
                        }
                    }
                }
            }

            '^' -> {
                when (map[x - 1][y]) {
                    '.' -> {
                        map[x - 1][y] = '@'
                        map[x][y] = '.'
                    }

                    ']' -> {
                        if (canMove(map, x - 1, y - 1, up = true)) {
                            moveMultipleBlocks(map, x - 1, y - 1, up = true)
                            map[x - 1][y] = '@'
                            map[x - 1][y - 1] = '.'
                            map[x][y] = '.'
                        }
                    }

                    '[' -> {
                        if (canMove(map, x - 1, y, up = true)) {
                            moveMultipleBlocks(map, x - 1, y, up = true)
                            map[x - 1][y] = '@'
                            map[x - 1][y + 1] = '.'
                            map[x][y] = '.'
                        }
                    }
                }
            }

            'v' -> {
                when (map[x + 1][y]) {
                    '.' -> {
                        map[x + 1][y] = '@'
                        map[x][y] = '.'
                    }

                    ']' -> {
                        if (canMove(map, x + 1, y - 1, up = false)) {
                            moveMultipleBlocks(map, x + 1, y - 1, up = false)
                            map[x + 1][y] = '@'
                            map[x + 1][y - 1] = '.'
                            map[x][y] = '.'
                        }
                    }

                    '[' -> {
                        if (canMove(map, x + 1, y, up = false)) {
                            moveMultipleBlocks(map, x + 1, y, up = false)
                            map[x + 1][y] = '@'
                            map[x + 1][y + 1] = '.'
                            map[x][y] = '.'
                        }
                    }
                }
            }
        }
    }

    fun part(inputFile: String, part2: Boolean = false): Long {
        val (map, steps) = getInput(inputFile)
        if (part2) {
            enlargeMap(map)
        }
        var total = 0L

        steps.forEach {
            if (part2) {
                performStepEnlarged(map, it)
            } else {
                performStep(map, it)
            }
        }

        for (i in map.indices) {
            for (j in map[i].indices) {
                if (part2 && map[i][j] == '[' || !part2 && map[i][j] == 'O') {
                    total += (100L * i) + j
                }
            }
        }
        return total
    }

    println(part("15-sample"))
    println(part("15"))
    println(part("15-sample", part2 = true))
    println(part("15", part2 = true))
}
