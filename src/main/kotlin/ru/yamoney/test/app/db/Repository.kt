package ru.yamoney.test.app.db

import org.litote.kmongo.KMongo

val db = KMongo.createClient(
        "reviewers_mongodb"
).getDatabase("reviewers") ?: throw IllegalStateException("Data base is null")