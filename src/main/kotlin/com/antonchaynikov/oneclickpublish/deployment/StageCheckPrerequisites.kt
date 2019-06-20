package com.antonchaynikov.oneclickpublish.deployment

import com.antonchaynikov.oneclickpublish.shellcommand.ShellCommand
import org.gradle.api.Project
import java.lang.Exception
import java.lang.IllegalStateException

class StageCheckPrerequisites(private val project: Project, private val branchNames: List<String>) : Stage {

    override fun execute() {
        checkAllChangesCommitted()
        checkIsOnCorrectBranch(branchNames)
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

    private fun checkIsOnCorrectBranch(branchNames: List<String>) {
        // get current branch name
        val execResult = ShellCommand(project.rootDir, "git", "rev-parse", "--abbrev-ref", "HEAD").execute()
        if (execResult.isSuccess()) {
            val currentBranchName = execResult.messages[0]

            if (!branchNames.isEmpty() && !branchNames.contains(currentBranchName)) {
                throw IllegalStateException(
                        "Can't deploy from $currentBranchName branch")
            }
        } else {
            throw Exception("Failed to check current branch")
        }
    }
}