package com.yelmanov.telegram.service


import com.yelmanov.domain.Regions
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Service
class BotMessageService(
    @Value("\${bot.token}")
    private val token: String
) : TelegramLongPollingBot(token) {

    fun sendMessage(msg: String, chatId: Long, keyboard: Boolean) {

//        val restTemplate = RestTemplate()
//        val forObject = restTemplate.getForObject(
//            "https://api.telegram.org/bot$token/sendMessage?chat_id=$chatId&text=$message&parse_mode=MarkdownV2",
//            String.javaClass
//        )
//        println(forObject)

        val message = SendMessage(chatId.toString(), msg)
        if (keyboard) {
            withKeyboard(message)
        }

        message.enableMarkdownV2(true)
        execute(message)

    }

    private fun withKeyboard(message: SendMessage) {
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

        message.replyMarkup = InlineKeyboardMarkup(
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
    }


    override fun getBotUsername(): String {
        TODO("Not yet implemented")
    }

    override fun onUpdateReceived(update: Update?) {
        TODO("Not yet implemented")
    }


}