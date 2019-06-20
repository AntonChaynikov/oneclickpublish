package com.antonchaynikov.oneclickpublish.deployment

import com.antonchaynikov.oneclickpublish.VERSION_PROPERTY_CODE
import com.antonchaynikov.oneclickpublish.VERSION_PROPERTY_NAME
import com.antonchaynikov.oneclickpublish.VersionType
import com.antonchaynikov.oneclickpublish.getVersionsFile
import com.antonchaynikov.oneclickpublish.shellcommand.ShellCommand
import org.gradle.api.Project
import java.io.*
import java.lang.Exception
import java.util.*

class ProjectVersion(private val project: Project, versionFilePath: String) {

    private val rawVersionProps = Properties()
    private val parsedVersionProperties = Properties()

    private val versionFile: File = getVersionsFile(project.projectDir, versionFilePath)

    fun init() {
        if (versionFile.exists()) {
            FileInputStream(versionFile).use {
                rawVersionProps.load(it)
                parseVersionProperties(rawVersionProps.getProperty(VERSION_PROPERTY_NAME))
            }
        } else {
            throw FileNotFoundException("The version file ${versionFile.canonicalPath} not found")
        }
    }

    fun getTagName(): String {
        return "v${composeFormattedVersion()}"
    }

    fun incrementVersionField(fieldName: String) {
        val versionValue = parsedVersionProperties.getProperty(fieldName).toInt() + 1
        parsedVersionProperties.setProperty(fieldName, versionValue.toString())

        val precedenceMap: Map<String, Int> = getVersionPropertiesPrecedenceMap()
        precedenceMap.entries.forEach {
            if (it.value > precedenceMap.getValue(fieldName)) {
                parsedVersionProperties.setProperty(it.key, "0")
            }
        }

        updateRawVersionProperty()

        FileOutputStream(versionFile).use {
            rawVersionProps.store(it, "Updated project version")
        }
    }

    fun undoChanges() {
        val result = ShellCommand(project.rootDir, "git", "checkout", versionFile.canonicalPath).execute()
        if (!result.isSuccess()) {
            throw Exception("Failed to revert changes to the version file, filename: ${versionFile.canonicalPath}")
        }
    }

    fun getVersionFilePath() = versionFile.absolutePath

    private fun composeFormattedVersion(): String {
        return parsedVersionProperties.getProperty(VersionType.MAJOR.fieldName) +
                ".${parsedVersionProperties.getProperty(VersionType.MINOR.fieldName)}" +
                ".${parsedVersionProperties.getProperty(VersionType.PATCH.fieldName)}"
    }

    private fun getVersionPropertiesPrecedenceMap(): Map<String, Int> = mapOf(
        VersionType.MAJOR.fieldName to 1,
        VersionType.MINOR.fieldName to 2,
        VersionType.PATCH.fieldName to 3
    )

    private fun parseVersionProperties(propertyString: String) {
        if (propertyString.matches(Regex("\\d+\\.\\d+\\.\\d+"))) {
            val propFields = propertyString.split(".")
            setVersionProperty(VersionType.MAJOR.fieldName, propFields[0])
            setVersionProperty(VersionType.MINOR.fieldName, propFields[1])
            setVersionProperty(VersionType.PATCH.fieldName, propFields[2])
        } else {
            throw Exception("The appVersionName property should be in the Vmajor.Vminor.Vpatch format")
        }
    }

    private fun setVersionProperty(versionName: String, value: String) {
        parsedVersionProperties.setProperty(versionName, value.replaceFirst("^0+(?!$)".toRegex(), ""))
    }

    private fun updateRawVersionProperty() {
        rawVersionProps.setProperty(VERSION_PROPERTY_NAME, composeFormattedVersion())
        rawVersionProps.setProperty(VERSION_PROPERTY_CODE, composeVersionCode())
    }

    private fun composeVersionCode(): String {
        val verMajor = Integer.parseInt(parsedVersionProperties.getProperty(VersionType.MAJOR.fieldName))
        val verMinor = Integer.parseInt(parsedVersionProperties.getProperty(VersionType.MINOR.fieldName))
        val verPatch = Integer.parseInt(parsedVersionProperties.getProperty(VersionType.PATCH.fieldName))
        return (verMajor * 1_000_000 + verMinor * 1000 + verPatch).toString()
    }
}