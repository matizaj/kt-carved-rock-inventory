package com.giyhub.matizaj

import kotlinx.datetime.LocalDate
import kotlin.io.path.Path
import kotlin.io.path.readLines

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

    println("SUCCESS: loaded${items.size} items")
    return FileOperation(success = true, payload = items, errorMessage = "")
}