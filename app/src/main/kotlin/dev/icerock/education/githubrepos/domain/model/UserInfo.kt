package dev.icerock.education.githubrepos.domain.model

sealed interface UserInfo {
    fun <T> map(mapper: Mapper<T>): T
    data class Success(val ownerName: String) : UserInfo {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(this)
    }

    class Error(val message: String) : UserInfo {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(this)
    }

    interface Mapper<T> {
        fun map(userInfo: UserInfo.Success): T
        fun map(userInfo: UserInfo.Error): T
    }
}
