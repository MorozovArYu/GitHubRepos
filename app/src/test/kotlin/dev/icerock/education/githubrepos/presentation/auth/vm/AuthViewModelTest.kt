package dev.icerock.education.githubrepos.presentation.auth.vm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import dev.icerock.education.githubrepos.data.KeyValueStorage
import dev.icerock.education.githubrepos.domain.auth.AuthUseCase
import dev.icerock.education.githubrepos.domain.model.UserInfo
import dev.icerock.education.githubrepos.presentation.communication.Communication
import dev.icerock.education.githubrepos.util.DispatchersList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import kotlin.reflect.KClass

class TestStateCommunication : Communication.MutableState<AuthViewModel.State> {
    var lastState: AuthViewModel.State? = null
    val statesList: MutableList<KClass<out AuthViewModel.State>> = mutableListOf()
    override fun observe(
        owner: LifecycleOwner, observer: Observer<AuthViewModel.State>
    ) = Unit

    override fun change(state: AuthViewModel.State) {
        statesList.add(state::class)
        this.lastState = state
    }
}

class TestActionCommunication : Communication.MutableAction<AuthViewModel.Action> {
    var action: AuthViewModel.Action? = null
    override fun collect(collector: FlowCollector<AuthViewModel.Action>) = Unit
    override suspend fun perform(action: AuthViewModel.Action) {
        this.action = action
    }
}

class TestKeyValueStorage : KeyValueStorage {
    override var token: String? = null
    fun setInvalidToken() {
        token = AuthViewModelTest.TestUtils.INVALID_TOKEN
    }

    fun setValidToken() {
        token = AuthViewModelTest.TestUtils.VALID_TOKEN
    }
}

class TestAuthUseCase : AuthUseCase {
    private var isError = false
    fun setError() {
        isError = true
    }

    override suspend fun auth(token: String): UserInfo {
        if (isError) throw AuthViewModelTest.TestUtils.ERROR
        return if (token == AuthViewModelTest.TestUtils.VALID_TOKEN) AuthViewModelTest.TestUtils.SUCCESS_USER_INFO else AuthViewModelTest.TestUtils.ERROR_USER_INFO
    }
}

object TestDispatchers : DispatchersList {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val IO = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val MAIN = UnconfinedTestDispatcher()
}

abstract class AuthViewModelTest {
    protected lateinit var keyValueStorage: TestKeyValueStorage
    protected lateinit var interactor: TestAuthUseCase
    private lateinit var dispatchers: TestDispatchers
    private lateinit var stateCommunication: TestStateCommunication
    private lateinit var actionCommunication: TestActionCommunication
    protected val authViewModel
        get() = AuthViewModel.Base(
            interactor = interactor,
            keyValueStorage = keyValueStorage,
            dispatchers = dispatchers,
            state = stateCommunication,
            action = actionCommunication,
        )
    protected val actualState
        get() = stateCommunication.lastState
    protected val allPastStates
        get() = stateCommunication.statesList
    protected val actualAction
        get() = actionCommunication.action

    @Before
    fun setUp() {
        dispatchers = TestDispatchers
        keyValueStorage = TestKeyValueStorage()
        interactor = TestAuthUseCase()
        stateCommunication = TestStateCommunication()
        actionCommunication = TestActionCommunication()
    }

    object TestUtils {
        private const val OWNER_NAME = "Owner Name"
        private const val ERROR_MESSAGE = "Error Message"
        const val VALID_TOKEN = "Valid Token"
        const val INVALID_TOKEN = "Invalid Token"
        val SUCCESS_USER_INFO: UserInfo = UserInfo.Success(OWNER_NAME)
        val ERROR_USER_INFO: UserInfo = UserInfo.Error(ERROR_MESSAGE)
        val ERROR = Exception(ERROR_MESSAGE)
    }
}





