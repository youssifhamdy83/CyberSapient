package com.example.cybersapienttask

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cybersapienttask.navigation.AppNav
import com.example.cybersapienttask.ui.theme.CyberSapientTaskTheme
import com.example.cybersapienttask.core.utils.LocaleHelper
import com.example.cybersapienttask.core.utils.SettingsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            // Collect theme and language preferences inside the composable scope
            var isDarkMode by remember { mutableStateOf(false) }

            // Collect preferences from DataStore
            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    SettingsDataStore.getTheme(applicationContext).collect { theme ->
                        isDarkMode = theme
                    }
                    SettingsDataStore.getLanguage(applicationContext).collect { lang ->
                        LocaleHelper.setLocale(this@MainActivity, lang)
                    }
                }
            }
            CyberSapientTaskTheme(darkTheme = isDarkMode) {
                navHostController = rememberNavController()
                AppNav(
                    modifier = Modifier,
                    navController = navHostController,
                )
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val savedLanguage = SettingsDataStore.getLanguage(newBase) // Get saved language from DataStore
        val localizedContext = LocaleHelper.setLocale(newBase, runBlocking { savedLanguage.first() })
        super.attachBaseContext(localizedContext)
    }

}

