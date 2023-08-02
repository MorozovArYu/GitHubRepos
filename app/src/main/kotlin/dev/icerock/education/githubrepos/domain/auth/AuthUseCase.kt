package dev.icerock.education.githubrepos.domain.auth


import dev.icerock.education.githubrepos.domain.AppRepository
import dev.icerock.education.githubrepos.domain.model.UserInfo

interface AuthUseCase {
    suspend fun auth(authType: AuthType = AuthType.Auto): UserInfo

    class Base(private val repository: AppRepository) : AuthUseCase {
        override suspend fun auth(authType: AuthType): UserInfo {
            val userInfo: UserInfo = repository.signIn(authType.token)
            return userInfo
        }

    }

    sealed class AuthType(val token: String) {
        object Auto : AuthType(AppRepository.TYPE_AUTO)
        class Manual(token: String) : AuthType(token)
    }
}