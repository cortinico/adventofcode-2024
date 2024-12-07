package com.ncorti.aoc2024

fun main() {

    fun part1(): Long {
        val lines = getInputAsText("07-sample") {
            split("\n")
                .filter { it.isNotBlank() }
                .map { it.split(": ", " ").map { it.toLong() } }
        }
        return lines.sumOf { line ->
            val target = line[0]
            var current = line[1]
            val stack = mutableListOf<Pair<Char, Long>>()
            var idx = 2
            do {
                if (idx < line.size) {
                    stack.add('+' to line[idx])
                    current += line[idx]
                    idx++
                }
                if (stack.size == line.size - 2) {
                    if (current == target) {
                        return@sumOf target
                    } else {
                        while (stack.isNotEmpty() && stack.last().first == '*') {
                            val (_, value) = stack.removeLast()
                            current /= value
                            idx--
                        }
                        if (stack.isEmpty()) {
                            return@sumOf 0L
                        }
                        val (_, value) = stack.removeLast()
                        current -= value
                        stack.add('*' to line[idx - 1])
                        current *= line[idx - 1]
                        if (stack.size == line.size - 2 && current == target) {
                            return@sumOf target
                        }
                    }
                }
            } while (stack.isNotEmpty())
            0L
        }
    }

    fun part2(): Long {
        val lines = getInputAsText("07-sample") {
            split("\n")
                .filter { it.isNotBlank() }
                .map { it.split(": ", " ").map { it.toLong() } }
        }
        return lines.sumOf { line ->
            val target = line[0]
            var current = line[1]
            val stack = mutableListOf<Pair<Char, Long>>()
            var idx = 2
            do {
                if (idx < line.size) {
                    stack.add('+' to line[idx])
                    current += line[idx]
                    idx++
                }
                if (stack.size == line.size - 2) {
                    if (current == target) {
                        return@sumOf target
                    } else {
                        while (stack.isNotEmpty() && stack.last().first == '|') {
                            val (_, value) = stack.removeLast()
                            current = current.toString().removeSuffix(value.toString()).toLong()
                            idx--
                        }
                        if (stack.isEmpty()) {
                            return@sumOf 0L
                        }
                        val (op, value) = stack.removeLast()
                        if (op == '+') {
                            current -= value
                            stack.add('*' to line[idx - 1])
                            current *= line[idx - 1]
                        } else {
                            current /= value
                            stack.add('|' to line[idx - 1])
                            current = "$current${line[idx - 1]}".toLong()
                        }
                        if (stack.size == line.size - 2 && current == target) {
                            return@sumOf target
                        }
                    }
                }
            } while (stack.isNotEmpty())
            0L
        }
    }

    println(part1())
    println(part2())
}
