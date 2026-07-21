package com.hieunt.base.data.remote

import com.hieunt.base.data.api.ApiService
import com.hieunt.base.data.database.entities.AppModel
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: Lazy<ApiService>) {
    suspend fun getAllData(): List<AppModel> =  apiService.get().getAllData()
}