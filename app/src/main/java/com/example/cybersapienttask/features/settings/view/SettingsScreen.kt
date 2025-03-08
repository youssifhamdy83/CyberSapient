/**
 * Created by [Youssef Hamdy] on 3/8/2025.
 */
package com.example.cybersapienttask.features.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cybersapienttask.R
import com.example.cybersapienttask.core.utils.LocaleHelper
import com.example.cybersapienttask.core.utils.SettingsDataStore
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Remember states for UI
    var isDarkMode by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("en") }

    // Load stored preferences
    LaunchedEffect(Unit) {
        SettingsDataStore.getTheme(context).collect { isDarkMode = it }
        SettingsDataStore.getLanguage(context).collect { selectedLanguage = it }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        TopAppBar(
            title = {
                Text(
                    stringResource(R.string.settings),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent, // Transparent background
                titleContentColor = MaterialTheme.colorScheme.onBackground, // Text color
                navigationIconContentColor = MaterialTheme.colorScheme.onBackground // Icon color
            ),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
        )

        Spacer(modifier = Modifier.height(4.dp))
        // 🌙 Theme Switch
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.dark_mode), modifier = Modifier.weight(1f))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isDark ->
                        isDarkMode = isDark
                        scope.launch { SettingsDataStore.setTheme(context, isDark) }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🌍 Language Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.selected_lan))
                Spacer(modifier = Modifier.height(8.dp))
                LanguageDropdown(selectedLanguage) { newLanguage ->
                    selectedLanguage = newLanguage
                    scope.launch {
                        SettingsDataStore.setLanguage(context, newLanguage)
                        LocaleHelper.setLocale(context, newLanguage)
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageDropdown(selectedLanguage: String, onLanguageChange: (String) -> Unit) {
    val languages =
        listOf(
            "en" to stringResource(R.string.english),
            "es" to stringResource(R.string.spanish),
            "fr" to stringResource(R.string.french),
            "de" to stringResource(R.string.deutsch),
        )
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(languages.find { it.first == selectedLanguage }?.second ?: "English")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            languages.forEach { (code, name) ->
                DropdownMenuItem(text = { Text(name) }, onClick = {
                    onLanguageChange(code)
                    expanded = false
                })
            }
        }
    }
}

