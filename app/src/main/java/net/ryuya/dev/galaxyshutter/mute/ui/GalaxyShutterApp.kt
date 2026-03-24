package net.ryuya.dev.galaxyshutter.mute.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import net.ryuya.dev.galaxyshutter.mute.ui.theme.GalaxyShutterMuteTheme

/**
 * アプリのルート Composable
 * MaterialTheme でラップし、権限状態に応じて画面を切り替える
 * フォアグラウンド復帰時（ON_RESUME）に現在値を即時再取得する
 */
@Composable
fun GalaxyShutterApp() {
    val context = LocalContext.current
    val viewModel: ShutterViewModel = viewModel(factory = ShutterViewModel.Factory(context))
    val uiState by viewModel.uiState.collectAsState()

    // フォアグラウンド復帰時に最新のシャッター設定値を取得する
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.checkStatusAndLoad()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    GalaxyShutterMuteTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            when (uiState.permissionState) {
                // 権限 OK → メインのトグル画面を表示
                is PermissionState.Granted -> {
                    ShutterScreen(
                        uiState = uiState,
                        onToggle = viewModel::toggleShutterSound
                    )
                }
                // 権限確認中 → ローディングを表示（ShutterScreen内でカバー）
                is PermissionState.Checking -> {
                    ShutterScreen(
                        uiState = uiState.copy(isLoading = true, isShutterSoundEnabled = null),
                        onToggle = {}
                    )
                }
                // 権限不足 → 権限案内画面を表示
                else -> {
                    PermissionScreen(
                        permissionState = uiState.permissionState,
                        onRetry = viewModel::checkStatusAndLoad
                    )
                }
            }
        }
    }
}
