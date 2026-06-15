package com.hieunt.base.data.remote

import com.hieunt.base.data.apis.AppApi
import com.hieunt.base.data.database.entities.AppModel
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val appApi: Lazy<AppApi>) {
    suspend fun getAllData(): List<AppModel> =  appApi.get().getAllData()
}