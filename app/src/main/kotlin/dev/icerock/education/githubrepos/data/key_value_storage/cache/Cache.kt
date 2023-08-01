package dev.icerock.education.githubrepos.data.key_value_storage.cache

interface Cache {
    fun saveToken(token: String)

    fun getToken(): String
}