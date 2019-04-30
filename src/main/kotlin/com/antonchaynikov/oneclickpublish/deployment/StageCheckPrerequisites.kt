package com.antonchaynikov.oneclickpublish.deployment

import com.antonchaynikov.oneclickpublish.shellcommand.ShellCommand
import org.gradle.api.Project
import java.lang.Exception
import java.lang.IllegalStateException

class StageCheckPrerequisites(private val project: Project, private val branchName: String) : Stage {

    override fun execute() {
        checkAllChangesCommitted()
        checkIsOnCorrectBranch(branchName)
    }

    override fun revert() {
        // Nothing to revert

        System.out.println("All changes reverted")
    }

    private fun checkAllChangesCommitted() {
        val execResult = ShellCommand(project.rootDir, "git", "diff-index", "--quiet", "HEAD").execute()
        if (!execResult.isSuccess()) {
            throw IllegalStateException("Commit or discard all changes before deployment")
        }
    }

    private fun checkIsOnCorrectBranch(branchName: String) {
        val execResult = ShellCommand(project.rootDir, "git", "rev-parse", "--abbrev-ref", "HEAD").execute()
        if (execResult.isSuccess()) {
            val currentBranchName = execResult.messages[0]
            if (branchName != currentBranchName) {
                throw IllegalStateException(
                        "Can only deploy from $branchName branch. Current branch: $currentBranchName")
            }
        } else {
            throw Exception("Failed to check current branch")
        }
    }
}