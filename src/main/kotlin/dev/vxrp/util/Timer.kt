package dev.vxrp.util

import kotlinx.coroutines.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Timer(private val period: Duration) {
    fun runWithTimer(task: suspend () -> Unit) = runBlocking {
        var taskExecuted = false

        val currentTask: suspend () -> Unit = {
            if (runTaskNow { task() }) {
                taskExecuted = true
            }
        }

        startTimer(1.seconds, task = currentTask)
        delay(1.seconds)
        assert(taskExecuted)
    }

    private suspend fun runTaskNow(function: suspend () -> (Unit)): Boolean {
        function()

        return true
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