package com.antonchaynikov.oneclickpublish

enum class VersionType(val fieldName: String) {
    MAJOR(VERSION_FILED_NAME_MAJOR),
    MINOR(VERSION_FILED_NAME_MINOR),
    PATCH(VERSION_FILED_NAME_PATCH),
    BUILD(VERSION_FILED_NAME_BUILD);
}