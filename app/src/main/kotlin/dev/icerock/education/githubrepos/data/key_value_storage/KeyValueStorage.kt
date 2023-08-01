package dev.icerock.education.githubrepos.data.key_value_storage

interface KeyValueStorage  {
    fun getToken(): String
    fun saveToken(token: String)
    class Base(): KeyValueStorage{
        var authToken: String? = null
        override fun getToken(): String {
            TODO("Not yet implemented")
        }

        override fun saveToken(token: String) {
            TODO("Not yet implemented")
        }
    }

}