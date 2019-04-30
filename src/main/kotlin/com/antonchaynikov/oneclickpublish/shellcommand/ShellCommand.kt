package com.antonchaynikov.oneclickpublish.shellcommand

import java.io.File
import java.util.*

class ShellCommand(private val directory: File, private vararg val command: String) {

    fun execute(): ExecResult {
        val process = ProcessBuilder(command.asList())
                .directory(directory)
                .redirectErrorStream(true)
                .start()

        val reader = process.inputStream.bufferedReader()
        val procMessages = LinkedList<String>()

        do {
            val line = reader.readLine() ?: break
            System.out.println(line)
            procMessages.add(line)
        } while (true)

        val exitCode = process.waitFor()
        return ExecResult(exitCode, procMessages)
    }
}
