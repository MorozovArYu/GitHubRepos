package dev.icerock.education.githubrepos.domain

import dev.icerock.education.githubrepos.domain.model.UserInfo

interface AppRepository {
    suspend fun signIn(token: String): UserInfo

    companion object {
        const val TYPE_AUTO = "Type Auto"
    }
}