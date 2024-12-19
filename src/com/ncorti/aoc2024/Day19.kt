package com.ncorti.aoc2024

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        val lines = split("\n").filter { it.isNotBlank() }
        lines.first().split(", ") to lines.drop(1)
    }

    val memoization = mutableMapOf("" to 1L)

    fun canBeComposed(design: String, patterns: List<String>): Long {
        if (design in memoization) return memoization[design]!!
        var canBeComposed = 0L
        patterns.forEach { p ->
            if (design.startsWith(p)) {
                canBeComposed += canBeComposed(design.removePrefix(p), patterns)
            }
        }
        memoization[design] = canBeComposed
        return canBeComposed
    }

    fun part(inputFile: String, part2: Boolean = false): Long {
        val (patterns, designs) = getInput(inputFile)
        memoization.clear()
        memoization[""] = 1L
        var count = 0L
        designs.forEach {
            val result = canBeComposed(it, patterns)
            if (!part2 && result > 0) {
                count++
            } else {
                count += result
            }
        }
        return count
    }

    println(part("19-sample"))
    println(part("19"))
    println(part("19-sample", part2 = true))
    println(part("19", part2 = true))
}
