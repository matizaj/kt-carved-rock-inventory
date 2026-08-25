package com.giyhub.matizaj

import java.util.Locale.getDefault

var overwriteFiles = false
var overwriteFolders = false
const val PROCESSING_FOLDER = "processing"

fun main(args: Array<String>){
    println("Craved Rock Inventory ")
    showConfiguration()
    var option = showMenu()
    while(option != 0) {
        when (option) {
            1 -> {
                val result = loadFile("inventory.csv")
                if(result.success) {
                    result.payload.forEach { println(it) }
                }
            }

            2 -> {
                val loadResults = loadFile("inventory.csv")
                if(loadResults.success) {
                    checkForFolder(PROCESSING_FOLDER)
                    val fileResult = writeItems2file(PROCESSING_FOLDER,
                        "raw-data.txt",
                        loadResults.payload,
                        overwriteFiles)
                    if (!fileResult.success) {
                        println("Error: ${fileResult.errorMessage}")
                    }else {
                        println(loadResults.errorMessage)
                    }
                }

            }

            3 -> {
                // TODO: implement export data grouped by category
            }

            4 -> {
                // TODO: implement export data for a single category
            }

            5 -> {
                val targetFolder = "transfer"
                checkForFolder(targetFolder)
                val result = moveFiles(PROCESSING_FOLDER, targetFolder, overwriteFiles)
                if(!result.success) {
                    println(result.errorMessage)
                }
            }

            9 -> showConfiguration()
        }
        option = showMenu()

    }
}

fun showConfiguration(){
    println("Overwrite files? Y/N")
    var input = readlnOrNull()
    if (input!=null) {
        overwriteFiles = input.lowercase(getDefault()).equals("y", true)
    }
    println("Overwrite folders? Y/N")
    input = readlnOrNull()
    if (input!= null) {
        overwriteFolders = input.lowercase(getDefault()).equals("y", true)
    }
}

fun showMenu(): Int {
    println("Select an action:")
    println("1. Display data in console")
    println("2. Export raw data")
    println("3. Export data grouped by category")
    println("4. Export data for a single category")
    println("5. Prepare data for transfer")
    println("9. Change overwrite preferences")
    println("0. Exit")

    val input =readlnOrNull()
    if (input!=null) {
        val option = input.toIntOrNull()
        if (option!=null) {
            return option
        }
    }
    return showMenu()
}

fun checkForFolder(folder: String) {
    val folderCheck = checkIfFolderExists(folder)
    if (folderCheck.success && folderCheck.payload) {
        println("Folder $folder already exists")
    } else {
        val result = createOutputFolder(folder, overwriteFolders)
        if (!result.success) {
            println("Error: ${result.errorMessage}")
        }
    }
}