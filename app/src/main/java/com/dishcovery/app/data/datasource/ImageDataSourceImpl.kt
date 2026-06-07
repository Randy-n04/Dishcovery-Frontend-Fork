package com.dishcovery.app.data.datasource

import com.dishcovery.app.data.datasource.remote.ImageApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageDataSourceImpl @Inject constructor(
    private val apiService: ImageApiService
) : ImageDataSource {

    /**
     * El OkHttpClient que usa Retrofit ya tiene el AuthInterceptor configurado
     * en NetworkModule, así que el Bearer Token se añade automáticamente a
     * esta petición multipart sin ningún paso extra aquí.
     */
    override suspend fun analyzeImage(imageFile: File): List<String> {
        //Dejo el log por si acaso lo ocupamos después
        //android.util.Log.d("ImageDS", "Archivo: ${imageFile.absolutePath}, existe: ${imageFile.exists()}, tamaño: ${imageFile.length()} bytes")

        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData(
            name  = "image",
            filename = imageFile.name,
            body  = requestBody
        )

        val response = apiService.analyzeImage(part)

        return if (response.isSuccessful) {
            response.body()?.tags ?: emptyList()
        } else {
            throw IOException("Error del servidor: ${response.code()} ${response.message()}")
        }
    }


}
