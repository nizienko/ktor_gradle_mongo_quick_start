package ru.yamoney.test.app.data

data class WebHook(
        val eventKey: String,
        val actor: User,
        val pullRequest: PullRequest
)


data class PullRequest(
        val id: Int,
        val title: String,
        val state: String,
        val open: Boolean,
        val createdDate: Long,
        val updatedDate: Long,
        val fromRef: FromRef,
        val reviewers: List<Reviewer>
) {
    fun uid() = "${this.fromRef.repository.name}_${this.id}"
}

data class Reviewer(
        val user: User,
        val role: String,
        val approved: Boolean,
        val status: String
)

data class User(
        val name: String,
        val emailAddress: String,
        val id: Long,
        val displayName: String,
        val active: Boolean
)

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