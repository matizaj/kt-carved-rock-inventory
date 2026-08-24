package com.giyhub.matizaj

import kotlinx.datetime.LocalDate
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.readLines
import kotlin.io.path.writeLines

const val ROOT_FOLDER = "data"
const val INPUT_PATH = "$ROOT_FOLDER/input"
const val OUTPUT_PATH = "$ROOT_FOLDER/output"

fun loadFile(filename: String): FileOperation<List<InventoryItem>> {
    val file = Path(INPUT_PATH, filename)
    val lines = file.readLines()
    val items = lines.drop(1).map { line ->
        val (productName, quantity, arrivalDate, manufacturer, category) = line.split(",").map { it.trim() }
        InventoryItem(productName, quantity.toInt(), arrivalDate = LocalDate.parse(arrivalDate), manufacturer, category)
    }

    println("SUCCESS: loaded ${items.size} items")
    return FileOperation(success = true, payload = items, errorMessage = "")
}

fun writeItems2file(folderName: String, filename: String, items: List<InventoryItem>):FileOperation<Unit> {
    try {
    val file = Path("$OUTPUT_PATH/$folderName", filename)
        file.writeLines(items.map { it.toString() }, Charset.defaultCharset())
        println("SUCCESS: wrote ${items.size} to the file ${file.name}")
        return FileOperation(true, Unit)
    } catch (e: Exception) {
        println("FAILED: ${e.message}")
        return FileOperation(false, Unit, errorMessage = "${e.message}")
    }
}

fun checkIfFolderExists(folderName: String): FileOperation<Boolean> {
    try {
        val folderPath = Path("$OUTPUT_PATH/$folderName")
        val exist = folderPath.exists()
        return FileOperation(exist, exist)
    } catch (ex: Exception) {
        return FileOperation(success = false, errorMessage = "FAILED: $ex", payload=false)
    }
}

fun createOutputFolder(folderName: String, overwriteFolder: Boolean): FileOperation<Unit> {
    try {
        Files.createDirectories(Paths.get("$OUTPUT_PATH/$folderName"))
        println("SUCCESS: created folder $folderName")
        return FileOperation(true, Unit)
    }catch (e: Exception) {
        if (e is FileAlreadyExistsException && overwriteFolder) {
            println("ERROR: folder $folderName already exists but overwritting folder is enabled")
            return FileOperation(success = true, payload = Unit)
        }
        return FileOperation(success = false, errorMessage = "ERROR: ${e.message}", payload = Unit)
    }
}