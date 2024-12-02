package dev.vxrp.util

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import kotlin.time.Duration

class Timer {
    fun runWithTimer(period: Duration, task: suspend () -> Unit) = runBlocking {
        var taskExecuted = false

        val currentTask: suspend () -> Unit = {
            task()

            taskExecuted = true
        }


        startTimer(task = currentTask, period = period)
        assert(taskExecuted)
    }

    fun runLooped(task: suspend () -> Unit) = runBlocking {
        var taskExecuted = false

        val currentTask: suspend () -> Unit = {
            task()

            taskExecuted = true
        }

        loop(task = currentTask)
        assert(taskExecuted)
    }

    private fun loop(task: suspend () -> Unit) {
        timerScope.launch {
            launch {
                while (isActive) {
                    task()
                }
            }
        }
    }

    private fun startTimer(period: Duration, task: suspend () -> Unit) {
        timerScope.launch {
            launch {
                while (isActive) {
                    task()
                    delay(period)
                }
            }
        }
    }

    private val timerScope = CoroutineScope(CoroutineExceptionHandler { _, exception ->
        LoggerFactory.getLogger(javaClass).error("An error occurred in the timer coroutine", exception)
    }) + SupervisorJob()
}