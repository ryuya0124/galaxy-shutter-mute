package net.ryuya.dev.galaxyshutter.mute.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Galaxy ブランドカラーをベースにした配色
private val GalaxyBlue = Color(0xFF1A73E8)
private val GalaxyDarkBlue = Color(0xFF0D47A1)
private val GalaxyLightBlue = Color(0xFFBBDEFB)

/** ダークテーマ用カラースキーム */
private val DarkColorScheme = darkColorScheme(
    primary = GalaxyLightBlue,
    onPrimary = Color(0xFF003064),
    primaryContainer = GalaxyDarkBlue,
    onPrimaryContainer = GalaxyLightBlue,
    secondary = Color(0xFFB0BEC5),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2A2A2A),
)

/** ライトテーマ用カラースキーム */
private val LightColorScheme = lightColorScheme(
    primary = GalaxyBlue,
    onPrimary = Color.White,
    primaryContainer = GalaxyLightBlue,
    onPrimaryContainer = GalaxyDarkBlue,
    secondary = Color(0xFF546E7A),
    background = Color(0xFFF5F8FF),
    surface = Color.White,
    surfaceVariant = Color(0xFFE8EEF8),
)

/**
 * Galaxy Shutter Mute アプリのテーマ
 * Android 12+ では Dynamic Color を使用し、それ以下では Galaxy カラーを使用する
 */
@Composable
fun GalaxyShutterMuteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Android 12+ は Dynamic Color（ウォルペーパーから生成）を優先する
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
