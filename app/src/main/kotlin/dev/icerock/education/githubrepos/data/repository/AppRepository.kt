package dev.icerock.education.githubrepos.data.repository

import dev.icerock.education.githubrepos.data.cloud_data_source.CloudDataSource
import dev.icerock.education.githubrepos.data.exeption.InvalidTokenException
import dev.icerock.education.githubrepos.data.key_value_storage.KeyValueStorage
import dev.icerock.education.githubrepos.data.model.User
import dev.icerock.education.githubrepos.model.UserInfo

interface AppRepository {
    suspend fun signIn(token: String = NO_TOKEN): UserInfo

    class Base(
        private val cloudDataSource: CloudDataSource,
        private val mapper: User.Mapper<UserInfo>,
        private val keyValueStorage: KeyValueStorage
    ) : AppRepository {
        override suspend fun signIn(token: String): UserInfo {
            val user = if (token == NO_TOKEN){
                keyValueStorage.getToken()
                cloudDataSource.getUser()
            } else {
                if(!cloudDataSource.isTokenValid(token)) throw InvalidTokenException(token)
                keyValueStorage.saveToken(token)
                cloudDataSource.getUser()
            }
            return user.map(mapper)
        }
    }

    companion object{
        private const val NO_TOKEN = "no token"
    }
}