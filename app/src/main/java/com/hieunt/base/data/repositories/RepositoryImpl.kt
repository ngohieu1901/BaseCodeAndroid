package com.hieunt.base.data.repositories

import com.hieunt.base.data.services.local.LocalService
import com.hieunt.base.data.services.remote.RemoteService
import com.hieunt.base.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val localService: LocalService,
    private val remoteService: RemoteService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): Repository {

}