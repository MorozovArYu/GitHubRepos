package dev.icerock.education.githubrepos.presentation.auth.vm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.education.githubrepos.data.KeyValueStorage
import dev.icerock.education.githubrepos.domain.auth.AuthUseCase
import dev.icerock.education.githubrepos.domain.model.UserInfo
import dev.icerock.education.githubrepos.presentation.communication.Communication
import dev.icerock.education.githubrepos.util.DispatchersList
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

interface AuthViewModel :
    Communication.ObserveState<AuthViewModel.State>,
    Communication.CollectAction<AuthViewModel.Action> {

    fun onSignButtonPressed(token: String)

    sealed interface State {
        object Idle : State
        object Loading : State
        data class InvalidInput(val reason: String) : State
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
        object RouteToMain : Action
    }


    class Base(
        private val interactor: AuthUseCase,
        private val keyValueStorage: KeyValueStorage,
        private val dispatchers: DispatchersList,
        private val state: Communication.MutableState<State>,
        private val action: Communication.MutableAction<Action>
    ) : ViewModel(),
        AuthViewModel {

        init {
            val token = keyValueStorage.token
            if (token == null) state.change(State.Idle)
            else {
                launchWithErrorHandle {
                    interactor.auth(token)
                    action.perform(Action.RouteToMain)
                }
            }
        }

        override fun onSignButtonPressed(token: String) {
            launchWithErrorHandle {
                state.change(State.Loading)
                when (val userInfo = interactor.auth(token)) {
                    is UserInfo.Success -> {
                        action.perform(Action.RouteToMain)
                        keyValueStorage.token = token
                    }

                    is UserInfo.Error -> {
                        state.change(State.InvalidInput(userInfo.message))
                    }
                }
            }
        }

        override fun collect(collector: FlowCollector<Action>) {
            action.collect(collector)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<State>) {
            state.observe(owner, observer)
        }

        private fun launchWithErrorHandle(block: suspend () -> Unit) {
            val errorHandler = CoroutineExceptionHandler { _, throwable ->
                viewModelScope.plus(dispatchers.IO).launch {
                    action.perform(Action.ShowError(throwable.message.toString()))
                }
            }
            viewModelScope.plus(dispatchers.IO).plus(errorHandler).launch {
                block.invoke()
            }
        }


    }


}

