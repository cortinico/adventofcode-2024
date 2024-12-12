package com.ncorti.aoc2024

fun main() {
    fun getInput(inputFile: String) = getInputAsText(inputFile) {
        split("\n").filter { it.isNotBlank() }
    }.first().toCharArray()


    fun computeDisk(line: CharArray): MutableList<String> {
        val disk = mutableListOf<String>()
        var fileIdx = 0
        var blank = false
        for (i in line.indices) {
            val current = line[i].toString().toInt()
            if (blank) {
                repeat(current) { disk.add(".") }
                blank = false
            } else {
                repeat(current) { disk.add(fileIdx.toString()) }
                blank = true
                fileIdx++
            }
        }
        return disk
    }

    fun computeBlocks(line: CharArray): MutableList<Pair<Int,Int>> {
        val blocks = mutableListOf<Pair<Int,Int>>()
        var fileIdx = 0
        var blank = false
        for (i in line.indices) {
            val current = line[i].toString().toInt()
            if (blank) {
                blocks.add(current to -1)
                blank = false
            } else {
                blocks.add(current to fileIdx)
                blank = true
                fileIdx++
            }
        }
        return blocks
    }

    fun rearrangeDisk(disk: MutableList<String>) {
        var i = 0
        var j = disk.size - 1
        while (i < j) {
            if (disk[i] != ".") {
                i++
                continue
            }
            while (disk[j] == ".") {
                j--
            }
            val t = disk[i]
            disk[i] = disk[j]
            disk[j] = t
            j--
            i++
        }
    }


    fun part1(inputFile: String): Long {
        val line = getInput(inputFile)
        val disk = computeDisk(line)
        rearrangeDisk(disk)

        return disk.filter { it != "." }.mapIndexed { index, value -> index.toLong() * value.toLong() }.sum()
    }

    fun computeChecksum(blocks: MutableList<Pair<Int, Int>>): Long {
        var total = 0L
        var idx = 0
        blocks.forEach {
            val (size, descriptor) = it
            repeat(size) {
                if (descriptor > 0) {
                    total += idx.toLong() * descriptor.toLong()
                }
                idx++
            }
        }
        return total
    }

    fun compactBlocks(blocks: MutableList<Pair<Int, Int>>) {
        var i = 0
        while (i < blocks.size -1) {
            if (blocks[i].second == -1 && blocks[i+1].second == -1) {
                blocks[i] = blocks[i].first + blocks[i+1].first to -1
                blocks.removeAt(i+1)
            } else {
                i++
            }
        }
    }

    fun printBlocks(blocks: MutableList<Pair<Int, Int>>) {
        blocks.forEach {
            val (size, descriptor) = it
            if (descriptor == -1) {
                print("($size) ")
            } else {
                print("$size[$descriptor] ")
            }
        }
        println()
    }

    fun part2(inputFile: String): Long {
        val line = getInput(inputFile)
        val blocks = computeBlocks(line)

        var currentDescriptor = blocks.maxBy { it.second }.second
        while (currentDescriptor >= 0) {
            val i = blocks.indexOfLast { it.second == currentDescriptor }
            val (size, descriptor) = blocks[i]

            for (j in 0 until i) {
                val (targetSize, targetDescriptor) = blocks[j]
                if (targetDescriptor == -1 && targetSize == size) {
                    blocks[i] = size to -1
                    blocks[j] = size to descriptor
                    break
                } else if (targetDescriptor == -1 && targetSize > size) {
                    blocks[i] = size to -1
                    blocks[j] = size to descriptor
                    blocks.add(j+1, targetSize-size to -1)
                    compactBlocks(blocks)
                    break
                }
            }
            currentDescriptor--
        }
        printBlocks(blocks)
        return computeChecksum(blocks)
    }

    println(part1("09-sample"))
    println(part1("09"))
    println(part2("09-sample"))
    println(part2("09"))
}
