package ru.yamoney.test.app

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.request.*
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.event.Level
import java.text.DateFormat

data class TestEntity(val name: String, val surname: String)

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        /*        intercept(ApplicationCallPipeline.Call) {
                    try {
                        println(call.request.uri)
                        println(call.request.queryParameters.toMap())
                        call.request.headers.forEach { s, list -> println("$s: $list") }
                        processRequest(call.request)
                        call.respond(HttpStatusCode.OK)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        call.respond(HttpStatusCode.BadRequest, e.message ?: e.localizedMessage)
                    }
                }*/
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
            }
        }
    }.start(wait = true)
}///asdasdasdasd

fun processRequest(request: ApplicationRequest) {
    println(request.httpMethod)
    println(request.contentType())
    println(request.toLogString())
}