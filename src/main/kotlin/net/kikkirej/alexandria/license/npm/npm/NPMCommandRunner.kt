package net.kikkirej.alexandria.license.npm.npm

import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

@Service
class NPMCommandRunner {

    fun npmInstall(directory: File){
        val process = ProcessBuilder(
            "npm",
            "install",
        ).directory(directory).start()
        process.waitFor()
    }

    fun npmLicenseCheck(directory: File): String {
        val process = ProcessBuilder(
            "npx",
            "-y",
            "license-checker-rseidelsohn",
            "--json"
        ).directory(directory).start()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val builder = StringBuilder()
        var line = ""
        while (reader.readLine().also { line = it } != null) {
            builder.append(line)
            builder.append(System.getProperty("line.separator"))
        }
        return builder.toString()
    }

}