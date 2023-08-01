package dev.icerock.education.githubrepos.data.repository

import dev.icerock.education.githubrepos.data.exeption.InvalidTokenException
import dev.icerock.education.githubrepos.model.UserInfo
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class LogInTest: AppRepositoryTest() {

    @Test(expected = InvalidTokenException::class)
    fun `sign in with invalid token - throws InvalidTokenException`() = runTest {
        val token: String = TOKEN_INVALID
        repository.signIn(token)
    }

    @Test
    fun `sign in with valid token - returns UserInfo`() = runTest {
        val token: String = TOKEN_VALID
        val actual: UserInfo = repository.signIn(token)
        val expected = UserInfo(ownerName = MODEL_DATA)
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `valid token - save to cache`() = runTest {
        val token = TOKEN_VALID
        repository.signIn(token)
        Assert.assertTrue(keyValueStorage.isTokenSaved())
    }

    @Test
    fun `invalid token - don't save to cache`() = runTest {
        val token = TOKEN_INVALID
        try {
            repository.signIn(token)
        } catch (e: Exception) {
            Assert.assertTrue(!keyValueStorage.isTokenSaved())
        }
        Assert.assertTrue(!keyValueStorage.isTokenSaved())
    }

    @Test
    fun `cache isn't empty - use cached token`() = runTest {
        keyValueStorage.saveToken(TOKEN_VALID)
        val actual = repository.signIn()
        val expected = UserInfo(ownerName = MODEL_DATA)
        Assert.assertEquals(expected, actual)
    }
}