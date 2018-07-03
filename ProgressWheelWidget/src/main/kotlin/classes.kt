import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import tornadofx.*

/**
 * Creates a pi chart.
 */
class PiView: View() {
    // declare a chapter (this will be pulled from db later and be a reference here)
    val chapter = Chapter(52.0, 13.0, 5.0, 20.0)

    val values = listOf(chapter).observable()
    val model = ChapterModel(values[0])

    override val root = BorderPane()

    init {
        with(root) {
            center {
                stackpane {
                    // style for outside the text
                    //addClass(MyStyle.titleClass)

                    rectangle {
                        fill = Color.WHITE
                        width = 300.0
                        height = 400.0
                        arcWidth = 25.0
                        arcHeight = 25.0
                    }

                    piechart {
                        labelsVisible = false
                        isLegendVisible = false
                        maxWidth = 250.0
                        minWidth = 250.0
                        startAngle = -90.0
                        isClockwise = false
                        addClass(ChartCSS.defaultPieChart)

                        data("", model.blank.value.toDouble())
                        data("", model.recorded.value.toDouble())
                        data("", model.reviewed.value.toDouble())
                        data("", model.edited.value.toDouble())
                    }

                    circle {
                        centerX = 100.0
                        centerY = 100.0
                        radius = 75.0
                        fill = Color.WHITE
                    }

                    text(model.started.value.toInt().toString() + "/" + model.verses.value.toInt().toString()) {
                        style {
                            fontSize = 24.pt
                        }
                    }
                }
            }
        }
    }
}

class ChapterModel(var chapter: Chapter) : ViewModel() {
    val verses = bind { chapter.versesProperty }
    val started = bind { chapter.startedProperty }

    val recorded = bind { chapter.recordedProperty }
    val reviewed = bind { chapter.reviewedProperty }
    val edited = bind { chapter.editedProperty }
    val blank = bind { chapter.blankProperty }
}

class ChartCSS : Stylesheet() {
    companion object {
        val defaultPieChart by cssclass()
    }
    init {
        defaultPieChart {
            defaultColor0 { backgroundColor += c("#DFDEE3") } // not started (grey)
            defaultColor1 { backgroundColor += c("#E56060") } // recorded (red/orange/who knows)
            defaultColor2 { backgroundColor += c("#3DB5FF") } // reviewed (blue)
            defaultColor3 { backgroundColor += c("#58BD2F") } // edited (green)
        }
    }
}