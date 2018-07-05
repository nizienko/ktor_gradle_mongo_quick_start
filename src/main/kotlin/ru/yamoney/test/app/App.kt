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
import ru.yamoney.test.app.services.processCallback
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
            get("/callback/{uid}/{status}") {
                val uId = call.parameters["uid"] ?: throw IllegalArgumentException("unknown uid in callback")
                val status = call.parameters["status"] ?: throw IllegalArgumentException("unknown status in callback")
                processCallback(Callback(uId, status))
                call.respond(CallbackResponse("success"))
            }
        }
    }.start(wait = true)
}

data class Callback(val uid: String, val status: String)

data class CallbackResponse(val status: String)

