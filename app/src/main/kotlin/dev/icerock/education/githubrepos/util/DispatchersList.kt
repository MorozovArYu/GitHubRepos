package dev.icerock.education.githubrepos.util

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersList {
    val IO: CoroutineDispatcher
    val MAIN: CoroutineDispatcher
}