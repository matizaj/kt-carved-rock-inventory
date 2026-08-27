package com.giyhub.matizaj

fun getCategories(list: List<InventoryItem>): List<String> {
    return list.map{it.category}.sorted().distinct()
}

fun filterItemsByCategory(items: List<InventoryItem>, category: String): List<InventoryItem> =
    items.filter { it.category == category }
