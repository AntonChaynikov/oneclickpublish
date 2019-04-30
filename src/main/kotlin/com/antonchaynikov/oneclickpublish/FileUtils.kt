package com.antonchaynikov.oneclickpublish

import java.io.File

fun getVersionsFile(projectRootDir: File, path: String): File =
        if (path.startsWith("/")) File(path) else File(projectRootDir, path)