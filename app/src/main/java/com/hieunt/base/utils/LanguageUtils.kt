package com.hieunt.base.utils

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.hieunt.base.R
import com.hieunt.base.presentations.model.LanguageParentModel
import com.hieunt.base.presentations.model.LanguageSubModel
import java.util.Locale

object LanguageUtils {
    fun saveLocale(context: Context, lang: String?) {
        setPreLanguage(context, lang)
    }

    // Load lại ngôn ngữ đã lưu và thay đổi chúng
    fun setLocale(context: Context) : Context {
        val language = getPreLanguage(context)
        val langToApply = if (language.isBlank()) Locale.getDefault().toString() else language
        return changeLang(langToApply, context)
    }

    // method phục vụ cho việc thay đổi ngôn ngữ.
    fun changeLang(lang: String, context: Context): Context {
        val deviceLanguageParts = when {
            lang.contains("_") -> lang.split("_")
            lang.contains("-") -> lang.split("-")
            else -> listOf(lang)
        }
        val appLanguageCode = if (deviceLanguageParts.size > 1) {
            Locale(deviceLanguageParts[0], deviceLanguageParts[1])
        }else{
            Locale(deviceLanguageParts[0])
        }

        Locale.setDefault(appLanguageCode)

        val config = Configuration(context.resources.configuration)
        config.setLocale(appLanguageCode)

        return context.createConfigurationContext(config)
    }

    fun getPreLanguage(mContext: Context?): String {
        if (mContext == null) return "en"
        val preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE)
        return preferences.getString("KEY_LANGUAGE", "").toString()
    }

    fun setPreLanguage(context: Context, language: String?) {
        if (language != null && language != "") {
            val preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("KEY_LANGUAGE", language)
            editor.apply()
        }
    }

    fun getPreLanguageName(mContext: Context?): String {
        if (mContext == null) return "English (Uk)"
        val preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE)
        Log.d("SystemUtils", "getPreLanguageName: ${preferences.getString("KEY_LANGUAGE_NAME", "").toString()}")
        return preferences.getString("KEY_LANGUAGE_NAME", "").toString()
    }

    fun setPreLanguageName(context: Context, language: String) {
        if (language != "") {
            val preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("KEY_LANGUAGE_NAME", language)
            editor.apply()
            Log.d("SystemUtils", "setPreLanguageName: $language")

        }
    }

    fun getAllLanguages(): List<LanguageParentModel> {
        val lists: MutableList<LanguageParentModel> = ArrayList()
        lists.add(
            LanguageParentModel(
                "Hindi", "hi", false, R.drawable.ic_hindi_flag,
                listOf(
                    LanguageSubModel(
                        R.drawable.flag_hindi_india,
                        "Hindi (Standard – India)",
                        "hi",
                        false
                    ),
                    LanguageSubModel(R.drawable.flag_hindi_el, "Hindi (Hinglish)", "hi", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Spanish", "es", false, R.drawable.ic_span_flag,
                listOf(
                    LanguageSubModel(R.drawable.flag_spain_spain, "Spanish (Spain)", "es", false),
                    LanguageSubModel(
                        R.drawable.flag_spain_latin,
                        "Spanish (Latin America)",
                        "es",
                        false
                    ),
                    LanguageSubModel(R.drawable.flag_spain_mexico, "Spanish (Mexico)", "es", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "French", "fr", false, R.drawable.ic_french_flag,
                listOf(
                    LanguageSubModel(R.drawable.flag_fr_fr, "French (France)", "fr", false),
                    LanguageSubModel(R.drawable.flag_fr_canada, "French (Canada)", "fr", false),
                    LanguageSubModel(R.drawable.flag_fr_afica, "French (Africa)", "fr", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "English", "en", false, R.drawable.ic_english_flag,
                listOf(
                    LanguageSubModel(R.drawable.flag_el_uk, "English (UK)", "en", false),
                    LanguageSubModel(R.drawable.flag_el_us, "English (US)", "en", false),
                    LanguageSubModel(R.drawable.flag_el_india, "English (India)", "en", false),
                    LanguageSubModel(
                        R.drawable.flag_el_international,
                        "English (International)",
                        "en",
                        false
                    )
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "German", "de", false, R.drawable.ic_german_flag,
                listOf(
                    LanguageSubModel(R.drawable.flag_de_de, "German (Germany)", "de", false),
                    LanguageSubModel(R.drawable.flag_de_austria, "German (Austria)", "de", false),
                    LanguageSubModel(
                        R.drawable.flag_de_switzer,
                        "German (Switzerland)",
                        "de",
                        false
                    )
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Indonesian", "in", false, R.drawable.ic_indo_flag,
                listOf(
                    LanguageSubModel(
                        R.drawable.flag_indo_spain,
                        "Indonesian (Standard)",
                        "in",
                        false
                    ),
                    LanguageSubModel(
                        R.drawable.flag_indo_spain,
                        "Indonesian (Informal, English combined)",
                        "in",
                        false
                    ),
                    LanguageSubModel(
                        R.drawable.flag_indo_spain,
                        "Indonesian (Javanese-influenced)",
                        "in",
                        false
                    )
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Portuguese", "pt", false, R.drawable.ic_portuguese_flag,
                listOf(
                    LanguageSubModel(R.drawable.flag_pt_pt, "Portuguese (Portugal)", "pt", false),
                    LanguageSubModel(R.drawable.flag_pt_brazil, "Portuguese (Brazil)", "pt", false),
                    LanguageSubModel(R.drawable.flag_pt_afica, "Portuguese (Africa)", "pt", false)
                )
            )
        )

        lists.add(
            LanguageParentModel(
                "Chinese", "zh", false, R.drawable.ic_china_flag,

                listOf(
                    LanguageSubModel(R.drawable.flag_cn_cn, "Chinese (China)", "zh", false),
                    LanguageSubModel(R.drawable.flag_cn_kh, "Chinese (Hong Kong)", "zh", false),
                    LanguageSubModel(R.drawable.flag_cn_tw, "Chinese (Taiwan)", "zh", false)
                )
            )
        )

        lists.add(
            LanguageParentModel(
                "Swahili", "sw", false, R.drawable.ic_swahili_flag,
                listOf()
            )
        )
        lists.add(
            LanguageParentModel(
                "Korean", "ko", false, R.drawable.ic_korean_flag,
                listOf()
            )
        )
        lists.add(
            LanguageParentModel(
                "Rusian", "ru", false, R.drawable.ic_russia_flag,
                listOf()
            )
        )
        lists.add(
            LanguageParentModel(
                "Turkish", "tr", false, R.drawable.ic_turkey_flag,
                listOf()
            )
        )
        lists.add(
            LanguageParentModel(
                "Arabic", "ar", false, R.drawable.ic_a_rap_flag,
                listOf()
            )
        )
        lists.add(
            LanguageParentModel(
                "Amharic", "am", false, R.drawable.ic_amharic_flag,
                listOf()
            )
        )
        lists.add(
            LanguageParentModel(
                "Zulu", "zu", false, R.drawable.ic_zulu_flag,
                listOf(
                    LanguageSubModel(R.drawable.flag_zulu_zulu, "Zulu (Standard)", "zu", false),
                    LanguageSubModel(R.drawable.flag_zulu_zulu, "Zulu (Urban)", "zu", false),
                    LanguageSubModel(R.drawable.flag_zulu_zulu, "Zulu (Traditional)", "zu", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Yoruba", "yo", false, R.drawable.ic_yoruba_flag,
                listOf()
            )
        )

        lists.add(
            LanguageParentModel(
                "Afrikaans", "af", false, R.drawable.ic_afrikaans_flag,
                listOf(
                    LanguageSubModel(
                        R.drawable.flag_standard,
                        "Afrikaans (South Africa – Standard)",
                        "af",
                        false
                    ),
                    LanguageSubModel(R.drawable.flag_standard, "Afrikaans (Informal)", "af", false),
                    LanguageSubModel(
                        R.drawable.flag_standard_namibia,
                        "Afrikaans (Namibia)",
                        "af",
                        false
                    )
                )
            )
        )

        return lists
    }

}