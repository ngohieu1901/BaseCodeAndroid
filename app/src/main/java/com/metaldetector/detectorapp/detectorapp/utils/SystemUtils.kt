package com.metaldetector.detectorapp.detectorapp.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.metaldetector.detectorapp.detectorapp.firebase.ads.AdsHelper
import com.metaldetector.detectorapp.detectorapp.model.LanguageModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SystemUtils {
    private var myLocale: Locale? = null

    fun isOnlyNumbers(input: String): Boolean {
        val regex = "^[0-9]*\$"
        return input.matches(regex.toRegex())
    }

    // Lưu ngôn ngữ đã cài đặt
    fun saveLocale(context: Context, lang: String?) {
        setPreLanguage(context, lang)
    }

    // Load lại ngôn ngữ đã lưu và thay đổi chúng
    fun setLocale(context: Context?) {
        if (context != null) {
            val language = getPreLanguage(context)
            if (language != "") {
                changeLang(language, context)
            }
        }
    }

    // method phục vụ cho việc thay đổi ngôn ngữ.
    private fun changeLang(lang: String, context: Context) {
        if (lang.equals("", ignoreCase = true)) return
        myLocale = Locale(lang)
        saveLocale(context, lang)
        myLocale?.let { Locale.setDefault(it) }
        val config = Configuration()
        config.locale = myLocale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun getPreLanguage(mContext: Context?): String {
        if (mContext == null) return "en"
        val preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE)
        return preferences.getString("KEY_LANGUAGE", "").toString()
    }

    private fun setPreLanguage(context: Context, language: String?) {
        if (language != null && language != "") {
            val preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("KEY_LANGUAGE", language)
            editor.apply()
        }
    }

    fun listLanguage(): MutableList<LanguageModel> {
        val list: MutableList<LanguageModel> = ArrayList()
        list.add(LanguageModel("Hindi", "hi"))
        list.add(LanguageModel("Spanish", "es"))
        list.add(LanguageModel("French", "fr"))
        list.add(LanguageModel("Portuguese", "pt"))
        list.add(LanguageModel("Indonesian", "in"))
        list.add(LanguageModel("German", "de"))
        list.add(LanguageModel("English", "en"))
        return list
    }

    fun haveNetworkConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        val haveConnectedWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        val haveConnectedMobile = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

        return haveConnectedWifi || haveConnectedMobile
    }

    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
        for (service in runningServices) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun currentDateFormatted(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    fun copyTextToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun shareUrl(context: Context, text: String) {
        if (context is Activity)
            AdsHelper.disableResume(context)
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, "Share text via")
        context.startActivity(shareIntent)
    }
}