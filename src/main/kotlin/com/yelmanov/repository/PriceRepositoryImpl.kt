package com.yelmanov.repository

import com.yelmanov.domain.Price
import com.yelmanov.domain.Regions
import org.springframework.stereotype.Component

@Component
class PriceRepositoryImpl() : PriceRepository {
    val prices: MutableList<Price> = mutableListOf()
    override fun save(price: Price) {
        prices.add(price)
    }

    override fun save(prices: List<Price>) {
        this.prices.addAll(prices)
    }

    override fun findAll(): List<Price> {
        return prices.toList()
    }

    override fun findAllByRegion(region: Regions): List<Price> {
        return prices.filter { region == it.region }.toList()
    }
}