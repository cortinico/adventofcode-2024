package com.ncorti.aoc2024

import kotlin.math.abs

fun main() {

    fun parseInput(): Pair<List<Int>, List<Int>> {
        val couples = getInputAsText("01") {
            split("\n").dropLast(1)
        }.map {
            it.substringBefore(" ").toInt() to it.substringAfterLast(" ").toInt()
        }
        return couples.map { it.first } to couples.map { it.second }
    }

    fun part1(): Int {
        var (l1, l2) = parseInput()
        l1 = l1.sorted()
        l2 = l2.sorted()
        return l1.foldIndexed(0) { index, acc, _ ->
            acc + abs(l1[index] - l2[index])
        }
    }

    fun part2(): Int {
        val (l1, l2) = parseInput()
        return l1.foldIndexed(0) { index, acc, _ ->
            acc + l2.count { it == l1[index] } * l1[index]
        }
    }

    println(part1())
    println(part2())
}
