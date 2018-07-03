import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty

/**
 * Chapter Model
 */
class Chapter(verses: Double, recorded: Double, reviewed: Double, edited: Double) {
    val blankProperty = SimpleDoubleProperty(verses - recorded - reviewed - edited)

    val startedProperty = SimpleDoubleProperty(recorded + reviewed + edited)

    val versesProperty = SimpleDoubleProperty(verses)
    //var verses by versesProperty

    val recordedProperty = SimpleDoubleProperty(recorded)
    //var recorded by recordedProperty

    val reviewedProperty = SimpleDoubleProperty(reviewed)
    //val reviewed by reviewedProperty

    val editedProperty = SimpleDoubleProperty(edited)
    //val edited by editedProperty
}