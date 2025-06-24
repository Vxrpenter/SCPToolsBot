/*
 * Copyright (c) 2024 Vxrpenter and the SCPToolsBot Contributors
 *
 * Licenced under the MIT License, any non-license compliant usage of this file(s) content
 * is prohibited. If you did not receive a copy of the license with this file, you
 * may obtain the license at
 *
 *  https://mit-license.org/
 *
 * This software may be used commercially if the usage is license compliant. The software
 * is provided without any sort of WARRANTY, and the authors cannot be held liable for
 * any form of claim, damages or other liabilities.
 *
 * Note: This is no legal advice, please read the license conditions
 */

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