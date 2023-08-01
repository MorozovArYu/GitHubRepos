package dev.icerock.education.githubrepos.data.model

import dev.icerock.education.githubrepos.model.UserInfo

data class User(private val ownerName: String) {

    interface Mapper<T> {
        fun map(input: User): T

        class ToUserInfo : Mapper<UserInfo> {
            override fun map(user: User): UserInfo {
                return UserInfo(
                    user.ownerName
                )
            }

        }
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(this)
}
