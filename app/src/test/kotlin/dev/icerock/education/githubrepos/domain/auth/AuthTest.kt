package dev.icerock.education.githubrepos.domain.auth

import dev.icerock.education.githubrepos.domain.model.UserInfo
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class AuthTest : AuthUseCaseTest() {

    @Test
    fun `valid token - success auth`() = runTest {
        val actual: UserInfo = authUseCase.auth(TestUtils.VALID_TOKEN)
        val expected: UserInfo = TestUtils.USER_INFO_SUCCESS
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun `invalid token - error auth`() = runTest {
        val actual: UserInfo =
            authUseCase.auth(TestUtils.INVALID_TOKEN)
        val expected: UserInfo = TestUtils.USER_INFO_ERROR
        Assert.assertEquals(actual, expected)
    }
}