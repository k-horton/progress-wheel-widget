package main

import tornadofx.*

fun main(args: Array<String>) {
    var graphModel = find(graphModel::class)
    graphModel.projectInit()

    print("Welcome to Factor-Mapper! Type 'start' to begin a new project (or 'exit' to exit): ")
    var enteredString = readLine()
    while (enteredString != "start" && enteredString != "exit") {
        println()
        print("Please type 'start' to begin or 'exit' to exit: ")
        enteredString = readLine()
    }
    if (enteredString == "start") {
        println()
        println("Great! Let's go!")
        addFactors(graphModel.myProject)
        addRelationships(graphModel.myProject)

        graphModel.myProject.initializeProject()
        println(graphModel.getLoopsAsText())

        println("FACTOR | INFLUENCE | DEPENDENCE")
        for (factor in graphModel.myProject.factors) {
            print(factor.name + ": " + factor.influence + " | " + factor.dependence)
        }
    }
    else if(enteredString == "exit") {
        println()
        println("Aight, bye!")
    }
}

fun addFactors(project: Project) {
    println("Let's add some factors.")
    var enteredString = ""
    var factors = ""

    println("Enter factor names one at a time (or enter 'finished' when you're done): ")

    enteredString = readLine()!!    // it's double-banged bc Kotlin has generalized anxiety disorder
    while(enteredString != "finished") {
        project.addFactor(Factor(enteredString))
        factors += enteredString + ", "
        enteredString = readLine()!!
        // todo: print a list of current factors added?
    }
    println("Factors: " + factors)
}

fun addRelationships(project: Project) {
    println("Alright, now onto how each factor affects the others.")

    for (factor1 in project.factors) {
        for (factor2 in project.factors)  {
            if (factor1 != factor2) {
                var polarity = 1
                var strength = 0

                print("Does " + factor1.name + " affect " + factor2.name + "? (y/n): ")
                var enteredString = readLine()

                while (enteredString != "y" && enteredString != "n") {
                    println()
                    print("Invalid input - please type 'y' or 'n': ")
                    enteredString = readLine()
                }

                if (enteredString == "n") { println("Great. Let's move to the next one.") }
                else if (enteredString == "y") {
                    while (strength == 0) {
                        println()
                        print("On a scale from 1 to 3, with 1 being weakest and 3 being strongest, how much does "
                                    + factor1.name + " affect " + factor2.name + "?: ")
                        enteredString = readLine()
                        when (Integer.parseInt(enteredString)) {
                            1, 2, 3 -> strength = Integer.parseInt(enteredString)
                            else -> {
                                println("Please enter a number between 1 and 3.")
                            }
                        }
                    }
                    println("Does " + factor1.name + " increase/positively impact "
                            + factor2.name + ", or does it decrease/neagtively impact it?")
                    print("Enter 'p' for positive, or enter 'n' for negative: ")
                    enteredString = readLine()
                    when (enteredString) {
                        "p" -> polarity = 1
                        "n" -> polarity = -1
                    }

                    project.addRelationship(Relationship(factor1, factor2, polarity, strength.toDouble()))
                }
            }
        }
    }
}
