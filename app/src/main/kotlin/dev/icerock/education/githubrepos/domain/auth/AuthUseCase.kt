package dev.icerock.education.githubrepos.domain.auth


import dev.icerock.education.githubrepos.domain.AppRepository
import dev.icerock.education.githubrepos.domain.model.UserInfo

interface AuthUseCase {
    suspend fun auth(token: String): UserInfo

    class Base(private val repository: AppRepository) : AuthUseCase {
        override suspend fun auth(token: String): UserInfo {
            return repository.signIn(token)
        }

    }

}