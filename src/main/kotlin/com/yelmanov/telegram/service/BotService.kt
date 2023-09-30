package com.yelmanov.telegram.service

import com.yelmanov.domain.Regions
import com.yelmanov.domain.User
import com.yelmanov.service.PriceService
import com.yelmanov.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow


@Service
class BotService(
    val priceService: PriceService,
    val userService: UserService,
    val botMessageService: BotMessageService,
    @Value("\${bot.token}")
    private val token: String
) : TelegramLongPollingBot(token) {


    @Value("\${bot.name}")
    private val botName = ""
    override fun getBotToken(): String = token

    override fun getBotUsername(): String = botName

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            val message = update.message
            val chatId = message.chatId
            if (message.hasText()) {
                val text = message.text

                when {
                    text == "/start" -> {
                        userService.saveUser(User(chatId = chatId, username = message.from.userName))
                        val responseText = "Hello ${update.message.from.firstName}, I am electricity prices bot\\. " +
                                "I will tell you about electricity prices in your region and I will send you messages with price everyday\\." +
                                "\n First of all you should choose your region"
                        botMessageService.sendMessage(responseText, chatId, true)
                    }

                    text == "/today" -> today(chatId)

                    text == "/tomorrow" -> tomorrow(chatId)

                    text == "/region" -> botMessageService.sendMessage("Not implemented", chatId, true)
                    else -> text
                }
            } else {
                "Jail bro!"
            }


        }
        if (update.hasCallbackQuery()) {
            val callback = update.callbackQuery
            val chatId = callback.from.id
            var user = userService.getUserByChatId(chatId)
            user.region = Regions.valueOf(callback.data)
            userService.saveUser(user)
            priceService.getTodayPricesFromElbruk(user)
        }
    }


    private fun today(chatId: Long) {
        val user = userService.getUserByChatId(chatId)
        botMessageService.sendMessage("You asked for prices", chatId, false)
        priceService.getTodayPricesFromElbruk(user)
    }

    private fun tomorrow(chatId: Long) {
        val user = userService.getUserByChatId(chatId)
        priceService.getTomorrowPricesFromElbruk(user)
    }

    private fun getReplyMarkup(allButtons: List<List<String>>): ReplyKeyboardMarkup {
        val markup = ReplyKeyboardMarkup()
        markup.keyboard = allButtons.map { rowButtons ->
            val row = KeyboardRow()
            rowButtons.forEach { rowButton ->
                row.add(rowButton)
            }
            row

        }
        return markup
    }

}