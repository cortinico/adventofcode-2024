package com.ncorti.aoc2024

import kotlin.math.pow

data class State17(
    var a: Long, var b: Long, var c: Long, val program: List<Int>, var ip: Int = 0
)


fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        val (a, b, c, program) = split("\n").filter { it.isNotBlank() }
        return@getInputAsText State17(
            a = a.removePrefix("Register A: ").toLong(),
            b = b.removePrefix("Register B: ").toLong(),
            c = c.removePrefix("Register C: ").toLong(),
            program = program.removePrefix("Program: ").split(",").map { it.toInt() })
    }

    fun comboOperand(operand: Long, state17: State17): Long = when (operand) {
        0L, 1L, 2L, 3L -> operand
        4L -> state17.a
        5L -> state17.b
        6L -> state17.c
        else -> error("Invalid operand")
    }

    fun run(state: State17): String {
        var output = ""
        while (true) {
            if (state.ip >= state.program.size) {
                return output.removeSuffix(",")
            }
            val operation = state.program[state.ip]
            val operand = state.program[state.ip + 1].toLong()
            when (operation) {
                0 /*adv*/ -> {
                    val num = state.a
                    val denum = 2.0.pow(comboOperand(operand, state).toDouble())
                    state.a = (num / denum).toLong() // TODO Check truncation
                    state.ip += 2
                }

                1 /*bxl*/ -> {
                    state.b = state.b xor operand
                    state.ip += 2
                }

                2 /*bst*/ -> {
                    state.b = comboOperand(operand, state) % 8
                    state.ip += 2
                }

                3 /*jnz*/ -> {
                    if (state.a != 0L) {
                        state.ip = operand.toInt()
                    } else {
                        state.ip += 2
                    }
                }

                4 /*bxc*/ -> {
                    state.b = state.b xor state.c
                    state.ip += 2
                }

                5 /*out*/ -> {
                    output += "${(comboOperand(operand, state) % 8)},"
                    state.ip += 2
                }

                6 /*bdv*/ -> {
                    val num = state.a
                    val denum = 2.0.pow(comboOperand(operand, state).toDouble())
                    state.b = (num / denum).toLong()
                    state.ip += 2
                }

                7 /*cdv*/ -> {
                    val num = state.a
                    val denum = 2.0.pow(comboOperand(operand, state).toDouble())
                    state.c = (num / denum).toLong()
                    state.ip += 2
                }
            }
        }
    }

    fun part1(inputFile: String): String {
        val state = getInput(inputFile)
        return run(state)
    }

    fun part2(inputFile: String): Long {
        val state = getInput(inputFile)
        val target = state.program.toMutableList().joinToString(",")
        var currentA = 0L
        var commaCounts = 0
        while (true) {
            val result = run(state.copy(a = currentA))
            if (target.endsWith(result) && result.count { it == ',' } == commaCounts) {
                if (target == result) {
                    return currentA
                }
                currentA *= 8
                commaCounts++
            } else {
                currentA++
            }
        }
    }

    println(part1("17-sample"))
    println(part1("17"))
    println(part2("17-sample"))
    println(part2("17"))
}
