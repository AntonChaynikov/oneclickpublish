package com.antonchaynikov.oneclickpublish.shellcommand

import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.File
import kotlin.test.assertFalse

internal class ShellCommandTest : Spek({
    Feature("Shell command execution") {
        Scenario("Successful command execution") {
            lateinit var shellCommand: ShellCommand
            lateinit var result: ExecResult

            Given("An 'echo test' command") {
                shellCommand = ShellCommand(File("."),"echo", "test")
            }

            When("The command is executed") {
                result = shellCommand.execute()
            }

            Then("The execution result should be a success") {
                assert(result.isSuccess())
            }

            Then("Result should return 'test' string") {
                assertEquals("test", result.messages[0])
            }
        }

        Scenario("Unsuccessful command execution") {
            lateinit var shellCommand: ShellCommand
            lateinit var result: ExecResult

            Given("An 'false' command") {
                shellCommand = ShellCommand(File("."),"false")
            }

            When("The command is executed") {
                result = shellCommand.execute()
            }

            Then("The execution result should not be a success") {
                assertFalse(result.isSuccess())
            }
        }
    }
})