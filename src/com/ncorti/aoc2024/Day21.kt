package com.ncorti.aoc2024

import kotlin.math.min

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }
    }

    val numKeypad = mapOf(
        '7' to Pair(0, 0),
        '8' to Pair(0, 1),
        '9' to Pair(0, 2),
        '4' to Pair(1, 0),
        '5' to Pair(1, 1),
        '6' to Pair(1, 2),
        '1' to Pair(2, 0),
        '2' to Pair(2, 1),
        '3' to Pair(2, 2),
        '0' to Pair(3, 1),
        'A' to Pair(3, 2)
    )

    val arrowKeypad = mapOf(
        '^' to Pair(0, 1),
        'A' to Pair(0, 2),
        '<' to Pair(1, 0),
        'v' to Pair(1, 1),
        '>' to Pair(1, 2),
    )

    val directions = mapOf(
        '<' to Pair(0, -1), '>' to Pair(0, 1), '^' to Pair(-1, 0), 'v' to Pair(1, 0)
    )


    fun generatePermutations(moves: List<Char>): List<List<Char>> {
        if (moves.isEmpty()) return listOf()
        if (moves.size == 1) return listOf(moves)
        val permutations = mutableListOf<List<Char>>()
        moves.forEachIndexed { index, move ->
            val subMoves = moves.toMutableList()
            subMoves.removeAt(index)
            val subPermutations = generatePermutations(subMoves)
            subPermutations.forEach { subPermutation ->
                permutations.add(listOf(move) + subPermutation)
            }
        }
        return permutations.distinct()
    }

    fun cartesianProduct(parts: List<List<List<Char>>>): List<List<List<Char>>> {
        if (parts.isEmpty()) return listOf()
        if (parts.size == 1) return parts.first().map { listOf(it) }
        val product = mutableListOf<List<List<Char>>>()
        val subProduct = cartesianProduct(parts.drop(1))
        parts.first().forEach { first ->
            subProduct.forEach { sub ->
                product.add(listOf(first) + sub)
            }
        }
        return product.distinct()
    }

    fun computePossibleWays(code: String, keypad: Map<Char, Pair<Int, Int>>): List<List<List<Char>>> {
        val parts = mutableListOf<List<List<Char>>>()
        var current = keypad['A']!!
        code.toCharArray().forEach { c ->
            val next = keypad[c]!!
            val dx = next.first - current.first
            val dy = next.second - current.second

            val moves = mutableListOf<Char>()
            if (dx > 0) {
                repeat(dx) { moves.add('v') }
            } else if (dx < 0) {
                repeat(-dx) { moves.add('^') }
            }
            if (dy > 0) {
                repeat(dy) { moves.add('>') }
            } else if (dy < 0) {
                repeat(-dy) { moves.add('<') }
            }
            val validPerms = generatePermutations(moves).map { it + 'A' }.filter { perm ->
                var (cx, cy) = current
                perm.forEach { key ->
                    if (key == 'A') return@filter true
                    val (dx, dy) = directions[key]!!
                    cx += dx
                    cy += dy
                    if ((cx to cy) !in keypad.values) {
                        return@filter false
                    }
                }
                return@filter true
            }
            if (validPerms.isEmpty()) {
                parts.add(listOf(listOf('A')))
            } else {
                parts.add(validPerms)
            }
            current = next
        }
        return cartesianProduct(parts)
    }

    fun part1(inputFile: String): Long {
        val step1 = mutableListOf<String>()
        val step2 = mutableListOf<String>()
        val step3 = mutableListOf<String>()
        var complexity = 0L

        getInput(inputFile).forEach { input ->
            step1.clear()
            step2.clear()
            step3.clear()
            val res1 = computePossibleWays(input, numKeypad)
            res1.forEach {
                step1.add(it.joinToString("") { it.joinToString("") })
            }
            step1.forEach {
                val res2 = computePossibleWays(it, arrowKeypad)
                res2.forEach {
                    step2.add(it.joinToString("") { it.joinToString("") })
                }
            }
            step2.forEach {
                val res3 = computePossibleWays(it, arrowKeypad)
                res3.forEach {
                    step3.add(it.joinToString("") { it.joinToString("") })
                }
            }
            val shortest = step3.minBy { it.length }!!
            println("$input: $shortest")
            complexity += shortest.length * input.removeSuffix("A").toLong()
        }

        return complexity
    }

    data class ComputeCallKey(val k1: Char, val k2: Char, val k3: Boolean)

    val memoCompute = mutableMapOf<ComputeCallKey, List<String>>()

    fun computePossibleWaysAsString(from: Char, to: Char, useDirectionalKeypad: Boolean): List<String> {
        if (ComputeCallKey(from, to, useDirectionalKeypad) in memoCompute) {
            return memoCompute[ComputeCallKey(from, to, useDirectionalKeypad)]!!
        }

        val keypad = if (useDirectionalKeypad) arrowKeypad else numKeypad
        val current = keypad[from]!!
        val next = keypad[to]!!

        val dx = next.first - current.first
        val dy = next.second - current.second

        val moves = mutableListOf<Char>()
        if (dx > 0) {
            repeat(dx) { moves.add('v') }
        } else if (dx < 0) {
            repeat(-dx) { moves.add('^') }
        }
        if (dy > 0) {
            repeat(dy) { moves.add('>') }
        } else if (dy < 0) {
            repeat(-dy) { moves.add('<') }
        }
        val validPerms = generatePermutations(moves).map { it + 'A' }.filter { perm ->
            var (cx, cy) = current
            perm.forEach { key ->
                if (key == 'A') return@filter true
                val (dx, dy) = directions[key]!!
                cx += dx
                cy += dy
                if ((cx to cy) !in keypad.values) {
                    return@filter false
                }
            }
            return@filter true
        }
        val result = if (validPerms.isEmpty()) {
            listOf("A")
        } else {
            validPerms.map { it.joinToString("") }
        }
        memoCompute[ComputeCallKey(from, to, useDirectionalKeypad)] = result
        return result
    }

    data class CostCallKey(val k1: Char, val k2: Char, val k3: Boolean, val k4: Int)

    val memoCost = mutableMapOf<CostCallKey, Long>()

    fun getStepCost(from: Char, to: Char, useDirectionalKeypad: Boolean, depth: Int): Long {
        if (CostCallKey(from, to, useDirectionalKeypad, depth) in memoCost) return memoCost[CostCallKey(
            from,
            to,
            useDirectionalKeypad,
            depth
        )]!!
        if (depth == 0) return computePossibleWaysAsString(from, to, true).minOf { it.length }.toLong()
        var bestCost = Long.MAX_VALUE
        val possibleWays = computePossibleWaysAsString(from, to, useDirectionalKeypad)
        possibleWays.forEach { way ->
            val code = "A$way"
            var cost = 0L
            for (i in 0 until code.length - 1) cost += getStepCost(code[i], code[i + 1], true, depth - 1)
            bestCost = min(cost, bestCost)
        }
        memoCost[CostCallKey(from, to, useDirectionalKeypad, depth)] = bestCost
        return bestCost
    }

    fun getLineCost(line: String, depth: Int): Long {
        val code = "A$line"
        var cost = 0L
        for (i in 0 until code.length - 1) cost += getStepCost(code[i], code[i + 1], false, depth)
        return cost
    }

    fun part2(inputFile: String): Long = getInput(inputFile).sumOf {
        it.removeSuffix("A").toLong() * getLineCost(it, 25)
    }

    println(part1("21-sample"))
    println(part1("21"))
    println(part2("21-sample"))
    println(part2("21"))
}
