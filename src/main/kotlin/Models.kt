package com.giyhub.matizaj


import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

data class InventoryItem(val productName: String,
                  val quantity: Int,
                  val arrivalDate: LocalDate,
                  val manufacturer: String,
                  val category: String,) {
    private val dateFormat = LocalDate.Format {
        monthNumber()
        char('/')
        day()
        char('/')
        year()
    }
}

data class FileOperation<T>(val success:Boolean, val payload: T, val errorMessage: String = "")