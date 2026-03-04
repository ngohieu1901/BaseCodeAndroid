# =============================================================================
# 1. ANDROID & SYSTEM DEFAULTS
# =============================================================================
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-dontnote java.nio.file.Files, java.nio.file.Path
-dontnote **.ILicensingService

# =============================================================================
# 2. THIRD PARTY LIBRARIES (Retrofit, OkHttp, Glide, GMS)
# =============================================================================
# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule

# OkHttp & Retrofit
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-keep class retrofit2.** { *; }
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# Kotlin Coroutines
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Google Ads / GMS
-keep class com.google.android.gms.internal.** { *; }

# Lib ads
-keep class com.amazic.library.** { *; }
-keep class com.hieunt.base.application.** { *; }
-keep class com.hieunt.base.presentations.feature.screen_base.splash.SplashActivity.** { *; }

-keep class com.hieunt.base.domain.model.** { *; }
-keep class com.hieunt.base.data.dto.** { *; }