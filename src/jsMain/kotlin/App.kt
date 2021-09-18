import kotlinx.browser.window
import kotlinx.coroutines.*
import react.*
import io.ktor.client.engine.js.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import react.dom.div
import react.dom.*
import kotlinext.js.*
import kotlinx.html.js.*
import kotlinx.coroutines.*

private val scope = MainScope()

val App = functionalComponent<RProps> { _ ->
    val (currentFolder, setFolder) = useState(
        Folder(
            "Use the form above to insert path to folder",
            mutableMapOf(),
            ArrayList()
        )
    )

    ul {
        h1 {
            +"Folder structure"
        }
        child(
            InputComponent,
            props = jsObject {
                onSubmit = { input ->
                    scope.launch {
                        setFolder(getFolder(PathWrapper(input)))
                    }
                }
            }
        )
        div {
            collapsedEntry {
                folder = currentFolder
                indent = ""
            }
        }
    }
}
