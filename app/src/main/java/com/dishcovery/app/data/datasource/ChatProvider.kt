package com.dishcovery.app.data.datasource

import com.dishcovery.app.domain.model.Chat

object ChatProvider {

    fun getChats(): List<Chat> {
        return listOf(

            // USER 1
            Chat(1, 1, "Javier Rodriguez", "¿Cuándo te gustaría empezar...?", "4:30 PM", 2),
            Chat(2, 1, "Karl", "Looks great!", "4:23 PM", 1),

            // USER 2
            Chat(3, 2, "Diana", "Lunch on Monday?", "4:12 PM", 0),
            Chat(4, 2, "Ben", "You sent a photo.", "3:58 PM", 0),

            // USER 1
            Chat(5, 1, "Chafcito - AI Assistant", "That's an amazing pizza!", "3:31 PM", 0),

            // USER 3
            Chat(6, 3, "Alicia", "See you at 8.", "3:30 PM", 0)
        )
    }
}