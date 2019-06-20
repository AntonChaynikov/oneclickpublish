package com.antonchaynikov.oneclickpublish.deployment

import com.antonchaynikov.oneclickpublish.shellcommand.ShellCommand
import org.gradle.api.Project

class StageAddTag(private val project: Project, private val projectVersion: ProjectVersion) : Stage {
    override fun execute() {

        val addTagResult = ShellCommand(
                project.rootDir,
                "git", "tag", "-a", projectVersion.getTagName(), "-m", "Tag added automatically via gradle task")
                .execute()

        if (!addTagResult.isSuccess()) {
            throw Exception("Failed to add tag ${projectVersion.getTagName()}. Does it already exist?")
        }
    }

    override fun revert() {
        System.out.println("Deleting tag ${projectVersion.getTagName()}")

        val result = ShellCommand(project.rootDir, "git", "tag", "-d", projectVersion.getTagName())
                .execute()

        if (!result.isSuccess()) {
            throw Exception("Failed to remove tag")
        }
    }
}