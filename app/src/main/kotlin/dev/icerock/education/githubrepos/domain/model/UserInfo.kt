package dev.icerock.education.githubrepos.domain.model

sealed interface UserInfo {
    data class Success(val ownerName: String) : UserInfo
    class Error(val message: String) : UserInfo
}
