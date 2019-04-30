package com.antonchaynikov.oneclickpublish.deployment

import com.antonchaynikov.oneclickpublish.VersionType
import com.antonchaynikov.oneclickpublish.shellcommand.ShellCommand
import org.gradle.api.Project
import java.io.*
import java.lang.Exception
import java.util.*

class ProjectVersion(private val project: Project, private val versionFile: File) {

    private val versionProperties = Properties()
    private val backupVersionProperties = Properties()

    fun getFormattedVersion(): String {
        return "v${versionProperties.getProperty(VersionType.MAJOR.fieldName)}" +
                ".${versionProperties.getProperty(VersionType.MINOR.fieldName)}" +
                ".${versionProperties.getProperty(VersionType.PATCH.fieldName)}" +
                ".${versionProperties.getProperty(VersionType.BUILD.fieldName)}"
    }

    fun incrementVersionField(fieldName: String) {
        val versionValue = versionProperties.getProperty(fieldName).toInt() + 1
        versionProperties.setProperty(fieldName, versionValue.toString())

        val precedenceMap: Map<String, Int> = getVersionPropertiesPrecedenceMap()
        precedenceMap.entries.forEach {
            if (it.value > precedenceMap.getValue(fieldName)) {
                versionProperties.setProperty(it.key, "0")
            }
        }

        FileOutputStream(versionFile).use {
            versionProperties.store(it, "Increased $fieldName")
        }
    }

    fun init() {
        if (versionFile.exists()) {
            FileInputStream(versionFile).use {
                versionProperties.load(it)
                backupVersionProperties.load(it)
            }
        } else {
            throw FileNotFoundException("The version file ${versionFile.canonicalPath} not found")
        }
    }

    fun undoChanges() {
        val result = ShellCommand(project.rootDir, "git", "checkout", versionFile.canonicalPath).execute()
        if (!result.isSuccess()) {
            throw Exception("Failed to revert changes to the version file, filename: ${versionFile.canonicalPath}")
        }
    }

    private fun getVersionPropertiesPrecedenceMap(): Map<String, Int> = mapOf(
            VersionType.MAJOR.fieldName to 1,
            VersionType.MINOR.fieldName to 2,
            VersionType.PATCH.fieldName to 3,
            VersionType.BUILD.fieldName to 4
    )
}