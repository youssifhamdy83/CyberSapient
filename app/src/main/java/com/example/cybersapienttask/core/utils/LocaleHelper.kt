/**
 * Created by [Youssef Hamdy] on 3/7/2025.
 */
package com.example.cybersapienttask.core.utils

import android.content.Context
import android.content.res.Configuration
import kotlinx.coroutines.flow.first
import java.util.*

object LocaleHelper {

    // Function to get the saved language from DataStore
    suspend fun getLanguage(context: Context): String {
        return SettingsDataStore.getLanguage(context).first() // Collecting the flow value
    }

    // Function to set the new language in the app's context
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }
}