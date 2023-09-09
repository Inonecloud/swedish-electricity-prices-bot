package com.yelmanov.service.job

import com.yelmanov.service.PriceService
import com.yelmanov.service.UserService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SendPricesJob(
    private val priceService: PriceService,
    private val userService: UserService
) {

    @Scheduled(cron = "0 0 8 * * ?")
    fun getPricesForTodayBySchedule() {
        userService.getAllUsers().forEach {
            priceService.getTodayPricesFromElbruk(it)
        }
    }

    @Scheduled(cron = "0 0 14 * * ?")
    fun getPricesForTomorrowBySchedule() {
        userService.getAllUsers().forEach {
            priceService.getTomorrowPricesFromElbruck(it)
        }
    }
}