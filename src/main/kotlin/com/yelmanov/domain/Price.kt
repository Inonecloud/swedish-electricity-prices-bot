package com.yelmanov.domain

import java.math.BigDecimal
import java.time.LocalTime

data class Price(
    val from: LocalTime,
    val to: LocalTime,
    val price: BigDecimal,
    val priceIncMoms: BigDecimal,
    val region: Regions
){
    override fun toString(): String {
        return "*from* $from *to* $to: __${price}__ Öre \n".replace(".","\\.")
    }
}


