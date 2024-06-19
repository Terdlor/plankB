package com.terdev.plankB

import com.terdev.plankB.common.CallbackHandler
import com.terdev.plankB.common.createMessage
import com.terdev.plankB.common.createMessageWithSimpleButtons
import com.terdev.plankB.common.createTestMessage
import com.terdev.plankB.common.getSimpleKeyboard
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Update

@SpringBootApplication
class PlankBotApplicatoin

fun main(args: Array<String>) {
    runApplication<PlankBotApplicatoin>(*args)
}

@Component
class ManagerTimeBot(
    commands: Set<BotCommand>,
    callbackHandlers: Set<CallbackHandler>,
    env: Environment
) : TelegramLongPollingCommandBot(env.getProperty("telegram.token")) {

    private val botName by lazy { env.getProperty("telegram.botName") ?: "не указано telegram.botName" }

    override fun getBotUsername(): String = botName

    private lateinit var handlerMapping: Map<String, CallbackHandler>

    init {
        registerAll(*commands.toTypedArray())
        handlerMapping = callbackHandlers.associateBy { it.name.text }
    }

    override fun processNonCommandUpdate(update: Update) {
        if (update.hasMessage()) {
            val chatId = update.message.chatId.toString()
            if (update.message.hasText()) {
                execute(createMessage(chatId, "Вы написали: *${update.message.text}*"))
            } else {
                execute(createMessage(chatId, "Я понимаю только текст!"))
            }
        } else if (update.hasCallbackQuery()) {
            val callbackQuery = update.callbackQuery
            val callbackData = callbackQuery.data

            val callbackArguments = callbackData.split("|")
            val callbackHandlerName = callbackArguments.first()

            handlerMapping.getValue(callbackHandlerName)
                .processCallbackData(
                    this,
                    callbackQuery,
                    callbackArguments.subList(1, callbackArguments.size)
                )
        }
    }
}