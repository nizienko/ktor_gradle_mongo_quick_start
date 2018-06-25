package ru.yamoney.test.app

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import ru.yamoney.test.app.db.db

data class TestEntity(val name: String, val surname: String)

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                try {
                    println(call.request.toString())
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }.start(wait = true)
}