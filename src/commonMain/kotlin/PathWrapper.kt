import kotlinx.serialization.Serializable

@Serializable
data class PathWrapper(val path: String) {
    companion object {
        const val path = "/folder"
    }
}
