package com.yelmanov.service.mapper

import com.yelmanov.domain.Price
import com.yelmanov.domain.Regions
import java.math.BigDecimal
import java.time.LocalTime
import java.time.format.DateTimeFormatter


fun convertElementsToPrices(
    elements: List<String>,
    numberOfColumns: Int,
    region: Regions
): List<Price> {
    val prices = mutableListOf<Price>()
    for (i in elements.indices step numberOfColumns) {
        val formatter = DateTimeFormatter.ofPattern("HH")
        val hours = elements[i].replace(" ", "").split("-")

        val price = Price(
            LocalTime.parse(hours[0], formatter),
            LocalTime.parse(hours[1], formatter),
            BigDecimal(elements[i + 1].replace(',', '.')),
            BigDecimal(elements[i + 2].replace(',', '.')),
            region
        )
        prices.add(price)
    }
    return prices.toList()
}

fun Price.elementToPrice(elements: Array<String>, region:Regions): Price {
    val formatter = DateTimeFormatter.ofPattern("HH")
    val hours = elements[0].replace(" ", "").split("-")

    return Price(
        LocalTime.parse(hours[0], formatter),
        LocalTime.parse(hours[1], formatter),
        BigDecimal(elements[1].replace(',', '.')),
        BigDecimal(elements[2].replace(',', '.')),
        region
    )
}