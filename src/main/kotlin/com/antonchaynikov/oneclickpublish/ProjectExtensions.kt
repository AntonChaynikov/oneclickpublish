package com.antonchaynikov.oneclickpublish

import org.gradle.api.Project

fun Project.getPluginSettings(): PluginSettings {
    val settings = this.extensions.findByName(PLUGIN_EXTENSION_NAME)
    if (settings is PluginSettings) {
        return settings
    }
    return PluginSettings()
}
