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
        fun observe(owner: LifecycleOwner, observer: Observer<State>)
    }

    interface ChangeState<State> {
        fun change(state: State)
    }

    interface MutableState<State> : ObserveState<State>, ChangeState<State>

    interface CollectAction<Action> {
        fun collect(collector: FlowCollector<Action>)
    }

    interface PerformAction<Action> {
        suspend fun perform(action: Action)
    }

    interface MutableAction<Action> : CollectAction<Action>, PerformAction<Action>


    abstract class Abstract<State, Action>(
        protected val scope: CoroutineScope,
        protected val state: MutableLiveData<State>,
        protected val action: MutableStateFlow<Action>
    ) : MutableState<State>, MutableAction<Action> {
        override fun observe(owner: LifecycleOwner, observer: Observer<State>) =
            state.observe(owner, observer)

        override fun change(state: State) = this.state.postValue(state)

        override fun collect(collector: FlowCollector<Action>) {
            scope.launch { action.collect(collector) }

        }

        override suspend fun perform(action: Action) = this.action.emit(action)

    }
}
