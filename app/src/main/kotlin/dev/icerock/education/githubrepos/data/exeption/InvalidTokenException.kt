package dev.icerock.education.githubrepos.data.exeption

class InvalidTokenException(
    token: String,
) : RuntimeException(
    "$token $INVALID_TOKEN_MESSAGE",
) {
    private companion object {
        const val INVALID_TOKEN_MESSAGE = "is invalid"
    }
}