package ru.yamoney.test.app.services

import org.apache.http.client.fluent.Form
import org.apache.http.client.fluent.Request
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection
import ru.yamoney.test.app.data.PullRequest
import ru.yamoney.test.app.data.WebHook
import ru.yamoney.test.app.db.db


fun processWebHook(webHook: WebHook) {
    when (webHook.eventKey) {
        "pr:opened" -> {
            db.getCollection<PullRequest>().insertOne(webHook.pullRequest)
            startJob(webHook.pullRequest)
        }
        "pr:deleted" -> db.getCollection<PullRequest>().deleteOne(webHook.pullRequest::id eq webHook.pullRequest.id)
        else -> println(webHook)
    }
}

data class AutorunResponse(val status: String, val message: String)

fun startJob(pullRequest: PullRequest) {
    val callBackUrl = "http://ugr-integration-tools1.yamoney.ru:8098/callback/${pullRequest.uId()}"
    println("Starting job for $pullRequest")
    val result = Request.Post("http://jenkins-ot.test.yamoney.ru:8096/jenkinsJob/run")
            .bodyForm(
                    Form.form()
                            .add("folder", "QA")
                            .add("folderUrl", "https://jenkins-dev.yamoney.ru/job/QA/")
                            .add("jobName", "TestJob")
                            .add("parameters", "branch=testBranch")
                            .add("callBackUrl", callBackUrl)
                            .build())
            .execute().returnContent().asString()
    println(result)
}

