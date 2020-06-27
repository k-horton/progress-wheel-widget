package main

import javafx.scene.paint.Color
import javafx.scene.image.Image
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import tornadofx.*

class MyApp: App(masterView::class)

/*
 * Main() starts/runs the application.
 */
/*fun main(args: Array<String>) {
    var graphModel = find(graphModel::class)

    graphModel.projectInit()    // yikes! todo: remove this code after testing

    launch<MyApp>(args)
}*/

/*
 * ALL VIEWS
 * @masterView - the permanent structure using BorderPane to hold all other views.
 * @factorOnboarding - view holding the factor onboarding (user input) process
 * @relationshipOnboarding - view holding the relationship onboarding (user input) process
 * @graphDisplay - displays the graphs (currently just displays all the loops as text)
 */
class masterView: View() {
    val factorView = find(factorOnboarding::class)

    override val root = borderpane {
        top = hbox {
            label("IFML FACTOR-MAPPING SOFTWARE") {
                textAlignment = TextAlignment.CENTER
                font = Font.font("Arial", FontWeight.BOLD, 36.0)
            }
            style {
                baseColor = Color.ALICEBLUE

                useMaxWidth = true
                minHeight = Dimension(100.0, Dimension.LinearUnits.px)
            }
        }

        center = factorView.root

        bottom = hbox {
            label("Copyright [Some Company Name Here?] 2020") {
                textAlignment = TextAlignment.CENTER
                font = Font.font("Arial", FontWeight.THIN, 15.0)
            }
        }

        style {
            font = Font.font("Arial", FontWeight.NORMAL, 17.0)

            minWidth = Dimension(1000.0, Dimension.LinearUnits.px)
            minHeight = Dimension(700.0, Dimension.LinearUnits.px)
            useMaxSize = true

            val autoPadding = Dimension(20.0, Dimension.LinearUnits.px)
            padding = CssBox(autoPadding, autoPadding, autoPadding, autoPadding)
        }
    }
}

class factorOnboarding: View() {
    var controller = find(MyController::class)

    override val root = vbox {
        label("Here's where we'll enter the factors!")
        textfield()
        button("Next -->") {
            action {
                replaceWith(relationshipOnboading::class)
            }
        }
    }
}

class relationshipOnboading: View() {
    override val root = vbox {
        label("Here's where we'll assign the relationships!")
        button("Next -->") {
            action {
                replaceWith(graphDisplay::class)
            }
        }
    }
}

class graphDisplay: View() {
    var controller = find(MyController::class)

    override val root = pane {
        label("Here's where we'll display the graph!")

        imageview(Image("file:src/main/kotlin/ring.jpg")) {
            fitHeight = 500.0
            fitWidth = 500.0

            var x = 50
            var y = 100
            for (factor in controller.myProject.factors) {
                circle(x, y, 10.0) {
                    fill = Color.CADETBLUE
                }
                label(factor.name)
                x += 70
            }
        }

        for (loop in controller.getLoopList()) {
            label(loop)
        }

        style {
            textAlignment = TextAlignment.CENTER
        }
    }
}

/*
 * Controller. Processes user input and requests.
 */
class MyController: Controller() {
    var graphModel = find(main.graphModel::class)
    var myProject = graphModel.myProject
    lateinit var loopTextList: List<String>

    // todo: this is also very janky. I should fix this.
    fun getLoopList(): List<String> {
        loopTextList = graphModel.getLoopsAsText()

        return loopTextList
    }

}

/*
 * Model. Holds data-related logic.
 */
class graphModel: ViewModel() {
    var myProject = Project()

    fun projectInit() {
        /*
        val le = Factor("local economy")
        myProject.addFactor(le)
        val ge = Factor("global economy")
        myProject.addFactor(ge)
        val gf = Factor("government funding")
        myProject.addFactor(gf)
        val lr = Factor("local rivalries")
        myProject.addFactor(lr)
        val ai = Factor("average income")
        myProject.addFactor(ai)
        val t = Factor("tourism")
        myProject.addFactor(t)
        val ws = Factor("water sources")
        myProject.addFactor(ws)

        myProject.addRelationship(Relationship(le, ge, 1, 1.0))
        myProject.addRelationship(Relationship(ge, t,1, 3.0))
        myProject.addRelationship(Relationship(t, ws, -1, 1.0))
        myProject.addRelationship(Relationship(ws, le, 1, 3.0))
        myProject.addRelationship(Relationship(ai, le, 1, 2.0))
        myProject.addRelationship(Relationship(ge, ai, 1, 1.0))
        myProject.addRelationship(Relationship(ws, t, -1, 1.0)) */

        myProject.initializeProject()
    }

    fun getLoopsAsText(): List<String> {
        val bLoops = myProject.bLoops
        val rLoops = myProject.rLoops
        var loopsText = mutableListOf<String>()

        for (loop in bLoops) {
            var i = 0
            var text = "Balancing: "
            for (factor in loop) {
                i++
                text += factor.name
                if (i != loop.size) { text += " --> " }
            }
            loopsText.add(text)
            println(text)
        }

        for (loop in rLoops) {
            var i = 0
            var text = "Reinforcing: "
            for (factor in loop) {
                i++
                text += factor.name
                if (i != loop.size) { text += " --> " }
            }
            loopsText.add(text)
            println(text)
        }

        return loopsText
    }

}