package dev.icerock.education.githubrepos.data.cloud_data_source

import dev.icerock.education.githubrepos.data.model.User

interface CloudDataSource {
    suspend fun isTokenValid(token: String): Boolean
    suspend fun getUser(): User
}