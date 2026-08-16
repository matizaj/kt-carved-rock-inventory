package com.giyhub.matizaj

import java.util.Locale.getDefault

var overwriteFiles = false
var overwriteFolders = false

fun main(args: Array<String>){
    println("Craved Rock Inventory ")
    showConfiguration()
    var option = showMenu()
    while(option != 0) {
        when (option) {
            1 -> {
                // TODO: implement data in console
            }

            2 -> {
                // TODO: implement raw data export
            }

            3 -> {
                // TODO: implement export data grouped by category
            }

            4 -> {
                // TODO: implement export data for a single category
            }

            5 -> {
                // TODO: implement prepare data for transfer
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