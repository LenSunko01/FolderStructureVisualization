@file:JsModule("react-collapse")
@file:JsNonModule

import react.RClass
import react.RProps

@JsName("Collapse")
external val collapseButton: RClass<ReactCollapseProps>

external interface ReactCollapseProps : RProps {
    var isOpened: Boolean
}
