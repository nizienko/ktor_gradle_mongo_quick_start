package ru.yamoney.test.app

import com.google.gson.Gson
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.event.Level
import java.text.DateFormat


fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        install(DefaultHeaders)
        install(Compression)
        install(CallLogging) {
            level = Level.DEBUG
        }
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
        routing {
            post("/hook/{repository}") {
                println(call.parameters["repository"])
                println(call.request.header("X-Event-Key"))

                println(call.receiveJson<WebHook>())
                call.respond(HttpStatusCode.OK)
            }
        }
    }.start(wait = true)
}

data class WebHook(
        val eventKey: String,
        val actor: Actor,
        val pullRequest: PullRequest
)

data class Actor(
        val name: String,
        val emailAddress: String,
        val id: Int
)

data class PullRequest(
        val id: Int,
        val title: String,
        val state: String,
        val open: Boolean,
        val createdDate: Long,
        val updatedDate: Long,
        val fromRef: FromRef,
        val reviewers: List<String>
)

data class FromRef(
        val repository: Repository
)

data class Repository(
        val slug: String,
        val id: Int,
        val name: String,
        val project: Project
)

data class Project(
        val key: String,
        val id: Int,
        val name: String,
        val description: String
)

val gson = Gson()

suspend inline fun <reified T> ApplicationCall.receiveJson(): T {
    return gson.fromJson(this.receiveText(), T::class.java)
}