package ru.yamoney.test.app

import com.google.gson.Gson
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

data class WebHook(val eventKey: String)

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
                val json = call.receiveText()
                val gson = Gson()
                val hook = gson.fromJson(json, WebHook::class.java)
                println(hook)
                call.respond(HttpStatusCode.OK)
            }
        }
    }.start(wait = true)
}
