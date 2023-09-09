package com.yelmanov.telegram.service

import com.yelmanov.domain.Regions
import com.yelmanov.domain.User
import com.yelmanov.service.PriceService
import com.yelmanov.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow


@Service
class BotService(
    val priceService: PriceService,
    val userService: UserService
) : TelegramLongPollingBot() {
    @Value("\${bot.token}")
    private val botToken = ""

    @Value("\${bot.name}")
    private val botName = ""
    override fun getBotToken(): String = botToken

    override fun getBotUsername(): String = botName

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            val message = update.message
            val chatId = message.chatId
            val responseText = if (message.hasText()) {
                val text = message.text

                when {
                    text == "/start" -> {
                        userService.saveUser( User(chatId = chatId, username = message.from.userName))
                        "Hello ${update.message.from.firstName}, I am electricity prices bot\\. " +
                                "I will tell you about electricity prices in your region and I will send you messages with price everyday\\." +
                                "\n First of all you should choose your region"
                    }

                    text == "/get" -> get(chatId)

                    text == "/tomorrow" -> tomorrow(chatId)
                    else -> text
                }
            } else {
                "Jail bro!"
            }
            sendNotification(chatId, responseText)
        }
        if (update.hasCallbackQuery()) {
            val callback = update.callbackQuery
            val chatId = callback.from.id
            var user = userService.getUserByChatId(chatId)
            user.region = Regions.valueOf(callback.data)
            userService.saveUser(user)
        }
    }


    private fun get(chatId: Long): String {
        val user = userService.getUserByChatId(chatId)
        priceService.getTodayPricesFromElbruk(user)
        return "You asked for prices"
    }

    private fun tomorrow(chatId: Long): String {
        val user = userService.getUserByChatId(chatId)
        priceService.getTomorrowPricesFromElbruck(user)
        return "Tomorrow"
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

    private fun sendNotification(chatId: Long, responseText: String) {
        val responseMessage = SendMessage(chatId.toString(), responseText)
        responseMessage.enableMarkdownV2(true)
//        responseMessage.replyMarkup = getReplyMarkup(
//            listOf(
//                listOf("Button 1", "Button 2"),
//                listOf("Button 2")
//            )
//        )
        val btn1 = InlineKeyboardButton()
        btn1.text = "SE1"
        btn1.callbackData = Regions.SE1.toString()
        val btn2 = InlineKeyboardButton()
        btn2.text = "SE2"
        btn2.callbackData = Regions.SE2.toString()
        val btn3 = InlineKeyboardButton()
        btn3.text = "SE3"
        btn3.callbackData = Regions.SE3.toString()
        val btn4 = InlineKeyboardButton()
        btn4.text = "SE4"
        btn4.callbackData = Regions.SE4.toString()

        responseMessage.replyMarkup = InlineKeyboardMarkup(
            listOf(
                listOf(
                    btn1,
                    btn2
                ),
                listOf(
                    btn3,
                    btn4
                )
            )
        )
        execute(responseMessage)
    }

}