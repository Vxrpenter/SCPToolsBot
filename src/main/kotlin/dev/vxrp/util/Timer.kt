package dev.vxrp.util

import kotlinx.coroutines.*
import kotlin.time.Duration

class Timer(private val period: Duration) {
    fun runWithTimer(task: suspend () -> Unit) = runBlocking {
        var taskExecuted = false

        val currentTask: suspend () -> Unit = {
            task()

            taskExecuted = true
        }

        startTimer(task = currentTask)
        assert(taskExecuted)
    }

    private fun CoroutineScope.startTimer(task: suspend () -> Unit): Job {
        return launch {
            while (isActive) {
                task()
                delay(period)
            }
        }
    }
}