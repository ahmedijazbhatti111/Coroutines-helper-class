package com.jpgtopdf.util

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import javax.inject.Inject

class Executor @Inject constructor()
{
    private var mainThreadHandler: CoroutineScope = MainScope()
    private var workerThreadHandler: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var defaultThreadHandler: CoroutineScope = CoroutineScope(Dispatchers.Default)

    fun release() {
        mainThreadHandler.cancel()
        workerThreadHandler.cancel()
        defaultThreadHandler.cancel()

        mainThreadHandler = MainScope()
        workerThreadHandler = CoroutineScope(Dispatchers.IO)
        defaultThreadHandler = CoroutineScope(Dispatchers.Default)
    }

    fun runWorker(onComplete:(()->Unit)?=null,callback: () -> Unit) {
        workerThreadHandler.launch { callback() }.invokeOnCompletion {
            onComplete?.invoke()
        }
    }
    fun runDefaultWorker(callback: () -> Unit) {
        defaultThreadHandler.launch { callback() }
    }

    fun runMain(callback: () -> Unit) {
        mainThreadHandler.launch { callback() }
    }

    fun runMain(delay:Long,callback: () -> Unit) {
        mainThreadHandler.launch {
            delay(delay)
            callback()
        }
    }

    fun runWorker(delay:Long,callback: () -> Unit) {
        workerThreadHandler.launch {
            delay(delay)
            callback()
        }
    }

    fun runDefaultWorker(delay:Long,callback: () -> Unit) {
        defaultThreadHandler.launch {
            delay(delay)
            callback()
        }
    }
}