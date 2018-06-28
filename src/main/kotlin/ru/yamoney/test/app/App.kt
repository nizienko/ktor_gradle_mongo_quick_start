package ru.yamoney.test.app

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
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.event.Level
import java.text.DateFormat

data class TestEntity(val name: String, val surname: String)

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
                println(call.receiveText())
                call.respond(HttpStatusCode.OK)
            }
            get("/hook/{repository}") {
                println(call.parameters["repository"])
                println(call.request.header("X-Event-Key"))
                println(call.receiveText())
                call.respond(HttpStatusCode.OK)
            }
            put("/hook/{repository}") {
                println(call.parameters["repository"])
                println(call.request.header("X-Event-Key"))
                println(call.receiveText())
                call.respond(HttpStatusCode.OK)
            }
            delete("/hook/{repository}") {
                println(call.parameters["repository"])
                println(call.request.header("X-Event-Key"))
                println(call.receiveText())
                call.respond(HttpStatusCode.OK)
            }
        }
    }.start(wait = true)
}
