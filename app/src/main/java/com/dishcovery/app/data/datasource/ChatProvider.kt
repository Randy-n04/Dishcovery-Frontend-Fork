package com.dishcovery.app.data.datasource

import com.dishcovery.app.domain.model.Chat

object ChatProvider {

    fun getChats(): List<Chat> {
        return listOf(

            Chat(3, 2, "Frank", "Lunch on Monday?", "4:12 PM", 0,avatarUrl = "https://randomuser.me/api/portraits/men/1.jpg"),
            Chat(4, 2, "María", "You sent a photo.", "3:58 PM", 0, avatarUrl = "https://randomuser.me/api/portraits/women/1.jpg"),
            Chat(1, 2, "Javier Rodriguez", "¿Cuándo te gustaría empezar...?", "4:30 PM", 2,
                avatarUrl = "https://randomuser.me/api/portraits/men/11.jpg"),
            Chat(2, 2, "Kari", "Looks great!", "4:23 PM", 1,
                avatarUrl = "https://randomuser.me/api/portraits/women/12.jpg"),
            Chat(3, 2, "Diana", "Lunch on Monday?", "4:12 PM", 0,
                avatarUrl = "https://randomuser.me/api/portraits/women/13.jpg"),
            Chat(4, 2, "Ben", "You sent a photo.", "3:58 PM", 0,
                avatarUrl = "https://randomuser.me/api/portraits/men/14.jpg"),
            Chat(5, 2, "Chafcito - AI Assistant", "That’s an amazing pizza!", "3:31 PM", 0,
                avatarUrl = "https://cdn-icons-png.flaticon.com/512/4712/4712027.png"),
            Chat(6, 2, "Alicia", "See you at 8.", "3:30 PM", 0,
                avatarUrl = "https://randomuser.me/api/portraits/women/15.jpg"),
            Chat(7, 3, "Juan", "Hola 👋", "2:00 PM", 0,
                avatarUrl = "https://randomuser.me/api/portraits/men/2.jpg"),
            Chat(8, 4, "Maria", "Todo bien", "1:50 PM", 1,
                avatarUrl = "https://randomuser.me/api/portraits/women/3.jpg")

        )
    }
}