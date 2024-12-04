package com.ncorti.aoc2024

enum class State {
    START, FIRST_NUM, COMMA, SECOND_NUM
}

fun main() {
    fun parseAndMultiply(substring: String): Int = substring.split(",")
        .map { it.toInt() }
        .reduce { acc, i -> acc * i }

    fun part1(): Int {
        val chars = getInputAsText("03") { this }
        var result = 0
        var start = 0
        while (chars.indexOf("mul(", start) != -1) {
            val idxStart = chars.indexOf("mul(", start)
            val idxEnd = chars.indexOf(")", idxStart)
            if (idxEnd == -1) {
                break
            }
            var valid = true
            var state = State.START
            for (i in idxStart + 4 until idxEnd) {
                state = when {
                    state == State.START && chars[i].isDigit() -> State.FIRST_NUM
                    state == State.FIRST_NUM && chars[i].isDigit() -> State.FIRST_NUM
                    state == State.FIRST_NUM && chars[i] == ',' -> State.COMMA
                    state == State.COMMA && chars[i].isDigit() -> State.SECOND_NUM
                    state == State.SECOND_NUM && chars[i].isDigit() -> State.SECOND_NUM
                    else -> {
                        valid = false
                        break
                    }
                }
            }
            if (valid) {
                result += parseAndMultiply(chars.substring(idxStart + 4, idxEnd))
            }
            start = idxStart + 1
        }
        return result
    }

    fun part2(): Int {
        val chars = getInputAsText("03") { this }
        var result = 0
        var start = 0
        var enabled = true
        while (chars.indexOfAny(listOf("mul(", "do()", "don't()"), start) != -1) {
            val idxStart = chars.indexOfAny(listOf("mul(", "do()", "don't()"), start)
            val idxDo = chars.indexOf("do()", start)
            val idxDont = chars.indexOf("don't()", start)
            if (idxStart == idxDo) {
                enabled = true
                start = idxStart + 1
                continue
            } else if (idxStart == idxDont) {
                enabled = false
                start = idxStart + 1
                continue
            }
            val idxEnd = chars.indexOf(")", idxStart)
            if (idxEnd == -1) {
                break
            }
            var valid = true
            var state = State.START
            for (i in idxStart + 4 until idxEnd) {
                state = when {
                    state == State.START && chars[i].isDigit() -> State.FIRST_NUM
                    state == State.FIRST_NUM && chars[i].isDigit() -> State.FIRST_NUM
                    state == State.FIRST_NUM && chars[i] == ',' -> State.COMMA
                    state == State.COMMA && chars[i].isDigit() -> State.SECOND_NUM
                    state == State.SECOND_NUM && chars[i].isDigit() -> State.SECOND_NUM
                    else -> {
                        valid = false
                        break
                    }
                }
            }
            if (valid && enabled) {
                result += parseAndMultiply(chars.substring(idxStart + 4, idxEnd))
            }
            start = idxStart + 1
        }
        return result
    }
    println(part1())
    println(part2())
}
