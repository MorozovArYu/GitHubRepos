package dev.icerock.education.githubrepos.domain.auth


import dev.icerock.education.githubrepos.domain.AppRepository
import dev.icerock.education.githubrepos.domain.model.UserInfo

interface AuthUseCase {
    suspend fun auth(authType: AuthType = AuthType.Auto): AuthResult

    class Base(private val repository: AppRepository) : AuthUseCase {
        override suspend fun auth(authType: AuthType): AuthResult {
            return when (val userInfo = repository.signIn(authType.token)) {
                is UserInfo.Success -> AuthResult.Success(userInfo)
                is UserInfo.Error -> AuthResult.Error(userInfo.message)
            }
        }

    }


    sealed class AuthType(val token: String) {
        object Auto : AuthType(AppRepository.TYPE_AUTO)
        class Manual(token: String) : AuthType(token)
    }

    sealed interface AuthResult {
        data class Success(private var userInfo: UserInfo) : AuthResult
        data class Error(private var message: String) : AuthResult
    }
}