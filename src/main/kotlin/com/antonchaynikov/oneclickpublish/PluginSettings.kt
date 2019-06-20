package com.antonchaynikov.oneclickpublish

import java.util.*

open class PluginSettings {
    var branchNames: List<String> = Collections.emptyList()
    var remoteRepoName: String = REMOTE_NAME
}