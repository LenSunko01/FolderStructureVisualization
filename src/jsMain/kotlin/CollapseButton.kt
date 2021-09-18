import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

external interface CollapseButtonState : RState {
    var isOpen: Boolean
    var dropdownIcon: String
}

external interface CollapseButtonProps : RProps {
    var folder: Folder
    var indent: String
}

@JsExport
class CollapseButton : RComponent<CollapseButtonProps, CollapseButtonState>() {
    override fun CollapseButtonState.init() {
        isOpen = false
        dropdownIcon = "+"
    }

    override fun RBuilder.render() {
        div {
            div {
                var name = props.indent + "|${props.folder.name} "
                +name
                for ((word, occurence) in props.folder.words) {
                    val entry = " <$word:$occurence>"
                    +entry
                }
                if (!props.folder.children.isEmpty()) {
                    button {
                        attrs {
                            onClickFunction = {
                                setState() {
                                    isOpen = !isOpen
                                    if (dropdownIcon == "+") {
                                        dropdownIcon = "-"
                                    } else {
                                        dropdownIcon = "+"
                                    }
                                }
                            }
                        }
                        +"${state.dropdownIcon}"
                    }
                    collapseButton {
                        attrs.isOpened = state.isOpen
                        for (child in props.folder.children) {
                            div {
                                collapsedEntry {
                                    folder = child
                                    indent = props.indent + "___"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.collapsedEntry(handler: CollapseButtonProps.() -> Unit): ReactElement {
    return child(CollapseButton::class) {
        this.attrs(handler)
    }
}
