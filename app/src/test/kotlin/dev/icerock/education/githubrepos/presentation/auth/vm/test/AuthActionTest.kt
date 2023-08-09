package dev.icerock.education.githubrepos.presentation.auth.vm.test

import dev.icerock.education.githubrepos.presentation.auth.vm.AuthViewModel
import dev.icerock.education.githubrepos.presentation.auth.vm.AuthViewModelTest
import org.junit.Assert.assertTrue
import org.junit.Test


class AuthActionTest : AuthViewModelTest() {
    @Test
    fun `init with saved token - RouteToMain action`() {
        keyValueStorage.setValidToken()
        authViewModel
        assertTrue(actualAction is AuthViewModel.Action.RouteToMain)
    }

    @Test
    fun `onPressedButton with valid token - RouteToMain action`() {
        authViewModel.onSignButtonPressed(TestUtils.VALID_TOKEN)
        assertTrue(actualAction is AuthViewModel.Action.RouteToMain)
    }

    @Test
    fun `onPressedButton with interactor error - ShowError action`() {
        interactor.setError()
        authViewModel.onSignButtonPressed(TestUtils.VALID_TOKEN)
        assertTrue(actualAction is AuthViewModel.Action.ShowError)
    }

    @Test
    fun `init with interactor error - ShowError action`() {
        keyValueStorage.setValidToken()
        interactor.setError()
        authViewModel
        assertTrue(actualAction is AuthViewModel.Action.ShowError)
    }

}