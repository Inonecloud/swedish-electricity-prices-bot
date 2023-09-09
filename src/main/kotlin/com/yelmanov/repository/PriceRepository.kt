package com.yelmanov.repository

import com.yelmanov.domain.Price
import com.yelmanov.domain.Regions

interface PriceRepository {
    fun save(price: Price)
    fun save(prices: List<Price>)
    fun findAll(): List<Price>
    fun findAllByRegion(region: Regions): List<Price>
}