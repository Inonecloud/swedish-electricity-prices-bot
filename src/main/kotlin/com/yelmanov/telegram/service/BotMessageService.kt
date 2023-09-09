package com.yelmanov.telegram.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class BotMessageService(
    @Value("\${bot.token}")
    private val token: String
) {

    fun sendMessage(message: String, chatId: Long) {

        val restTemplate = RestTemplate()
        val forObject = restTemplate.getForObject(
            "https://api.telegram.org/bot$token/sendMessage?chat_id=$chatId&text=$message&parse_mode=MarkdownV2",
            String.javaClass
        )
        println(forObject)

    }


}