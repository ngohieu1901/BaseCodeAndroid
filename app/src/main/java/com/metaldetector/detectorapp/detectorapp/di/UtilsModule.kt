package com.metaldetector.detectorapp.detectorapp.di

import android.app.Activity
import android.content.Context
import com.metaldetector.detectorapp.detectorapp.utils.PermissionUtils
import com.metaldetector.detectorapp.detectorapp.utils.SharePrefUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideSharePrefUtils(context: Context): SharePrefUtils {
        return SharePrefUtils(context)
    }

    @Provides
    @Singleton
    fun providePermissionUtils(activity: Activity): PermissionUtils {
        return PermissionUtils(activity)
    }
}