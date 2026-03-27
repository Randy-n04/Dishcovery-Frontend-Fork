package com.dishcovery.app.data.datasource

import com.dishcovery.app.domain.model.Chat
import com.dishcovery.app.domain.model.Message

object ChatProvider {


    private val avatars = listOf(
        "https://i.pravatar.cc/150?img=4", // 0
        "https://i.pravatar.cc/150?img=maria",   // 1
        "https://i.pravatar.cc/150?img=5",  // 2
        "https://i.pravatar.cc/150?img=andres",  // 3
        "https://i.pravatar.cc/150?img=luis",    // 4
        "https://i.pravatar.cc/150?img=sofia",   // 5
        "https://i.pravatar.cc/150?img=3",       // 6
        "https://i.pravatar.cc/150?img=valeria", // 7
        "https://i.pravatar.cc/150?img=david",   // 8
        "https://i.pravatar.cc/150?img=laura",   // 9
        "https://i.pravatar.cc/150?img=1",       // 10
        "https://i.pravatar.cc/150?img=camila",  // 11
    )

    private val chats = listOf(
        Chat(1, 2, "Javier", "¿Cuándo te gustaría empezar...?", "4:30 PM", 0,
            avatars[0]),
        Chat(2, 2, "Kari", "Looks great!", "4:23 PM", 0,
            avatars[1]),
        Chat(3, 2, "Diana", "Lunch on Monday?", "4:12 PM", 0,
            avatars[11]),
        Chat(4, 2, "Ben", "You sent a photo.", "3:58 PM", 0,
            avatars[10]),
        Chat(5, 2, "Chafcito - AI Assistant", "That's an amazing pizza!", "3:31 PM", 0,
            "https://cdn-icons-png.flaticon.com/512/4712/4712027.png"),
        Chat(6, 2, "Alicia", "See you at 8.", "3:30 PM", 0,
            avatars[2]),

        Chat(7, 3, "Marco", "Perfecto, llevo los ingredientes.", "5:40 PM", 1,
            avatars[5]),
        Chat(8, 3, "Luisa", "Nos vemos el domingo para cocinar.", "2:10 PM", 0,
            avatars[6]),

        Chat(9, 4, "Mateo", "Te comparto la receta de ramen.", "6:05 PM", 2,
            avatars[7]),
        Chat(10, 4, "Paola", "Gracias por el tip de salsas.", "1:35 PM", 0,
            avatars[8]),

        Chat(11, 5, "Rosa", "Esa pasta te quedo genial.", "7:01 PM", 0,
            avatars[9]),
        Chat(12, 5, "Daniel", "Mañana probamos tacos veganos.", "12:48 PM", 1,
            avatars[10]),

        Chat(13, 6, "Emma", "Ya hice la lista del mercado.", "4:09 PM", 0,
            avatars[11]),
        Chat(14, 6, "Nico", "Te mando foto del emplatado.", "10:20 AM", 0,
            avatars[0]),  // reutiliza desde el inicio

        Chat(15, 7, "Andrea", "Listo, arranco con la masa.", "8:15 PM", 0,
            avatars[1]),
        Chat(16, 7, "Leo", "Hoy hacemos postres rapidos.", "11:02 AM", 0,
            avatars[2]),

        Chat(17, 8, "Santiago", "Te ayudo con el marinado.", "3:25 PM", 0,
            avatars[3]),
        Chat(18, 8, "Ines", "Quedo muy bien el arroz.", "9:12 AM", 0,
            avatars[4]),

        Chat(19, 9, "Marta", "Ajusta la sal y queda perfecto.", "5:55 PM", 0,
            avatars[5]),
        Chat(20, 9, "Hugo", "Te veo en la clase online.", "2:44 PM", 0,
            avatars[6]),

        Chat(21, 10, "Julia", "Prueba hornearlo 5 min mas.", "6:33 PM", 0,
            avatars[7]),
        Chat(22, 10, "Tomas", "Me encanto esa combinacion.", "1:19 PM", 0,
            avatars[8]),

        Chat(23, 11, "Sara", "Te paso mi receta de brownies.", "7:45 PM", 1,
            avatars[9]),
        Chat(24, 11, "Bruno", "Despues hacemos la salsa picante.", "4:56 PM", 0,
            avatars[10]),

        Chat(25, 12, "Elena", "Ya tengo todo para la sopa.", "8:02 PM", 0,
            avatars[11]),
        Chat(26, 12, "Raul", "Hoy toca cocina asiatica.", "9:28 AM", 0,
            avatars[0]),

        Chat(27, 13, "Noa", "Que buena pinta ese curry.", "5:17 PM", 0,
            avatars[1]),
        Chat(28, 13, "Ivan", "Luego te envio otra idea.", "12:03 PM", 0,
            avatars[2])
    )

    fun getChats(userId: Long): List<Chat> {
        return chats
            .filter { it.userId == userId }
            .map { chat ->
                val latestMessage = messages.lastOrNull { it.chatId == chat.id }
                if (latestMessage != null) {
                    chat.copy(
                        lastMessage = latestMessage.message,
                        time = latestMessage.time
                    )
                } else {
                    chat
                }
            }
    }


    private val messages = mutableListOf(
        Message(1, 1, 2, "¡Hola! Estoy emocionado/a de aprender cocina mexicana!", "8:30 AM"),
        Message(2, 1, 3, "¡Hola Juan! También estoy ansioso/a por aprender cocina italiana!", "8:31 AM"),
        Message(3, 1, 2, "¿Cuándo te gustaría empezar nuestro intercambio de cocina?", "8:32 AM"),

        Message(4, 2, 12, "La salsa quedo increible, buen trabajo.", "9:10 AM"),
        Message(5, 2, 2, "Gracias, en la noche pruebo otra version.", "9:14 AM"),

        Message(6, 3, 13, "¿Almorzamos y practicamos pasta el lunes?", "10:03 AM"),
        Message(7, 3, 2, "Si, me sirve perfecto despues del trabajo.", "10:06 AM"),

        Message(8, 4, 14, "Te mande una foto del resultado final.", "11:21 AM"),
        Message(9, 4, 2, "Se ve buenisimo, gran emplatado.", "11:23 AM"),

        Message(10, 5, 999, "That pizza looks amazing. Try adding fresh basil.", "12:01 PM"),
        Message(11, 5, 2, "Great idea, I will do it next time.", "12:03 PM"),

        Message(12, 6, 15, "Nos vemos a las 8 para la clase.", "1:45 PM"),
        Message(13, 6, 2, "Listo, llevo mis cuchillos y libreta.", "1:47 PM"),

        Message(14, 7, 21, "¿Usamos horno o sarten para la receta?", "2:11 PM"),
        Message(15, 7, 3, "Mejor horno, queda mas pareja.", "2:14 PM"),
        Message(16, 8, 22, "Nos vemos el domingo para cocinar.", "3:00 PM"),
        Message(17, 8, 3, "Perfecto, llevo los ingredientes.", "3:04 PM"),

        Message(18, 9, 23, "Te comparto la receta de ramen completa.", "9:20 AM"),
        Message(19, 9, 4, "Gracias, la practico hoy mismo.", "9:24 AM"),
        Message(20, 10, 24, "Tu tip de salsas me ayudo mucho.", "10:12 AM"),
        Message(21, 10, 4, "Me alegra, luego te paso otra tecnica.", "10:18 AM"),

        Message(22, 11, 25, "Esa pasta te quedo genial.", "11:01 AM"),
        Message(23, 11, 5, "Gracias, la siguiente la hago con pesto.", "11:06 AM"),
        Message(24, 12, 26, "Mañana probamos tacos veganos.", "11:35 AM"),
        Message(25, 12, 5, "De una, llevo tortillas artesanales.", "11:40 AM"),

        Message(26, 13, 27, "Ya hice la lista del mercado.", "1:08 PM"),
        Message(27, 13, 6, "Excelente, yo compro especias.", "1:12 PM"),
        Message(28, 14, 28, "Te mando foto del emplatado.", "1:50 PM"),
        Message(29, 14, 6, "Buenisimo, se ve profesional.", "1:53 PM"),

        Message(30, 15, 29, "Arranco con la masa y te aviso.", "2:25 PM"),
        Message(31, 15, 7, "Dale, yo preparo el relleno.", "2:29 PM"),
        Message(32, 16, 30, "Hoy hacemos postres rapidos.", "3:16 PM"),
        Message(33, 16, 7, "Me encanta, llevo chocolate extra.", "3:20 PM"),

        Message(34, 17, 31, "Te ayudo con el marinado.", "4:05 PM"),
        Message(35, 17, 8, "Gracias, asi adelantamos tiempo.", "4:09 PM"),
        Message(36, 18, 32, "Quedo muy bien el arroz.", "4:44 PM"),
        Message(37, 18, 8, "Si, la textura salio perfecta.", "4:47 PM"),

        Message(38, 19, 33, "Ajusta la sal y queda perfecto.", "5:02 PM"),
        Message(39, 19, 9, "Listo, ahora sabe mucho mejor.", "5:06 PM"),
        Message(40, 20, 34, "Te veo en la clase online.", "5:31 PM"),
        Message(41, 20, 9, "Conectado, ya tengo todo listo.", "5:34 PM"),

        Message(42, 21, 35, "Prueba hornearlo 5 min mas.", "6:10 PM"),
        Message(43, 21, 10, "Buena idea, asi queda mas crujiente.", "6:14 PM"),
        Message(44, 22, 36, "Me encanto esa combinacion.", "6:41 PM"),
        Message(45, 22, 10, "Gracias, la repetimos el viernes.", "6:45 PM"),

        Message(46, 23, 37, "Te paso mi receta de brownies.", "7:03 PM"),
        Message(47, 23, 11, "Genial, la pruebo este fin de semana.", "7:07 PM"),
        Message(48, 24, 38, "Despues hacemos la salsa picante.", "7:30 PM"),
        Message(49, 24, 11, "Perfecto, llevo chiles frescos.", "7:34 PM"),

        Message(50, 25, 39, "Ya tengo todo para la sopa.", "8:00 PM"),
        Message(51, 25, 12, "Excelente, voy calentando agua.", "8:03 PM"),
        Message(52, 26, 40, "Hoy toca cocina asiatica.", "8:18 PM"),
        Message(53, 26, 12, "Voy con fideos y vegetales.", "8:21 PM"),

        Message(54, 27, 41, "Que buena pinta ese curry.", "9:01 PM"),
        Message(55, 27, 13, "Gracias, le puse leche de coco.", "9:04 PM"),
        Message(56, 28, 42, "Luego te envio otra idea.", "9:20 PM"),
        Message(57, 28, 13, "Buenisimo, quiero probar algo nuevo.", "9:24 PM")
    )

    fun getMessages(chatId: Long): List<Message> {
        return messages.filter { it.chatId == chatId }
    }

  
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