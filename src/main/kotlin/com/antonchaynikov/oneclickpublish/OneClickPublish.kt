package com.antonchaynikov.oneclickpublish

import com.antonchaynikov.oneclickpublish.deployment.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class OneClickPublish : Plugin<Project> {

    override fun apply(target: Project) {
        target.extensions.create(PLUGIN_EXTENSION_NAME, PluginSettings::class.java)

        configureDeployTask(target, TASK_NAME_DEPLOY_MAJOR, VersionType.MAJOR)
        configureDeployTask(target, TASK_NAME_DEPLOY_MINOR, VersionType.MINOR)
        configureDeployTask(target, TASK_NAME_DEPLOY_PATCH, VersionType.PATCH)
        configureDeployTask(target, TASK_NAME_DEPLOY_BUILD, VersionType.BUILD)
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