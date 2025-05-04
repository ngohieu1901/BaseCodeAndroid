package com.hieunt.base.di

import android.content.Context
import com.hieunt.base.utils.SharePrefUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharePrefUtils(@ApplicationContext context: Context): SharePrefUtils {
        return SharePrefUtils(context)
    }
}