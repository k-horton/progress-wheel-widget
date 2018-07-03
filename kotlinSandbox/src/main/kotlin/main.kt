import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import tornadofx.*

/**
 * Initializes the class and the stylesheet.
 */
class MyApp : App(MyView::class) {
    // starts App in maximized window
    /*override fun start(stage: Stage) {
        super.start(stage)
        stage.isMaximized = true
    }*/

    // loads the stylesheet
    init {
        //reloadStylesheetsOnFocus()
    }
}

/**
 * The main view for the UI. (Think React-style.)
 */
class MyView : View() {
    override val root = VBox()

    init {
        with(root) {
            // pull some views
            val searchTest = find(SearchTest::class)

            // stick those bois in there
            borderpane {
                center = searchTest.root
            }

            // sets background color for the whole app
            style {
                backgroundColor += Color.LIGHTPINK
                padding = box(10.px)
            }
        }
    }
}

/**
 * Launches the app!
 */
fun main(args: Array<String>) {
    launch<MyApp>(args)
}