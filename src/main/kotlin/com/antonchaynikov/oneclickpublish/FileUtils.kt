package com.antonchaynikov.oneclickpublish

import java.io.File

fun getVersionsFile(projectDir: File, path: String): File =
        if (path.startsWith("/")) File(path) else File(projectDir, path)