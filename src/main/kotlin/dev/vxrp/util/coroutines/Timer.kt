package dev.vxrp.util.coroutines

import kotlinx.coroutines.*
import kotlin.time.Duration

class Timer{
    fun runWithTimer(period: Duration, coroutineScope: CoroutineScope, task: suspend () -> Unit) = runBlocking {
        var taskExecuted = false

        val currentTask: suspend () -> Unit = {
            task()

            taskExecuted = true
        }

        startTimer(period, coroutineScope, currentTask)
        assert(taskExecuted)
    }

    fun runLooped(coroutineScope: CoroutineScope, task: suspend () -> Unit) = runBlocking {
        var taskExecuted = false

        val currentTask: suspend () -> Unit = {
            task()

            taskExecuted = true
        }

        loop(coroutineScope, task = currentTask)
        assert(taskExecuted)
    }

    private fun loop(coroutineScope: CoroutineScope, task: suspend () -> Unit) {
        coroutineScope.launch {
            launch {
                while (isActive) {
                    task()
                }
            }
        }
    }

    private fun startTimer(period: Duration, coroutineScope: CoroutineScope, task: suspend () -> Unit) {
        coroutineScope.launch {
            launch {
                while (isActive) {
                    task()
                    delay(period)
                }
            }
        }
    }
}