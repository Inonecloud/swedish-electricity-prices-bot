package com.yelmanov

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ElectricityPricesApplication


fun main(args: Array<String>) {
    runApplication<ElectricityPricesApplication>(*args)

}
//    bot.buildBehaviourWithLongPolling {
//        println(getMe())
//
//        val nameReplyMarkup = ReplyKeyboardMarkup(
//            matrix {
//                row {
//                    +SimpleKeyboardButton("nope")
//                }
//            }
//        )
//
//        val chooseRegionInlineKeyboard = inlineKeyboard {
//            row {
//                dataButton("S1", "s1")
//                dataButton("S2", "s2")
//            }
//            row {
//                dataButton("S3", "s3")
//                dataButton("S4", "s4")
//            }
//        }
//
//
////        onCommand("start") {
////
////            val photo = waitPhoto(
////                SendTextMessage(it.chat.id, "Send me your photo please, ${it.location}")
////            ).first()
////
////            val name = waitText(
////                SendTextMessage(
////                    it.chat.id,
////                    "Send me your name or choose \"nope\"",
////                    replyMarkup = nameReplyMarkup
////                ),
////            ).first().text.takeIf {it != "nope"}
////
////            sendPhoto(
////                it.chat.id,
////                photo.mediaCollection,
////                entities = buildEntities {
////                    if(name != null) regular(name)
////                }
////            )
////        }
//
//        onCommand("start") {
//            val user = it.chat as PrivateChatImpl
//
//            sendMessage(
//                it.chat.id,
//                "Hello ${user.firstName}, I am electricity prices bot. I will tell you about electricity prices in your region and I will send you " +
//                        "messages with price everyday. First of all you should choose your region"
//            )
//        }
//
//        onCommand("region") {
//            sendMessage(
//                it.chat.id,
//                "Choose your region",
//                replyMarkup = chooseRegionInlineKeyboard
//            )
//        }
//
//        onCommand("test") {
//            s.getTodayPricesFromElbruk("s3-stockholm")
//            sendMessage(
//                it.chat.id,
//                "Choose your region",
//                replyMarkup = chooseRegionInlineKeyboard
//            )
//        }
//
//        onMessageDataCallbackQuery {
//            println(it.data)
//            sendMessage(
//                it.message.chat.id,
//                "Your choice is ${it.data}"
//            )
//
//
//
//            if (it.data == "s1") {
//                TimeUnit.SECONDS.sleep(5)
//                sendMessage(it.message.chat, "I slept 5 second and sent you a message")
//            }
//            it.let {
//                answer(it, "You choose ${it.data}")
//                return@onMessageDataCallbackQuery
//            }
//
//        }
//
//
//
//        onUnhandledCommand {
//            sendMessage(
//                it.chat.id,
//                replyMarkup = replyKeyboard(resizeKeyboard = true, oneTimeKeyboard = true) {
//                    row {
//                        simpleButton("Change Region")
//                        simpleButton("Get Actual Price")
//                    }
//                    row {
//                        simpleButton("Hej")
//                    }
//                }
//            ) {
//                +"Use " + botCommand("region") + " to choose your region"
//            }
//        }
//
////        onText { message: CommonMessage<TextContent> ->
////            assert(message.text == "Change Region")
////            reply(
////                to = message,
////                text = "/region",
////                //replyMarkup = ReplyKeyboardRemove()
////            )
////        }
//
//
////        setMyCommands(
////            BotCommand("region", "Change user's region"),
////            BotCommand("actual_price", "Sends current price of electricity")
////        )
//
//
//        allUpdatesFlow.subscribeSafelyWithoutExceptions(this) {
//            println(it)
//        }
//
//
//    }.join()


