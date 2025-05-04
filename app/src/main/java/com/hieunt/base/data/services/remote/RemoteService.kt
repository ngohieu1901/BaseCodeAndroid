package com.hieunt.base.data.services.remote

import com.hieunt.base.base.network.BaseRemoteService
import com.hieunt.base.base.network.NetworkResult
import com.hieunt.base.data.apis.AppApi
import com.hieunt.base.data.database.entities.AppModel
import javax.inject.Inject

class RemoteService @Inject constructor(private val appApi: AppApi): BaseRemoteService() {
    suspend fun getAllData(): NetworkResult<List<AppModel>> {
        return callApi { appApi.getAllData() }
    }
}