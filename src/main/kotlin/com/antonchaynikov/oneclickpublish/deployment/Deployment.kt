package com.antonchaynikov.oneclickpublish.deployment

import java.util.*

class Deployment(vararg stages: Stage) {

    private val deployStages = ArrayDeque<Stage>()
    private val executedStages = ArrayDeque<Stage>()

    init {
        deployStages.apply {
            stages.forEach { addLast(it) }
        }
    }

    fun deploy() {
        try {
            while (deployStages.isNotEmpty()) {
                val stage = deployStages.removeFirst()
                stage.execute()
                executedStages.push(stage)
            }
        } catch (e: Exception) {
            while (executedStages.isNotEmpty()) {
                executedStages.pop().revert()
            }
            throw e
        }
    }
}
