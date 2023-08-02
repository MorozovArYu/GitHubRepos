package dev.icerock.education.githubrepos.domain.auth

import dev.icerock.education.githubrepos.domain.model.UserInfo
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class AuthTest : AuthUseCaseTest() {

    @Test
    fun `valid token - success manual auth`() = runTest {
        val actual: UserInfo = authUseCase.auth(AuthUseCase.AuthType.Manual(TestUtils.VALID_TOKEN))
        val expected: UserInfo = TestUtils.USER_INFO_SUCCESS
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun `invalid token - error manual auth`() = runTest {
        val actual: UserInfo =
            authUseCase.auth(AuthUseCase.AuthType.Manual(TestUtils.INVALID_TOKEN))
        val expected: UserInfo = TestUtils.USER_INFO_ERROR
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun `valid token - success auto auth`() = runTest {
        val actual: UserInfo = authUseCase.auth()
        val expected: UserInfo = TestUtils.USER_INFO_SUCCESS
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun `invalid token - error auto auth`() = runTest {
        repository.setInvalidToken()
        val actual: UserInfo = authUseCase.auth()
        val expected: UserInfo = TestUtils.USER_INFO_ERROR
        Assert.assertEquals(actual, expected)
    }
}