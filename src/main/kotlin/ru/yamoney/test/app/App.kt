package ru.yamoney.test.app

import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.features.toLogString
import io.ktor.http.HttpStatusCode
import io.ktor.request.ApplicationRequest
import io.ktor.request.contentType
import io.ktor.request.httpMethod
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.toMap

data class TestEntity(val name: String, val surname: String)

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        intercept(ApplicationCallPipeline.Call) {
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
        }
    }.start(wait = true)
}///asdasdasdasd

fun processRequest(request: ApplicationRequest) {
    println(request.httpMethod)
    println(request.contentType())
    println(request.toLogString())
}