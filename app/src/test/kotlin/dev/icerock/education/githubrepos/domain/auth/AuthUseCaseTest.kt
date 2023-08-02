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
        const val VALID_TOKEN = "valid token"
        const val INVALID_TOKEN = "invalid token"
        val USER_INFO_SUCCESS = UserInfo.Success(OWNER_NAME)
        val AUTH_RESULT_SUCCESS = AuthUseCase.AuthResult.Success(USER_INFO_SUCCESS)
        val USER_INFO_ERROR = UserInfo.Error(message = ERROR_MESSAGE)
        val AUTH_RESULT_ERROR = AuthUseCase.AuthResult.Error(USER_INFO_ERROR.message)
    }

    @Before
    fun setUp() {
        repository = TestAppRepository()
        authUseCase = AuthUseCase.Base(
            repository
        )
    }


}