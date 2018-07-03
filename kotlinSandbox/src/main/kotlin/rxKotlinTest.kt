import javafx.scene.paint.Color
import tornadofx.*
import io.reactivex.Observable

/**
 * Test search function with RxKotlin
 */
class SearchTest: View() {
    var searchTerm : String = ""
    var searchButton = button("search") {}

    override val root = vbox {
        // style for outside the text
        // addClass(MyStyle.titleClass)

        text("This is a test.") {
            // sets text color
            // because to affect the text it's gotta be in here
            // but for the background it needs to be outside of the text object
            // hence, two different style things
            style {
                fill = Color.HOTPINK
            }
        }
        textfield(searchTerm)
    }
}

private fun createButtonClickObservable(): Observable<String> {
    val searchTest: SearchTest by inject()

    return Observable.create { emitter ->
        searchTest.searchButton.setOnClickListener {
            emitter.onNext(searchTest.searchTerm)
        }

        emitter.setCancellable {
            searchTest.searchButton.setOnClickListener(null)
        }
    }
}