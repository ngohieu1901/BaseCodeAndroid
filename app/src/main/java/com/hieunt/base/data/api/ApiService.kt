package com.hieunt.base.data.api

import com.hieunt.base.data.database.entities.AppModel
import retrofit2.http.GET

interface ApiService {
    @GET("all_data")
    suspend fun getAllData(): List<AppModel>
}