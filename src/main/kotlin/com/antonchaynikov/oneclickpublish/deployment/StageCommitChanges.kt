package com.antonchaynikov.oneclickpublish.deployment

import com.antonchaynikov.oneclickpublish.shellcommand.ShellCommand
import org.gradle.api.Project
import java.lang.Exception

class StageCommitChanges(private val project: Project, private val filePath: String) : Stage {

    override fun execute() {
        val addResult = ShellCommand(project.rootDir, "git", "add", filePath).execute()
        if (!addResult.isSuccess()) {
            throw Exception("Failed to stage version file")
        }

        val commitResult = ShellCommand(project.rootDir, "git", "commit", "-m", "\"Increased app version via script\"").execute()
        if (!commitResult.isSuccess()) {
            throw Exception("Failed to commit version file changes")
        }
    }

    override fun revert() {
        val result = ShellCommand(project.rootDir, "git", "reset", "HEAD~").execute()
        if (!result.isSuccess()) {
            throw Exception("Failed to undo the version change commit")
        }
    }

}