package org.kesmarag.megaptera.utils

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.text.Regex

fun pattern2Regex(original: String): Regex {
    val reg: String = original.replace("?",".?").replace("*",".*?")
    val regex: Regex = Regex(reg)
    return regex
}

fun listOfFiles(path: String): List<File> {
    val tmpPath: Path? = Paths.get(path)
    val original = tmpPath?.fileName.toString()
    var dir: String = tmpPath?.parent?.toString() ?: System.getProperty("user.dir").toString()
    val regex = pattern2Regex(original)
    val tmpDir: File = File(dir)
    val list = tmpDir.listFiles()
            .filter { it.name.matches(regex) }
            .filter { it.isFile }
    return list
}