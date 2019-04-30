package com.antonchaynikov.oneclickpublish.deployment

import com.antonchaynikov.oneclickpublish.VersionType

class StageIncrementVersion(private val projectVersion: ProjectVersion, private val versionType: VersionType) : Stage {
    override fun execute() {
        System.out.println("Incrementing version")
        projectVersion.incrementVersionField(versionType.fieldName)
    }

    override fun revert() {
        System.out.println("Reverting version change")
        projectVersion.undoChanges()
    }
}