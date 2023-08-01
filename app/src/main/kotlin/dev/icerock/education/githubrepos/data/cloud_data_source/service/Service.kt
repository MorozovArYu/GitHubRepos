package dev.icerock.education.githubrepos.data.cloud_data_source.service

import dev.icerock.education.githubrepos.data.model.User
import retrofit2.Response

interface Service {
    suspend fun getUserInfo(token: String): Response<User>
}