package com.yelmanov.service.job

import com.yelmanov.service.PriceService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SendPricesJob(
    private val priceService: PriceService
) {

    @Scheduled(cron = "0 0 8 * * ?")
    fun getPricesForTodayBySchedule(){
        priceService.getTodayPricesFromElbruk()
    }

    @Scheduled(cron = "0 0 14 * * ?")
    fun getPricesForTomorrowBySchedule(){
        priceService.getTomorrowPricesFromElbruck()
    }
}