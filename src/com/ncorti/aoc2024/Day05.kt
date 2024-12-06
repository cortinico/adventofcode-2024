package com.ncorti.aoc2024

fun main() {
    fun part1(): Int {
        val input = getInputAsText("05") {
            split("\n").filter { it.isNotBlank() }
        }
        val rules = input.filter { "|" in it }.map { it.split("|") }.map { it[0] to it[1] }
        return input.filter { it.contains(",") }
            .map { it.split(",") }
            .filter { steps ->
            rules.forEach {
                if (it.first in steps && it.second in steps && steps.indexOf(it.first) > steps.indexOf(it.second)) {
                    return@filter false
                }
            }
            return@filter true
        }.sumOf { (it[(it.size / 2)]).toInt() }
    }

    fun part2(): Int {
        val input = getInputAsText("05") {
            split("\n").filter { it.isNotBlank() }
        }
        val rules = input.filter { "|" in it }.map { it.split("|") }.map { it[0] to it[1] }
        return input.filter { it.contains(",") }
            .map { it.split(",") }
            .filter { steps ->
            rules.forEach {
                if (it.first in steps && it.second in steps && steps.indexOf(it.first) > steps.indexOf(it.second)) {
                    return@filter true
                }
            }
            return@filter false
        }.map {
            it.sortedWith { o1, o2 ->
                if (o1 to o2 in rules) -1 else 1
            }
        }.sumOf { (it[(it.size / 2)]).toInt() }
    }

    println(part1())
    println(part2())
}
