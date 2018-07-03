package ru.yamoney.test.app.data

data class WebHook(
        val eventKey: String,
        val actor: Actor,
        val pullRequest: PullRequest
)

data class Actor(
        val name: String,
        val emailAddress: String,
        val id: Int
)


data class PullRequest(
        val id: Int,
        val title: String,
        val state: String,
        val open: Boolean,
        val createdDate: Long,
        val updatedDate: Long,
        val fromRef: FromRef,
        val reviewers: List<String>
) {
    fun uId() = "${this.fromRef.repository.name}_${this.id}"
}

data class FromRef(
        val repository: Repository,
        val id: String,
        val displayId: String
)

data class Repository(
        val slug: String,
        val id: Int,
        val name: String,
        val project: Project
)

data class Project(
        val key: String,
        val id: Int,
        val name: String,
        val description: String
)