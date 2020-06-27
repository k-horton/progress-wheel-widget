package main

import com.ichipsea.kotlin.matrix.*     // https://github.com/yinpeng/kotlin-matrix

/*
 * Copyright 2020 [Company Name]
 * @author Kimberly Horton
 * last updated 05-18-2020
 */

class Project {
    lateinit var strengthMatrix: Matrix<Double>
    var factors: List<Factor> = listOf()
    var relationships: List<Relationship> = listOf()
    var _vertexMap = HashMap<Factor, HashMap<Factor, Relationship>>()
    var bLoops = mutableListOf<MutableList<Factor>>()
    var rLoops = mutableListOf<MutableList<Factor>>()

    fun addFactor(newFactor: Factor) {
        newFactor.location = factors.size
        factors += newFactor
    }

    fun addRelationship(newRelationship: Relationship) {
        relationships += newRelationship
    }

    /*
     * a secondary "init"-type process to initialize the internal
     * arrays to be the right size (i.e. based on the no. of factors)
     * and multiply the strength matrix
     */
    fun initializeProject() {
        // fill the double-hashmap

        for (rel in relationships) {
            // filter out all rels with strength '0'
            if (rel.str != 0.0) {
                // if the factor hasn't been put into the map yet
                if (!_vertexMap.containsKey(rel.f1)) {
                    val _newHash = HashMap<Factor, Relationship>()
                    _newHash.put(rel.f2, rel)
                    _vertexMap.put(rel.f1, _newHash)
                }
                // if the key exists and thus already has a hashMap attached to it
                else {
                    _vertexMap[rel.f1]?.put(rel.f2, rel)    // the '?.' is totally unnecessary, but it's Kotlin, so...
                }
            }
        }

        /*
        // todo: remove this print statement for testing
        for (factor in _vertexMap.keys) {
            print(factor.name + " relates to ")
            for (rel in _vertexMap.getValue(factor).keys) {
                print(rel.name + ", ")
            }
            println()
        }
        */

        // sets strengthMatrix to the 7x multiplied version
        createStrengthMatrix()

        // find all the existing loops!
        for (factor in _vertexMap.keys) {
            findAllLoops(mutableListOf(factor))
        }

        // sort loops by strength
        bLoops.sortByDescending { getLoopStrength(it) }
        rLoops.sortByDescending { getLoopStrength(it) }

    }

    // todo: THIS IS VERY CLUNKY AND I NEED TO MAKE IT ELEGANT
    /*
     * Sets the project's strengthMatrix variable to a 7x multiplied representation of all factor relationships.
     */
    fun createStrengthMatrix() {
        var _tempArray = Array(factors.size) { i -> relationships[0]}
        var relMatrix = Array(factors.size) {i -> _tempArray}

        // add each relationship's data to 2D array
        for(rel in relationships) {
            relMatrix[rel.f1.location][rel.f2.location] = rel
        }

        // copy over just the str value to a new matrix
        strengthMatrix = createMatrix(factors.size, factors.size) { x, y -> relMatrix[x][y].str }

        // multiply the strength matrix by itself 7 times
        for (i in 0..7) {
            strengthMatrix = strengthMatrix x strengthMatrix
        }

        // just running this here because it has to run after this function does (& no time like the present)
        getInfluenceAndDependence()
    }

    // todo: test this thing
    fun getInfluenceAndDependence() {
        for (factor in factors) {
            for (i in 0..strengthMatrix.rows) {
                factor.influence += strengthMatrix[i, factor.location]
                factor.dependence += strengthMatrix[factor.location, i]
                println(i + ": " + factor.influence + " | " + factor.dependence)
            }
        }
    }

    // ===== FINDING ALL TOURS (AKA LOOPS) ===== //
    // Breaks if any same-factor relationships exist!
    fun findAllLoops(path: MutableList<Factor>) {
        var currentPath = path

        if (currentPath.size == 0) {
            println("ERROR: Bruh, your findAllLoops() is getting a size-0 path.")
        }

        // for each possible 'next step' from a factor...
        for (nextStep in _vertexMap.getValue(currentPath.last()).keys) {
            // ... if it's the same as the starting factor, add the loop
            if (nextStep == currentPath[0]) {
                // create a copy of loop (negates risk of memory crosstalk w/ currentPath from findAllLoops)
                var pathCopy = currentPath.toMutableList()
                // add the last factor in the loop
                pathCopy.add(nextStep)
                loopSorter(pathCopy)
            }
            // if it's not a repeat factor, add it and recurse
            else if (!currentPath.contains(nextStep)) {
                // create a copy of currentPath to edit (MEMORY CROSS-TALK WILL HAPPEN WHEN YOU DON'T HERE!!!)
                var loopPath = currentPath.toMutableList()
                loopPath.add(nextStep)
                findAllLoops(loopPath)
            }
        }
    }

    /*
     * Places a loop in either the Balancing or Reinforcing loop lists
     */
    fun loopSorter(loop: MutableList<Factor>) {
        when (getLoopPolarity(loop)) {
            1 -> bLoops.add(loop)
            -1 -> rLoops.add(loop)
            else -> {
                println("ERROR: Bruh, getLoopPolarity() is not working.")
            }
        }
    }

    /*
     * Returns the loop's calculated strength using the multiplied-out strengths in the strengthMatrix.
     *
     * The mathematical function is simply adding the multiplied-out strength of each relationships and then
     * finding the average by dividing the total by the number of factors in the loop.
     */
    fun getLoopStrength(loop: MutableList<Factor>): Double {
        var totalStr = 0.0
        for (i in 0 until loop.size - 1) {
            totalStr += strengthMatrix.get(loop.get(i).location, loop.get(i + 1).location)
        }

        return totalStr / (loop.size - 1)    // -1 is taking the duplicate start/end factor into account
    }

    /*
     * Returns 1 if loop is balancing, -1 if reinforcing.
     */
    fun getLoopPolarity(loop: MutableList<Factor>): Int {
        var totalPolarity = 1

        for (i in 0 until loop.size - 1) {
            // println("Rel: " + loop.get(i).name + " & " + loop.get(i + 1).name)
            totalPolarity *= _vertexMap.getValue(loop.get(i)).getValue(loop.get(i + 1)).pol
        }

        // println("This loop is " + totalPolarity + "!")

        return totalPolarity
    }
}
