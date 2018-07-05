package ru.yamoney.test.app.services

import org.apache.http.client.fluent.Form
import org.apache.http.client.fluent.Request
import org.litote.kmongo.deleteOne
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.litote.kmongo.updateOne
import ru.yamoney.test.app.Callback
import ru.yamoney.test.app.data.PullRequest
import ru.yamoney.test.app.data.WebHook
import ru.yamoney.test.app.db.db
import ru.yamoney.test.app.sendMessage


fun processWebHook(webHook: WebHook) {
    when (webHook.eventKey) {
        "pr:opened" -> {
            sendMessage("Новый pr от ${webHook.actor.displayName} в ${webHook.pullRequest.fromRef.repository.name}")
            db.getCollection<PullRequestState>().insertOne(
                    PullRequestState(
                            webHook.pullRequest.uid(),
                            webHook.actor.displayName,
                            State.OPEN))
            startJob(webHook.pullRequest)

            db.getCollection<PullRequestState>().updateOne(
                    "{uid: '${webHook.pullRequest.uid()}'}",
                    "{\$set: {state: '${State.COMPILATION_CHECK_STARTED}'}}"
            )
        }
        "pr:deleted" -> db.getCollection<PullRequestState>().deleteOne("{uid: '${webHook.pullRequest.uid()}'}")
        else -> println(webHook)
    }
}

fun processCallback(callback: Callback) {
    val pullRequest = db.getCollection<PullRequestState>()
            .findOne("{uid: '${callback.uid}'}")
            ?: throw IllegalStateException("Не знаем такой ПР $callback")

    println("$pullRequest compilation check finished with $callback")

    db.getCollection<PullRequestState>().updateOne(
            "{uid: '${callback.uid}'}",
            "{\$set: {state: '${State.COMPILATION_CHECK_FINISHED}'}}")
    if (callback.status != "SUCCESS") {
        sendMessage("Проверка пр ${pullRequest.author} на компиляцию завершилась со статусом ${callback.status}")
    }
}

data class PullRequestState(
        val uid: String,
        val author: String,
        val state: State
)

enum class State {
    OPEN,
    COMPILATION_CHECK_STARTED,
    COMPILATION_CHECK_FINISHED,
    REVIEWERS_SET
}

fun startJob(pullRequest: PullRequest) {
    val callBackUrl = "http://ugr-integration-tools1.yamoney.ru:8098/callback/${pullRequest.uid()}"
    println("Starting job for $pullRequest")
    val result = Request.Post("http://jenkins-ot.test.yamoney.ru:8096/jenkinsJob/run")
            .bodyForm(
                    Form.form()
                            .add("folder", "QA")
                            .add("folderUrl", "https://jenkins-dev.yamoney.ru/job/QA/")
                            .add("jobName", "TestJob")
                            .add("parameters", "branch=${pullRequest.fromRef.displayId}")
                            .add("callBackUrl", callBackUrl)
                            .build())
            .execute().returnContent().asString()
    println(result)
}

