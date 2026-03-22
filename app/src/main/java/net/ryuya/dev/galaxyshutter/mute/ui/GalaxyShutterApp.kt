package net.ryuya.dev.galaxyshutter.mute.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import net.ryuya.dev.galaxyshutter.mute.ui.theme.GalaxyShutterMuteTheme

/**
 * アプリのルート Composable
 * MaterialTheme でラップし、権限状態に応じて画面を切り替える
 */
@Composable
fun GalaxyShutterApp() {
    val context = LocalContext.current
    val viewModel: ShutterViewModel = viewModel(factory = ShutterViewModel.Factory(context))
    val uiState by viewModel.uiState.collectAsState()

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
                        onRequestShizuku = viewModel::requestShizukuPermission,
                        onRetry = viewModel::checkStatusAndLoad
                    )
                }
            }
        }
    }
}
