package com.antonchaynikov.oneclickpublish.deployment

import com.antonchaynikov.oneclickpublish.TestException
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertTrue

internal class DeploymentTest : Spek({

    Feature("Deployment") {
        Scenario("Deploying any version, no errors") {

            lateinit var stages: Array<TestStage>

            Given("Deployment stages in particular order") {
                stages = arrayOf(
                    TestStage(),
                    TestStage()
                )
            }

            When("Deployment stages are executed") {
                Deployment(*stages).deploy()
            }

            Then("Stages should be executed in the given order") {
                assertTrue {
                    stages[0].hasExecutedBefore(stages[1])
                }
            }
        }

        Scenario("Deploying any version with error") {
            lateinit var stages: Array<TestStage>

            Given("Deployment stages in particular order with error in one of the stages") {
                stages = arrayOf(
                    TestStage(),
                    TestStage(),
                    TestStage(true)
                )
            }

            When("Error happens during execution of one of the stages") {
                try {
                    Deployment(*stages).deploy()
                } catch (e: TestException) {
                }
            }

            Then("All changes made by the stages should be canceled in the order that is opposite to the order of the stages execution") {
                assertTrue {
                    stages[1].hasRevertedBefore(stages[0])
                }
            }

        }
    }

})

class TestStage(): Stage {

    constructor(hasErrors: Boolean): this() {
        this.hasErrors = hasErrors
    }

    private var executionTime: Long = 0
    private var revertionTime: Long = 0
    private var hasErrors: Boolean = false

    override fun execute() {
        if (hasErrors) {
            throw TestException()
        }
        executionTime = System.nanoTime()
    }

    override fun revert() {
        revertionTime = System.nanoTime()
    }

    fun hasExecutedBefore(other: TestStage) = executionTime < other.executionTime

    fun hasRevertedBefore(other: TestStage) = revertionTime < other.revertionTime
}