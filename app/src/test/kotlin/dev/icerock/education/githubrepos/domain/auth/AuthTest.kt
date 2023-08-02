package dev.icerock.education.githubrepos.domain.auth

import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class AuthTest: AuthUseCaseTest() {

    @Test
    fun `valid token - success manual auth`() = runTest {
        val actual = authUseCase.auth(AuthUseCase.AuthType.Manual(TestUtils.VALID_TOKEN))
        val expected = TestUtils.AUTH_RESULT_SUCCESS
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun `invalid token - error manual auth`() = runTest {
        val actual = authUseCase.auth(AuthUseCase.AuthType.Manual(TestUtils.INVALID_TOKEN))
        val expected = TestUtils.AUTH_RESULT_ERROR
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun `valid token - success auto auth`() = runTest {
        val actual = authUseCase.auth()
        val expected = TestUtils.AUTH_RESULT_SUCCESS
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun `invalid token - error auto auth`() = runTest {
        repository.setInvalidToken()
        val actual = authUseCase.auth()
        val expected = TestUtils.AUTH_RESULT_ERROR
        Assert.assertEquals(actual, expected)
    }
}