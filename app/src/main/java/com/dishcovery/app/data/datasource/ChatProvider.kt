package com.dishcovery.app.data.datasource

import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.model.Message

object ChatProvider {

    // 🔹 LISTA DE CHATS (lo que ya tienes)
    private val chats = listOf(

        Chat(1, 2, "Javier Rodriguez", "¿Cuándo te gustaría empezar...?", "4:30 PM", 0,
            "https://randomuser.me/api/portraits/men/11.jpg"),

        Chat(2, 2, "Kari", "Looks great!", "4:23 PM", 0,
            "https://randomuser.me/api/portraits/women/12.jpg"),

        Chat(3, 2, "Diana", "Lunch on Monday?", "4:12 PM", 0,
            "https://randomuser.me/api/portraits/women/13.jpg"),

        Chat(4, 2, "Ben", "You sent a photo.", "3:58 PM", 0,
            "https://randomuser.me/api/portraits/men/14.jpg"),

        Chat(5, 2, "Chafcito - AI Assistant", "That’s an amazing pizza!", "3:31 PM", 0,
            "https://cdn-icons-png.flaticon.com/512/4712/4712027.png"),

        Chat(6, 2, "Alicia", "See you at 8.", "3:30 PM", 0,
            "https://randomuser.me/api/portraits/women/15.jpg")
    )

    fun getChats(userId: Long): List<Chat> {
        return chats.filter { it.userId == userId }
    }

    // 🔥 NUEVO: MENSAJES
    private val messages = mutableListOf(

        Message(1, 1, 2, "¡Hola! Estoy emocionado/a de aprender cocina mexicana!", "8:30 AM"),
        Message(2, 1, 3, "¡Hola Javier! También estoy ansioso/a por aprender cocina italiana!", "8:31 AM"),
        Message(3, 1, 2, "¿Cuándo te gustaría empezar nuestro intercambio de cocina?", "8:32 AM")
    )

    fun getMessages(chatId: Long): List<Message> {
        return messages.filter { it.chatId == chatId }
    }

    // 🔥 NUEVO: enviar mensaje (simulado)
    fun sendMessage(chatId: Long, senderId: Long, text: String) {
        val newMessage = Message(
            id = messages.size + 1L,
            chatId = chatId,
            senderId = senderId,
            message = text,
            time = "Ahora"
        )
        messages.add(newMessage)
    }
}