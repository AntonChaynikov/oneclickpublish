package com.antonchaynikov.oneclickpublish

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class OneClickPublishTest: Spek({

    Feature("Tasks creation by the plugin") {

        val project: Project by memoized {
            ProjectBuilder.builder().build()
        }

        Scenario("Plugin application") {

            When("Plugin is applied to the project") {
                @Suppress("UnstableApiUsage")
                project.pluginManager.apply("com.antonchaynikov.oneclickpublish")
            }

            listOf(
                "deployMajorVersion",
                "deployMinorVersion",
                "deployPatchVersion"
            ).forEach {
                Then("$it task in group $TASK_GROUP_NAME should be created") {
                    val task = project.tasks.findByName(it)
                    assertNotNull(task)
                    assertEquals(TASK_GROUP_NAME, task.group)
                }
            }
        }
    }
})