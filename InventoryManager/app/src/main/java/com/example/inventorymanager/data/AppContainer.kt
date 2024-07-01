package com.example.inventorymanager.data

import android.content.Context
import com.example.inventorymanager.network.InventoryApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {
    val inventoryRepository: InventoryRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val baseUrl = BaseURL().baseURL

    private val retrofit = Retrofit
        .Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: InventoryApiService by lazy {
        retrofit.create(InventoryApiService::class.java)
    }

    override val inventoryRepository: InventoryRepository by lazy {
        NetworkInventoryRepository(retrofitService, context)
    }
}