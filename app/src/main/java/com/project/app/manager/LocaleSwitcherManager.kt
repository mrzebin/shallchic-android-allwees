package com.project.app.manager

import android.content.Context
import android.content.res.Configuration
import com.hb.basemodel.config.Constant
import com.hb.basemodel.utils.SPManager
import java.util.*

/**
 * Locale switcher manager.
 *
 */
object LocaleSwitcherManager {
    /**
     * Update locale resources.
     * @param newBase The new base context for this wrapper
     */
    fun configureBaseContext(newBase: Context): Context {
        val language = SPManager.sGetString(Constant.SP_LOCALE_LANGUAGE)
        val country  = SPManager.sGetString(Constant.SP_LOCALE_COUNTRY)
        return updateResources(newBase, Locale(language,country))
    }

    private fun updateResources(newBase: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val res = newBase.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        return newBase.createConfigurationContext(config)
    }
}
