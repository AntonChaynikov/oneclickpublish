package com.antonchaynikov.oneclickpublish.shellcommand

data class ExecResult(val exitCode: Int, val messages: List<String>) {
    fun isSuccess() = exitCode == 0
}