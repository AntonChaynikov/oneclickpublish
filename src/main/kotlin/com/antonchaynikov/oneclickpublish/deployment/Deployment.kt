package com.antonchaynikov.oneclickpublish.deployment

import java.util.*

class Deployment(vararg stages: Stage) {

    private val deployStages = ArrayDeque<Stage>()
    private val executedStages = ArrayDeque<Stage>()

    init {
        stages.forEach { deployStages.addLast(it) }
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
                try {
                    executedStages.pop().revert()
                } catch (undoErr: Exception) {
                    undoErr.printStackTrace()
                    // error happened, no point to continue
                    executedStages.clear()
                }
            }
            throw e
        }
    }
}
