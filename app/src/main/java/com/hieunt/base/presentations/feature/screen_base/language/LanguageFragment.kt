package com.hieunt.base.presentations.feature.screen_base.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.hieunt.base.R
import com.hieunt.base.base.BaseFragment
import com.hieunt.base.databinding.FragmentLanguageBinding
import com.hieunt.base.domain.model.LanguageParentModel
import com.hieunt.base.domain.model.LanguageSubModel
import com.hieunt.base.presentations.feature.screen_base.language_start_new.LanguageStartNewAdapter
import com.hieunt.base.utils.SystemUtils
import com.hieunt.base.widget.tap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {
    private var adapter: LanguageStartNewAdapter? = null
    private var model = LanguageSubModel()

    override fun initData() {

    }

    override fun setupView() {
        adapter = LanguageStartNewAdapter(
            setLanguageDefault(),
            onClickSubLanguage = {
                model = it
                selectLanguage()
            },
            onClickDropDown = {}
        )

        binding.recyclerView.adapter = adapter
        binding.ivBack.tap {
            popBackStack()
        }
    }

    override fun dataCollect() {

    }

    private fun selectLanguage() {
        SystemUtils.saveLocale(requireContext(), model.isoLanguage)
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(model.isoLanguage)
        AppCompatDelegate.setApplicationLocales(appLocale)
        popBackStack()
    }

    private fun setLanguageDefault(): List<LanguageParentModel> {
        val lists: MutableList<LanguageParentModel> = ArrayList()
        lists.add(
            LanguageParentModel(
                "English", "en", false, R.drawable.ic_english_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_el_uk,"English (UK)", "en", false),
                    LanguageSubModel(R.drawable.flag_el_us,"English (US)", "en", false),
                    LanguageSubModel(R.drawable.flag_el_india,"English (India)", "en", false),
                    LanguageSubModel(R.drawable.flag_el_international,"English (International)", "en", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Hindi", "hi", false, R.drawable.ic_hindi_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_hindi_india,"Hindi (Standard – India)", "hi", false),
                    LanguageSubModel(R.drawable.flag_hindi_el,"Hindi (Hinglish)", "hi", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Spanish", "es", false, R.drawable.ic_span_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_spain_spain,"Spanish (Spain)", "es", false),
                    LanguageSubModel(R.drawable.flag_spain_latin,"Spanish (Latin America)", "es", false),
                    LanguageSubModel(R.drawable.flag_spain_mexico,"Spanish (Mexico)", "es", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "French", "fr", false, R.drawable.ic_french_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_fr_fr,"French (France)", "fr", false),
                    LanguageSubModel(R.drawable.flag_fr_canada,"French (Canada)", "fr", false),
                    LanguageSubModel(R.drawable.flag_fr_afica,"French (Africa)", "fr", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "German", "de", false, R.drawable.ic_german_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_de_de,"German (Germany)", "de", false),
                    LanguageSubModel(R.drawable.flag_de_austria,"German (Austria)", "de", false),
                    LanguageSubModel(R.drawable.flag_de_switzer,"German (Switzerland)", "de", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Indonesian", "in", false, R.drawable.ic_indo_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_indo_spain,"Indonesian (Standard)", "in", false),
//                    LanguageSubModel(R.drawable.flag_indo_spain,"Indonesian (Informal, English combined)", "in", false),
                    LanguageSubModel(R.drawable.flag_indo_japan,"Indonesian (Javanese-influenced)", "in", false)
                )
            )
        )
        lists.add(
            LanguageParentModel(
                "Portuguese", "pt", false, R.drawable.ic_portuguese_flag,
                mutableListOf(
                    LanguageSubModel(R.drawable.flag_pt_pt,"Portuguese (Portugal)", "pt", false),
                    LanguageSubModel(R.drawable.flag_pt_brazil,"Portuguese (Brazil)", "pt", false),
                    LanguageSubModel(R.drawable.flag_pt_afica,"Portuguese (Africa)", "pt", false)
                )
            )
        )
        var data: LanguageParentModel? = null
        lists.forEach { languageModel ->
            languageModel.listLanguageSubModel.forEach { languageSubModel ->
                if (languageSubModel.isoLanguage == SystemUtils.getPreLanguage(requireContext())) {
                    this.model = languageSubModel
                    data = languageModel
                    languageSubModel.isCheck = true
                }
            }
        }
        data?.let {
            lists.remove(it)
            lists.add(0, it)
        }
        return lists
    }
}