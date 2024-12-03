package com.ncorti.aoc2024

import kotlin.math.absoluteValue

fun main() {

    fun isSafe(input: List<Int>): Boolean {
        val diffs = List(input.size) { index ->
            input[(index + 1) % input.size] - input[index]
        }.dropLast(1)
        return (diffs.all { it >= 0 } || diffs.all { it <= 0 }) && diffs.all { it.absoluteValue in 1..3 }
    }

    fun part1(): Int = getInputAsText("02") {
        split("\n").dropLast(1)
    }.map {
        it.split(" ").map { it.toInt() }
    }.count(::isSafe)

    fun part2(): Int = getInputAsText("02") {
        split("\n").dropLast(1)
    }.map {
        it.split(" ").map { it.toInt() }
    }.count { list ->
        repeat(list.size) {
            val substring = list.filterIndexed { index, i -> index != it }
            if (isSafe(substring)) {
                return@count true
            }
        }
        return@count isSafe(list)
    }

    println(part1())
    println(part2())
}
