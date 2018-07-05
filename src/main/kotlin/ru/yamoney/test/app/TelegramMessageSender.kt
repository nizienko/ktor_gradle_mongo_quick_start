package ru.yamoney.test.app

import org.apache.http.client.fluent.Form
import org.apache.http.client.fluent.Request
import java.net.URLEncoder

const val USER_STORAGE_URL = "http://jenkins-ot.test.yamoney.ru:8095"


fun sendMessage(message: String, subscription: String = "jira-integration-team") {
    Request.Post("$USER_STORAGE_URL/notification/store")
            .bodyForm(
                    Form.form()
                            .add("type", URLEncoder.encode(subscription, "UTF-8"))
                            .add("message", URLEncoder.encode(message, "UTF-8"))
                            .build())
            .execute().returnContent().asString()
}


fun main(args: Array<String>) {
    sendMessage("Я не моюсь")
}