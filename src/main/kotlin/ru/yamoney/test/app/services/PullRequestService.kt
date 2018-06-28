package ru.yamoney.test.app.services

import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import ru.yamoney.test.app.data.PullRequest
import ru.yamoney.test.app.data.WebHook
import ru.yamoney.test.app.db.db

fun processWebHook(webHook: WebHook) {
    when (webHook.eventKey) {
        "pr:opened" -> db.getCollection<PullRequest>().insertOne(webHook.pullRequest)
        "pr:deleted" -> db.getCollection<PullRequest>().deleteOne(webHook.pullRequest::id eq webHook.pullRequest.id)
        else -> println(webHook)
    }
}