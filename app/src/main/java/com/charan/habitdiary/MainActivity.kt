package com.charan.habitdiary

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.charan.habitdiary.data.model.enums.ThemeOption
import com.charan.habitdiary.data.repository.DataStoreRepository
import com.charan.habitdiary.presentation.navigation.RootNavigation
import com.charan.habitdiary.ui.theme.HabitDiaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var dataStore : DataStoreRepository
    private val keepScreen = mutableStateOf(true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            keepScreen.value
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)

        )

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        setContent {
            val initialTheme = if(isSystemInDarkTheme()) ThemeOption.DARK else ThemeOption.LIGHT
            val themeData = dataStore.getTheme.collectAsStateWithLifecycle(initialValue = initialTheme)
            val dynamicColorsEnabled = dataStore.getDynamicColorsState.collectAsStateWithLifecycle(initialValue = true)
            val onBoardingCompleted = remember {
                mutableStateOf(true)
            }
            LaunchedEffect(Unit) {
                onBoardingCompleted.value = dataStore.getOnBoardingCompleted.first()
            }
            val isDarkMode = when(themeData.value){
                ThemeOption.DARK -> true
                ThemeOption.LIGHT -> false
                ThemeOption.SYSTEM_DEFAULT -> isSystemInDarkTheme()
            }
            controller.isAppearanceLightStatusBars = !isDarkMode
            LaunchedEffect(Unit) {
                keepScreen.value = false
            }
            HabitDiaryTheme(
                darkTheme = isDarkMode,
                dynamicColor = dynamicColorsEnabled.value
            ) {
                Surface {
                    RootNavigation(
                        onBoardingCompleted = onBoardingCompleted.value
                    )
                }

            }
        }
    }
}
