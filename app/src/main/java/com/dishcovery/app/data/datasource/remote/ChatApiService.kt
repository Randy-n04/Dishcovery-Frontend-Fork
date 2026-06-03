package com.dishcovery.app.data.datasource.remote

import com.dishcovery.app.data.datasource.model.ChatDto
import com.dishcovery.app.data.datasource.model.ChatInputDto
import com.dishcovery.app.data.datasource.model.MessageCreateRequest
import com.dishcovery.app.data.datasource.model.MessageDto
import com.dishcovery.app.data.datasource.model.MessageUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface ChatApiService {

    @GET("v1/chats")
    suspend fun findAll(): List<ChatDto>

    @GET("v1/chats/{id}")
    suspend fun findById(@Path("id") id: Long): ChatDto

    @POST("v1/chats")
    suspend fun create(@Body chatInput: ChatInputDto): ChatDto

    @PATCH("v1/chats")
    suspend fun update(@Body chatInput: ChatInputDto): ChatDto

    @DELETE("v1/chats/{id}")
    suspend fun deleteById(@Path("id") id: Long): Response<Unit>

    // Nota: Mantengo estos si existen en otro controlador del backend
    @GET("v1/messages")
    suspend fun getMessagesByChat(@Query("chatId") chatId: Long): List<MessageDto>

    @POST("v1/messages")
    suspend fun createMessage(@Body request: MessageCreateRequest): MessageDto
}
