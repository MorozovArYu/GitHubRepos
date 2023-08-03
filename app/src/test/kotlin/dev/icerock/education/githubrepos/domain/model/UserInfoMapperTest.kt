package dev.icerock.education.githubrepos.domain.model

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * @author Morozov Artem
 * This test describes {@link UserInfo# Mapper} and provides an example of its implementation
 * */

class UserInfoMapperTest {
    private lateinit var mapper: UserInfo.Mapper<TestModel>

    data class TestModel(
        private val firstField: String,
        private val secondField: String
    ) {
        class FromUserInfo : UserInfo.Mapper<TestModel> {
            override fun map(userInfo: UserInfo.Error): TestModel {
                return TestModel(
                    firstField = TestUtils.TEST_MODEL_FIELD_FIRST,
                    secondField = userInfo.message
                )
            }

            override fun map(userInfo: UserInfo.Success): TestModel {
                return TestModel(
                    firstField = userInfo.ownerName,
                    secondField = TestUtils.TEST_MODEL_FIELD_SECOND
                )
            }
        }

    }

    object TestUtils {
        private const val OWNER_NAME = "Owner Name"
        private const val ERROR_MESSAGE = "Error Message"
        const val TEST_MODEL_FIELD_FIRST = "Field First"
        const val TEST_MODEL_FIELD_SECOND = "Field Second"
        val SUCCESS_USER_INFO = UserInfo.Success(ownerName = OWNER_NAME)
        val ERROR_USER_INFO = UserInfo.Error(message = ERROR_MESSAGE)
        val SUCCESS_TEST_MODEL = TestModel(OWNER_NAME, TEST_MODEL_FIELD_SECOND)
        val ERROR_TEST_MODEL = TestModel(TEST_MODEL_FIELD_FIRST, ERROR_MESSAGE)

    }

    @Before
    fun setUp() {
        mapper = TestModel.FromUserInfo()
    }

    @Test
    fun `test success UserInfo mapper`() {
        val actual = TestUtils.SUCCESS_USER_INFO.map(mapper)
        val expected = TestUtils.SUCCESS_TEST_MODEL
        assertEquals(expected, actual)
    }

    @Test
    fun `test error UserInfo mapper`() {
        val actual = TestUtils.ERROR_USER_INFO.map(mapper)
        val expected = TestUtils.ERROR_TEST_MODEL
        assertEquals(expected, actual)
    }


}