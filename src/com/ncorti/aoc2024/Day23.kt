package com.ncorti.aoc2024

fun main() {

    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }
    }.toHashSet()

    data class Component(val pc1: String, val pc2: String, val pc3: String)

    fun part1(inputFile: String): Int {
        val pcs = mutableSetOf<String>()
        val input = getInput(inputFile)
        val components = mutableSetOf<Component>()
        input.forEach {
            val (pc1, pc2) = it.split("-")
            pcs.add(pc1)
            pcs.add(pc2)
        }
        pcs.forEach { pc1 ->
            val firstArcs = input.filter { it.startsWith("$pc1-") || it.endsWith("-$pc1") }
            firstArcs.forEach {
                val (left, right) = it.split("-")
                val pc2 = if (left == pc1) right else left
                val secondArcs = input.filter { it.startsWith("$pc2-") || it.endsWith("-$pc2") }.filter { pc1 !in it }
                secondArcs.forEach {
                    val (left2, right2) = it.split("-")
                    val pc3 = if (left2 == pc2) right2 else left2
                    if ("$pc1-$pc3" in firstArcs || "$pc3-$pc1" in firstArcs) {
                        val (p1, p2, p3) = listOf(pc1, pc2, pc3).sorted()
                        components.add(Component(p1, p2, p3))
                    }
                }
            }
        }
        return components.count { it.pc1.startsWith("t") || it.pc2.startsWith("t") || it.pc3.startsWith("t") }
    }

    fun bronKerbosch(
        R: Set<String>, P: MutableSet<String>, X: MutableSet<String>, graph: Map<String, Set<String>>
    ): Set<Set<String>> {
        val cliques = mutableSetOf<Set<String>>()
        if (P.isEmpty() && X.isEmpty()) {
            cliques.add(R.toMutableSet())
        }
        while (P.isNotEmpty()) {
            val nextNode = P.iterator().next()
            val newR: MutableSet<String> = R.toMutableSet()
            newR.add(nextNode)
            val newP: MutableSet<String> = P.toMutableSet()
            newP.retainAll(graph[nextNode]!!)
            val newX: MutableSet<String> = X.toMutableSet()
            newX.retainAll(graph[nextNode]!!)
            cliques.addAll(bronKerbosch(newR, newP, newX, graph))
            P.remove(nextNode)
            X.add(nextNode)
        }
        return cliques
    }

    fun part2(inputFile: String): String {
        val input = getInput(inputFile)
        val adj = mutableMapOf<String, Set<String>>()
        input.forEach {
            val (pc1, pc2) = it.split("-")
            adj[pc1] = adj.getOrDefault(pc1, setOf()) + pc2
            adj[pc2] = adj.getOrDefault(pc2, setOf()) + pc1
        }
        val cliques = bronKerbosch(setOf(), adj.keys, mutableSetOf(), adj)
        return cliques.maxBy { it.size }.toList().sorted().joinToString(",")
    }

    println(part1("23-sample"))
    println(part1("23"))
    println(part2("23-sample"))
    println(part2("23"))
}

