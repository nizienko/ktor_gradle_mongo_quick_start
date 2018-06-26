package ru.yamoney.test.app

import io.ktor.application.call
import io.ktor.request.uri
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

data class TestEntity(val name: String, val surname: String)

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                try {
                    println(call.request.uri)
                    println(call.request.queryParameters)
                    call.respondText { "Hello" }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }.start(wait = true)
}