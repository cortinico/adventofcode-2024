package com.ncorti.aoc2024

import com.ncorti.aoc2024.Operation.*

enum class Operation { AND, OR, XOR }

fun main() {

    data class Wire(val name: String, var value: Int)
    data class Port(val i1: Wire, val i2: Wire, val out: Wire, val op: Operation)

    fun getInput(inputFile: String): Pair<MutableMap<String, Wire>, List<Port>> {
        val raw = getInputAsText(inputFile) {
            split("\n").filter { it.isNotBlank() }
        }
        val wires = raw.filter { ":" in it }
            .map { line -> Wire(name = line.substringBefore(":"), value = line.substringAfter(":").trim().toInt()) }
            .associateBy { it.name }.toMutableMap()
        val ports = raw.filter { ":" !in it }.map {
            val (i1, op, i2, out) = it.split(" ", " -> ", "->").filter { it.isNotBlank() }
            val input1 = wires.getOrPut(i1) { Wire(name = i1, value = -1) }
            val input2 = wires.getOrPut(i2) { Wire(name = i2, value = -1) }
            val output = wires.getOrPut(out) { Wire(name = out, value = -1) }
            Port(input1, input2, output, Operation.valueOf(op))
        }
        return wires to ports
    }


    fun part1(inputFile: String): Long {
        val (wires, ports) = getInput(inputFile)
        while (wires.values.filter { it.name.startsWith("z") }.any { it.value == -1 }) {
            ports.filter { it.out.value == -1 && it.i1.value != -1 && it.i2.value != -1 }.forEach { port ->
                port.out.value = when (port.op) {
                    AND -> port.i1.value and port.i2.value
                    OR -> port.i1.value or port.i2.value
                    XOR -> port.i1.value xor port.i2.value
                }
            }
        }
        return wires.values.filter { it.name.startsWith("z") }.sortedBy { it.name }.reversed()
            .joinToString("") { it.value.toString() }
            .toLong(radix = 2)
    }

    fun part2(inputFile: String): String {
        val (_, ports) = getInput(inputFile)
        val violations = mutableListOf<Port>()
        val inputXor = ports.filter { it.op == XOR }.filter {
            (it.i1.name.startsWith("x") && it.i2.name.startsWith("y")) || (it.i1.name.startsWith("y") && it.i2.name.startsWith(
                "x"
            ))
        }.filterNot {
            (it.i1.name == "x00" && it.i2.name == "y00") || (it.i1.name == "y00" && it.i2.name == "x00")
        }
        val otherXor = ports.filter { it.op == XOR }.filter {
            !it.i1.name.startsWith("x") && !it.i2.name.startsWith("y") && !it.i1.name.startsWith("y") && !it.i2.name.startsWith(
                "x"
            )
        }
        inputXor.forEach { check ->
            otherXor.any { it.i1.name == check.out.name || it.i2.name == check.out.name }.let { found ->
                if (!found) violations.add(check)
            }
        }
        otherXor.forEach { check ->
            inputXor.any { it.out.name == check.i1.name || it.out.name == check.i2.name }.let { found ->
                if (!found) {
                    println("Could not find a port that is my input - $check")
                    // You will need to check which port is the correct swap
                }
            }
        }
        violations.addAll(otherXor.filter { !it.out.name.startsWith("z") })
        val outputterPorts = ports.filter { it.out.name.startsWith("z") }
        violations.addAll(outputterPorts.filter { it.op != XOR }.filter { it.out.name != "z45" })
        return violations.sortedBy { it.out.name }.joinToString(",") { it.out.name }
    }

    println(part1("24-sample"))
    println(part1("24"))
    println(part2("24"))
}
