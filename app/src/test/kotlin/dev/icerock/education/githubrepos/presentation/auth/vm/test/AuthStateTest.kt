package dev.icerock.education.githubrepos.presentation.auth.vm.test

import dev.icerock.education.githubrepos.presentation.auth.vm.AuthViewModel
import dev.icerock.education.githubrepos.presentation.auth.vm.AuthViewModelTest
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthStateTest : AuthViewModelTest() {

    @Test
    fun `init without saved token - Idle state`() {
        authViewModel
        assertTrue(actualState is AuthViewModel.State.Idle)
    }

    @Test
    fun `onPressedButton with valid token - Idle state to Loading state`() {
        val subject = authViewModel
        assertTrue(actualState is AuthViewModel.State.Idle)
        subject.onSignButtonPressed(TestUtils.VALID_TOKEN)
        assertTrue(actualState is AuthViewModel.State.Loading)
    }

    @Test
    fun `onPressedButton with invalid token - Idle to Loading to InvalidInput state's`() {
        authViewModel.onSignButtonPressed(TestUtils.INVALID_TOKEN)
        val cases = listOf(
            AuthViewModel.State.Idle::class,
            AuthViewModel.State.Loading::class,
            AuthViewModel.State.InvalidInput::class,
        )
        val allPastStates = allPastStates
        assertTrue(allPastStates.size == 3)
        cases.zip(allPastStates).forEach { assertTrue(it.first == it.second) }
        cases.zip(allPastStates).forEach { assertTrue(it.first == it.second) }
    }
}