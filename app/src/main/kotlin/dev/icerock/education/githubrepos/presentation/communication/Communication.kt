package dev.icerock.education.githubrepos.presentation.communication

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface Communication {
    interface ObserveState<State> {
        fun observeState(owner: LifecycleOwner, observer: Observer<State>)
    }

    interface ChangeState<State> {
        fun changeState(state: State)
    }

    interface MutableState<State> : ObserveState<State>, ChangeState<State>

    interface CollectAction<Action> {
        fun collectAction(collector: FlowCollector<Action>)
    }

    interface PerformAction<Action> {
        suspend fun performAction(action: Action)
    }

    interface MutableAction<Action> : CollectAction<Action>, PerformAction<Action>

    interface Mutable<State, Action> :
        MutableState<State>,
        MutableAction<Action>


    abstract class Abstract<State, Action>(
        protected val scope: CoroutineScope,
        protected val state: MutableLiveData<State>,
        protected val action: MutableStateFlow<Action>
    ) : Mutable<State,Action> {
        override fun observeState(owner: LifecycleOwner, observer: Observer<State>) =
            state.observe(owner, observer)

        override fun changeState(state: State) = this.state.postValue(state)

        override fun collectAction(collector: FlowCollector<Action>) {
            scope.launch { action.collect(collector) }

        }

        override suspend fun performAction(action: Action) = this.action.emit(action)

    }
}
