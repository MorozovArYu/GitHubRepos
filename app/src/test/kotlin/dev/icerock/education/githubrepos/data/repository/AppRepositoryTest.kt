package dev.icerock.education.githubrepos.data.repository


import dev.icerock.education.githubrepos.data.cloud_data_source.CloudDataSource
import dev.icerock.education.githubrepos.data.key_value_storage.KeyValueStorage
import dev.icerock.education.githubrepos.data.model.User
import dev.icerock.education.githubrepos.model.UserInfo
import org.junit.Before


// TODO Maybe it would be better if i moc mapper and make another test for it
abstract class AppRepositoryTest {
    private lateinit var cloudDataSource: TestCloudDataSource
    private lateinit var mapper: User.Mapper<UserInfo>
    protected lateinit var repository: AppRepository
    protected lateinit var keyValueStorage: TestKeyValueStorage

    private class TestCloudDataSource : CloudDataSource {
        private val user = User(ownerName = MODEL_DATA)

        override suspend fun isTokenValid(token: String): Boolean = token == TOKEN_VALID
        override suspend fun getUser(): User = user

    }
    protected class TestKeyValueStorage() : KeyValueStorage {

        private var token: String? = null
        override fun getToken(): String {
            return token ?: ""
        }


        override fun saveToken(token: String) {
            this.token = token
        }

        fun isTokenSaved() = token != null

    }

    @Before
    fun setUp() {
        cloudDataSource = TestCloudDataSource()
        mapper = User.Mapper.ToUserInfo()
        keyValueStorage = TestKeyValueStorage()
        repository = AppRepository.Base(
            cloudDataSource = cloudDataSource,
            mapper = mapper,
            keyValueStorage = keyValueStorage
        )
    }


    companion object {
        const val TOKEN_VALID = "valid token"
        const val TOKEN_INVALID = "invalid token"
        const val MODEL_DATA = "model data"
    }
}

