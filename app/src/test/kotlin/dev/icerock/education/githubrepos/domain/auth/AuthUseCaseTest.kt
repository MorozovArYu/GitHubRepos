package dev.icerock.education.githubrepos.domain.auth

import dev.icerock.education.githubrepos.domain.AppRepository
import dev.icerock.education.githubrepos.domain.model.UserInfo
import org.junit.Before

abstract class AuthUseCaseTest {

    protected lateinit var authUseCase: AuthUseCase
    protected lateinit var repository: TestAppRepository

    class TestAppRepository : AppRepository {
        private var token = TestUtils.VALID_TOKEN
        fun setInvalidToken() {
            token = TestUtils.INVALID_TOKEN
        }


        override suspend fun signIn(token: String): UserInfo {
            if (token == AppRepository.TYPE_AUTO) return signIn(this.token)
            if (token == TestUtils.VALID_TOKEN) return TestUtils.USER_INFO_SUCCESS
            return TestUtils.USER_INFO_ERROR
        }


    }

    object TestUtils {
        private const val ERROR_MESSAGE = "Error Message"
        private const val OWNER_NAME = "Owner Name"
        const val VALID_TOKEN = "Valid Token"
        const val INVALID_TOKEN = "Invalid Token"
        val USER_INFO_SUCCESS = UserInfo.Success(ownerName = OWNER_NAME)
        val USER_INFO_ERROR = UserInfo.Error(message = ERROR_MESSAGE)
    }

    @Before
    fun setUp() {
        repository = TestAppRepository()
        authUseCase = AuthUseCase.Base(
            repository
        )
    }


}