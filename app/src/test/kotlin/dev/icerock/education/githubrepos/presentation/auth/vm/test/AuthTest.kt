package dev.icerock.education.githubrepos.presentation.auth.vm.test

import dev.icerock.education.githubrepos.presentation.auth.vm.AuthViewModelTest
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthTest : AuthViewModelTest() {
    @Test
    fun `onPressedButton with valid token - token saved to storage`() {
        authViewModel.onSignButtonPressed(TestUtils.VALID_TOKEN)
        assertTrue(keyValueStorage.token == TestUtils.VALID_TOKEN)
    }
}