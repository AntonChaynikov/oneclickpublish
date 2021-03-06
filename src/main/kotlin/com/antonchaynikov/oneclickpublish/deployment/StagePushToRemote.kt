package com.antonchaynikov.oneclickpublish.deployment

import com.antonchaynikov.oneclickpublish.shellcommand.ShellCommand
import org.gradle.api.Project

class StagePushToRemote(
        private val project: Project,
        private val projectVersion: ProjectVersion,
        private val remoteName: String) : Stage {

    override fun execute() {
        // pushing tag named as projectVersion.getTagName() to the remote
        val pushCommand = ShellCommand(project.rootDir, "git", "push", remoteName, ":", projectVersion.getTagName()).execute()
        if (!pushCommand.isSuccess()) {
            throw Exception("Failed to push changes to the remote")
        }
    }

    override fun revert() {
        // Nothing to revert
    }
}