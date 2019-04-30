package com.antonchaynikov.oneclickpublish

import com.antonchaynikov.oneclickpublish.deployment.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class OneClickPublish : Plugin<Project> {

    override fun apply(target: Project) {
        target.extensions.create(PLUGIN_EXTENSION_NAME, PluginSettings::class.java)

        mapOf(
            TASK_NAME_DEPLOY_MAJOR to VersionType.MAJOR,
            TASK_NAME_DEPLOY_MINOR to VersionType.MINOR,
            TASK_NAME_DEPLOY_PATCH to VersionType.PATCH,
            TASK_NAME_DEPLOY_BUILD to VersionType.BUILD
        ).forEach {
            configureDeployTask(target, it.key, it.value)
        }
    }

    private fun configureDeployTask(target: Project, taskName: String, versionType: VersionType) {
        val settings = target.getPluginSettings()
        val projectVersion = ProjectVersion(target, getVersionsFile(target.rootDir, settings.versionFilePath))

        target.task(taskName).apply {
            group = TASK_GROUP_NAME
            doLast {
                projectVersion.init()

                Deployment(
                    StageCheckPrerequisites(target, settings.branchName),
                    StageIncrementVersion(projectVersion, versionType),
                    StageCommitChanges(target, settings.versionFilePath),
                    StageAddTag(target, projectVersion),
                    StagePushToRemote(target, projectVersion, settings.remoteRepoName)
                ).deploy()
            }
        }
    }
}