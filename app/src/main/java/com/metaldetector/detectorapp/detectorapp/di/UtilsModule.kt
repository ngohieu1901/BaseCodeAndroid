package com.metaldetector.detectorapp.detectorapp.di

import android.content.Context
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideSharePrefUtils(@ApplicationContext context: Context): SharePrefUtils {
        return SharePrefUtils(context)
    }

}