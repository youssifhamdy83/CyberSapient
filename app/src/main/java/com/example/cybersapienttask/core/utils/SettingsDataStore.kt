/**
 * Created by [Youssef Hamdy] on 3/7/2025.
 */
package com.example.cybersapienttask.core.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

object SettingsDataStore {

    private val THEME_KEY = booleanPreferencesKey("theme_key")
    private val LANGUAGE_KEY = stringPreferencesKey("language_key")

    // Get theme from DataStore
    suspend fun setTheme(context: Context, isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkMode
        }
    }

    fun getTheme(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[THEME_KEY] ?: false // default to light theme
            }
    }

    // Set language in DataStore
    suspend fun setLanguage(context: Context, language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    fun getLanguage(context: Context): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                preferences[LANGUAGE_KEY] ?: "en" // default to English
            }
    }
}