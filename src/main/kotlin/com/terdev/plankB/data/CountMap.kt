package com.terdev.plankB.data

import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
class CountMap {

    data class Action(
        val start: LocalDateTime,
        var stop: LocalDateTime?
    )

    val countMap: HashMap<Long, Action> = HashMap()

    fun action(userId: Long, day: Int, month: Int, year: Int, date: LocalDateTime, type: String) {
        if (type == "in") {
            if (countMap[userId] == null) {
                countMap[userId] = Action(date, null)
            } else {
                //TODO всплывающее
            }
        }
        if (type == "out") {
            if (countMap[userId] != null) {
                val action = countMap[userId]
                action?.stop = date
            } else {
                //TODO всплывающее
            }
        }
    }

    fun getActionByDay(userId: Long, day: Int, month: Int, year: Int): Long {
        val result = 0L
        if (countMap[userId] != null) {
            val action = countMap[userId]
            return completeTime(action!!)
        }

        return result
    }

    fun completeTime(action: Action): Long {
        if (action.stop == null) {
            return 0
        }
        return Duration.between(action.start, action.stop).toSeconds()
    }
}