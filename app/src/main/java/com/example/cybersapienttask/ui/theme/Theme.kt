package com.example.cybersapienttask.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Custom Color Data Class
data class CustomColors(
    val highTask: Color,
    val mediumTask: Color,
    val lowTask: Color,
    val completedProgress: Color,
    val remainingProgress: Color,
    val gray: Color
)


private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkOnPrimary,
    onSecondary = DarkOnSecondary,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    secondary = LightSecondary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = LightOnPrimary,
    onSecondary = LightOnSecondary,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,

)

// Define Custom Color Schemes for Light and Dark
private val DarkCustomColors = CustomColors(
    highTask = HighTaskColorDark,
    mediumTask = MediumTaskColorDark,
    lowTask = LowTaskColorDark,
    completedProgress = CompletedProgressColorDark,
    remainingProgress = RemainingProgressColorDark,
    gray =  DarGray
)

private val LightCustomColors = CustomColors(
    highTask = HighTaskColor,
    mediumTask = MediumTaskColor,
    lowTask = LowTaskColor,
    completedProgress = CompletedProgressColor,
    remainingProgress = RemainingProgressColor,
    gray = LightGrayColor
)

// CompositionLocal for Custom Colors
val LocalCustomColors = staticCompositionLocalOf { LightCustomColors }


@Composable
fun CyberSapientTaskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customColors = if (darkTheme) DarkCustomColors else LightCustomColors

    CompositionLocalProvider(LocalCustomColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object CustomTheme {
    val colors: CustomColors
        @Composable
        get() = LocalCustomColors.current
}