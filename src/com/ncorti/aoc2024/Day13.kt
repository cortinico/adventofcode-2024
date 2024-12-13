package com.ncorti.aoc2024

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }
    }

    fun part(inputFile: String, isPart2: Boolean = false): Long {
        val input = getInput(inputFile)
        var total = 0L
        input.chunked(3).onEach { (l1, l2, l3) ->
            val (x1, y1) = l1.split("Button A: X+", ", Y+").filter { runCatching { it.toLong() }.isSuccess }
                .map { it.toLong() }
            val (x2, y2) = l2.split("Button B: X+", ", Y+").filter { runCatching { it.toLong() }.isSuccess }
                .map { it.toLong() }
            val (n1, n2) = l3.split("Prize: X=", ", Y=").filter { runCatching { it.toLong() }.isSuccess }.map {
                if (isPart2) it.toLong() + 10000000000000L else it.toLong()
            }

            val n = (n1 * y2 - n2 * x2).toDouble() / (x1 * y2 - x2 * y1)
            val m = (n1 - x1 * n) / x2
            total += if (n % 1 != 0.0 || m % 1 != 0.0) {
                0L
            } else {
                n.toLong() * 3 + m.toLong()
            }
        }
        return total
    }

    println(part("13-sample"))
    println(part("13"))
    println(part("13-sample", isPart2 = true))
    println(part("13", isPart2 = true))
}
