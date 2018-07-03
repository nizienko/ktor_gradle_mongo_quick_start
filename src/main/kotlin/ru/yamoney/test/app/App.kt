package ru.yamoney.test.app

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.event.Level
import receiveJson
import ru.yamoney.test.app.services.processWebHook
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
                processWebHook(call.receiveJson())
                call.respond(HttpStatusCode.OK)
            }
            get("/callback/{prId}/{status}") {
                println(call.parameters["prId"])
                println(call.parameters["status"])
                call.respond(CallBackResponse("success"))
            }
        }
    }.start(wait = true)
}

data class CallBackResponse(val status: String)

