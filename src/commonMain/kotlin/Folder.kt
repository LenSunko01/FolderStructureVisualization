import kotlinx.serialization.Serializable

@Serializable
data class Folder(val name: String, var words: MutableMap<String, Int>, val children: ArrayList<Folder>) {
    companion object {
        const val path = "/folder"
    }
}
