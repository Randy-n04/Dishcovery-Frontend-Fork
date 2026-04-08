package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.data.datasource.model.MessageCreateRequest
import com.dishcovery.app.data.datasource.model.MessageDto
import com.dishcovery.app.data.datasource.model.MessageUpdateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApiService {

    @GET("chats")
    suspend fun getChats(): List<ChatDto>

    @GET("messages")
    suspend fun getMessagesByChat(@Query("chatId") chatId: Long): List<MessageDto>

    @POST("messages")
    suspend fun createMessage(@Body request: MessageCreateRequest): MessageDto

    @PUT("messages/{id}")
    suspend fun updateMessage(@Path("id") id: Long, @Body request: MessageUpdateRequest): MessageDto
}