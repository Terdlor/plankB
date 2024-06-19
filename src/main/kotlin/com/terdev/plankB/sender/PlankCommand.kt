package com.terdev.plankB.sender

import com.terdev.plankB.common.CommandName
import com.terdev.plankB.common.HandlerName
import com.terdev.plankB.common.createMessageWithInlineButtons
import com.terdev.plankB.data.AnswerTarget
import com.terdev.plankB.data.CountMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import java.time.LocalDate

@Component
class PlankCommand : BotCommand(CommandName.PLANK.text, "") {

    @Autowired
    lateinit var countMap: CountMap

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {

        val localDate = LocalDate.now()
        val actions = countMap.getActionByDay(user.id, localDate.dayOfMonth, localDate.monthValue, localDate.year)

        val callback = HandlerName.PLANK.text
        absSender.execute(
            createMessageWithInlineButtons(
                chat.id.toString(),
                "За сегодня у тебя $actions",
                listOf(
                    listOf(
                        "$callback|${AnswerTarget.IN.text}" to AnswerTarget.IN.desc,
                        "$callback|${AnswerTarget.OUT.text}" to AnswerTarget.OUT.desc
                    ),
                )
            )
        )
    }
}