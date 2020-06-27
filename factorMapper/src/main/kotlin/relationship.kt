package main
/*
 * Copyright 2020 [Company Name]
 * @author Kimberly Horton
 * last updated 04-13-2020
 */

class Factor(factorName: String) {
    var location: Int
    var name = factorName
    var influence = 0.0
    var dependence = 0.0

    init {
        location = 0
        // todo: write check for name
    }
}

/*
 * polarity: -1 for negative polarity, +1 for positive.
 */
class Relationship(factor1: Factor, factor2: Factor, polarity: Int, strength: Double) {
    var f1 = factor1
    var f2 = factor2
    var pol = polarity
    var str = strength
    
    // todo: write functions for editing

    init {
        // todo: write checks for each var
    }
}