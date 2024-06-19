package com.terdev.plankB.common

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow


fun createMessage(chatId: String, text: String) =
    SendMessage(chatId, text)
        .apply { enableMarkdown(true) }
        .apply { disableWebPagePreview() }

fun createMessageWithSimpleButtons(chatId: String, text: String, simpleButtons: List<List<String>>) =
    createMessage(chatId, text)
        .apply {
            replyMarkup = getSimpleKeyboard(simpleButtons)
        }

fun getSimpleKeyboard(allButtons: List<List<String>>): ReplyKeyboard =
    ReplyKeyboardMarkup().apply {
        keyboard = allButtons.map { rowButtons ->
            val row = KeyboardRow()
            rowButtons.forEach { rowButton -> row.add(rowButton) }
            row
        }
        oneTimeKeyboard = true
    }

fun createMessageWithInlineButtons(chatId: String, text: String, inlineButtons: List<List<Pair<String, String>>>) =
    createMessage(chatId, text)
        .apply {
            replyMarkup = getInlineKeyboard(inlineButtons)
        }

fun getInlineKeyboard(allButtons: List<List<Pair<String, String>>>): InlineKeyboardMarkup =
    InlineKeyboardMarkup().apply {
        keyboard = allButtons.map { rowButtons ->
            rowButtons.map { (data, buttonText) ->
                InlineKeyboardButton().apply {
                    text = buttonText
                    callbackData = data
                }
            }
        }
    }

fun createTestMessage(chatId: String, text: String) =
    SendMessage(chatId, text)
        //.apply { replyKeyboardMarkup() }
        .apply { replyMarkup = getReplyRemove() }

fun replyKeyboardMarkup(): ReplyKeyboardMarkup {
    val replyKeyboardMarkup = ReplyKeyboardMarkup()
    //replyKeyboardMarkup.selective = true
    //replyKeyboardMarkup.resizeKeyboard = true
    replyKeyboardMarkup.oneTimeKeyboard = true
    replyKeyboardMarkup.keyboard = keyboardRows()
    return replyKeyboardMarkup
}

fun keyboardRows(): List<KeyboardRow> {
    val rows: MutableList<KeyboardRow> = ArrayList()
    rows.add(KeyboardRow(keyboardButtons()))
    return rows
}

fun keyboardButtons(): List<KeyboardButton> {
    val buttons: MutableList<KeyboardButton> = ArrayList()
    buttons.add(KeyboardButton("/start"))
    buttons.add(KeyboardButton("/stop"))
    return buttons
}

fun getReplyRemove(): ReplyKeyboardRemove {
    val markup = ReplyKeyboardRemove()
    markup.removeKeyboard = true
    return markup
}