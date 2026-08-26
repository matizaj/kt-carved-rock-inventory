package com.giyhub.matizaj

import kotlinx.datetime.LocalDate
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.readLines
import kotlin.io.path.writeLines
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

const val ROOT_FOLDER = "data"
const val INPUT_PATH = "$ROOT_FOLDER/input"
const val OUTPUT_PATH = "$ROOT_FOLDER/output"

fun loadFile(filename: String): FileOperation<List<InventoryItem>> {
    val (items, timeTaken) = measureTimedValue {
        val file = Path(INPUT_PATH, filename)
        val lines = file.readLines()
        lines.drop(1).map { line ->
            val (productName, quantity, arrivalDate, manufacturer, category) = line.split(",").map { it.trim() }
            InventoryItem(
                productName,
                quantity.toInt(),
                arrivalDate = LocalDate.parse(arrivalDate),
                manufacturer,
                category
            )
        }
    }

    println("SUCCESS: loaded ${items.size} items in time: $timeTaken")
    return FileOperation(success = true, payload = items, errorMessage = "")
}

fun writeItems2file(folderName: String, filename: String, items: List<InventoryItem>, overwriteFiles: Boolean):FileOperation<Unit> {
    try {
        val timeTaken = measureTimedValue {
            val file = Path("$OUTPUT_PATH/$folderName", filename)
            if (overwriteFiles) {
                file.writeLines(
                    items.map { it.toString() },
                    Charset.defaultCharset(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
                )
            } else {
                file.writeLines(
                    items.map { it.toString() },
                    Charset.defaultCharset(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE
                )
            }
        }
        println("SUCCESS: wrote ${items.size} to the file $filename in time: $timeTaken")
        return FileOperation(true, Unit)
    } catch (e: Exception) {
        println("FAILED: ${e.message}")
        return FileOperation(false, Unit, errorMessage = "${e.message}")
    }
}

fun checkIfFolderExists(folderName: String): FileOperation<Boolean> {
        try {
            val (exist, timeTaken) = measureTimedValue {
                val folderPath = Path("$OUTPUT_PATH/$folderName")
                folderPath.exists()
            }
                println("SUCCESS: Checked for folder $folderName in time: $timeTaken")
                return FileOperation(exist, exist)

        } catch (ex: Exception) {
            return FileOperation(success = false, errorMessage = "FAILED: $ex", payload = false)
        }

}

fun createOutputFolder(folderName: String, overwriteFolder: Boolean): FileOperation<Unit> {
    try {
        val timeTaken = measureTime {
            Files.createDirectories(Paths.get("$OUTPUT_PATH/$folderName"))
        }
        println("SUCCESS: created folder $folderName in time: $timeTaken")
        return FileOperation(true, Unit)
    }catch (e: Exception) {
        if (e is FileAlreadyExistsException && overwriteFolder) {
            println("ERROR: folder $folderName already exists but overwritting folder is enabled")
            return FileOperation(success = true, payload = Unit)
        }
        return FileOperation(success = false, errorMessage = "ERROR: ${e.message}", payload = Unit)
    }
}
fun moveFiles(src: String, target: String, overwriteFile:Boolean):FileOperation<Unit> {
    try {
        val (fileCount, timeTaken) = measureTimedValue {
            val source = Paths.get("$OUTPUT_PATH/$src")
            val target = Paths.get("$OUTPUT_PATH/$target")
            if (!source.exists() || !source.isDirectory()) {
                println("FAILED: $src does not exist or not a directory")
                return FileOperation(success = false, payload = Unit, errorMessage = "FAIL")
            }
            if (!target.exists()) {
                createOutputFolder(target.toString(), overwriteFile)
            }
            var counter = 0
            val files = Files.list(source).use { stream ->
                stream.filter { path ->
                    Files.isRegularFile(path)
                }.toList()
            }

            files.forEach { file ->
                try {
                    val targetFile = target.resolve(file.name)
                    if (overwriteFile) {
                        Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING)
                    } else {
                        Files.move(file, targetFile)
                    }
                } catch (ex: Exception) {
                    return FileOperation(success = false, errorMessage = "ERROR: ${ex.message}", payload = Unit)
                }
                counter++

            }
            counter
        }
        println("SUCCESS: moved $fileCount from $src to $target in time: $timeTaken")
        return FileOperation(true, Unit)
    } catch(ex: Exception) {
        return FileOperation(success = false, payload=Unit, errorMessage = "FAILED: ${ex.message}")
    }
}