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

abstract class AuthViewModelTest {
    protected lateinit var keyValueStorage: TestKeyValueStorage
    protected lateinit var interactor: TestAuthUseCase
    private lateinit var dispatchers: TestDispatchers
    private lateinit var communication: TestCommunication
    protected val authViewModel
        get() = AuthViewModel.Base(
            interactor = interactor,
            keyValueStorage = keyValueStorage,
            dispatchers = dispatchers,
            communication = communication,
        )
    protected val actualState
        get() = communication.lastState
    protected val allPastStates
        get() = communication.statesList
    protected val actualAction
        get() = communication.action

    @Before
    fun setUp() {
        dispatchers = TestDispatchers
        keyValueStorage = TestKeyValueStorage()
        interactor = TestAuthUseCase()
        communication = TestCommunication()
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


class TestCommunication :
    Communication.Mutable<AuthViewModel.State, AuthViewModel.Action>{
    var lastState: AuthViewModel.State? = null
    var action: AuthViewModel.Action? = null
    val statesList: MutableList<KClass<out AuthViewModel.State>> = mutableListOf()

    override fun observeState(
        owner: LifecycleOwner, observer: Observer<AuthViewModel.State>
    ) = Unit

    override fun changeState(state: AuthViewModel.State) {
        statesList.add(state::class)
        this.lastState = state
    }

    override fun collectAction(collector: FlowCollector<AuthViewModel.Action>) = Unit
    override suspend fun performAction(action: AuthViewModel.Action) {
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




