import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File

fun createFolder(file : File) : Folder {
    if (!file.exists()) {
        return Folder("File does not exist", mutableMapOf(), ArrayList())
    }
    val fileName = file.name

    if (file.isFile) {
        var map = mutableMapOf<String, Int>()
        for (line in file.readLines()) {
            for (word in line.split(" ")) {
                val currentOccurence = map[word] ?: 0
                map[word] = currentOccurence + 1
            }
        }
        if (map.isNotEmpty()) {
            val tempList = map.toList().sortedBy { (_, value) -> -value }.take(3)
            map = tempList.toMap() as MutableMap<String, Int>
        }
        return Folder(fileName, map, arrayListOf())
    }
    val children = file.listFiles()
    val folder = Folder(fileName, mutableMapOf(), ArrayList(children.size))
    for (i in 0..children.size - 1) {
        val childFolder = createFolder(children[i])
        folder.children.add(childFolder)
        for ((key, value) in childFolder.words) {
            val currentOccurence = folder.words[key] ?: 0
            folder.words[key] = currentOccurence + value
        }
    }
    if (folder.words.isNotEmpty()) {
        val tempList = folder.words.toList().sortedBy { (_, value) -> -value }.take(3)
        folder.words = tempList.toMap() as MutableMap<String, Int>
    }
    return folder
}

fun main() {
    embeddedServer(Netty, 9090) {
	install(ContentNegotiation) {
    	    json()
	}
	install(CORS) {
    	    method(HttpMethod.Get)
    	    method(HttpMethod.Post)
    	    method(HttpMethod.Delete)
    	    anyHost()
	}
	install(Compression) {
            gzip()
	}
        routing {
            get("/") {
                call.respondText(
                this::class.java.classLoader.getResource("index.html")!!.readText(),
                ContentType.Text.Html)
            }
            static("/") {
                resources("")
            }
            route(Folder.path) {
                post {
                    val wrapper = call.receive<PathWrapper>()
                    val file = File(wrapper.path)
                    call.respond(createFolder(file))
                }
            }
        }
    }.start(wait = true)
}
