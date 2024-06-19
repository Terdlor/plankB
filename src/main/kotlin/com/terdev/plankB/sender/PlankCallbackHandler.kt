package com.terdev.plankB.sender

import com.terdev.plankB.common.CallbackHandler
import com.terdev.plankB.common.HandlerName
import com.terdev.plankB.data.AnswerTarget
import com.terdev.plankB.data.CountMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.bots.AbsSender
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class PlankCallbackHandler: CallbackHandler {

    override val name: HandlerName = HandlerName.PLANK

    @Autowired
    lateinit var countMap: CountMap

    override fun processCallbackData(
        absSender: AbsSender,
        callbackQuery: CallbackQuery,
        arguments: List<String>
    ) {
        val localDate = Instant.ofEpochMilli(callbackQuery.message.date * 1000L)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val chatId = callbackQuery.message.chatId.toString()

        if (LocalDateTime.now().toLocalDate().isEqual(localDate)) {
            countMap.action(
                callbackQuery.from.id,
                localDate.dayOfMonth,
                localDate.monthValue,
                localDate.year,
                LocalDateTime.now(),
                arguments.first()
            )
        } else {
            absSender.execute(
                AnswerCallbackQuery(callbackQuery.id, "Это не сегодня", true, null, 0)
            )
            return
        }

        val actions = countMap.getActionByDay(
            callbackQuery.from.id,
            localDate.dayOfMonth,
            localDate.monthValue,
            localDate.year
        )

        try {
            absSender.execute(
                EditMessageText(
                    chatId,
                    callbackQuery.message.messageId,
                    callbackQuery.inlineMessageId,
                    "За сегодня у тебя $actions",
                    "Markdown",
                    false,
                    callbackQuery.message.replyMarkup,
                    emptyList()
                )
            )
        } finally {
            absSender.execute(AnswerCallbackQuery(callbackQuery.id))
        }
    }
}