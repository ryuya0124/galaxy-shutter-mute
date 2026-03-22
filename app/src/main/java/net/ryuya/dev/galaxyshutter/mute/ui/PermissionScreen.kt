package net.ryuya.dev.galaxyshutter.mute.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * 権限が不足している場合に表示する案内画面
 * 権限状態に応じてメッセージとアクションボタンを表示する
 */
@Composable
fun PermissionScreen(
    permissionState: PermissionState,
    onRequestShizuku: () -> Unit,
    onRetry: () -> Unit
) {
    val (title, message, buttonLabel, buttonAction) = when (permissionState) {
        is PermissionState.ShizukuNotRunning -> PermissionContent(
            title = "Shizuku が起動していません",
            message = "この機能を使用するには Shizuku アプリを起動する必要があります。\n\nShizuku を起動してから「再試行」をタップしてください。",
            buttonLabel = "再試行",
            action = onRetry
        )
        is PermissionState.ShizukuPermissionRequired -> PermissionContent(
            title = "Shizuku 権限が必要です",
            message = "Galaxy Shutter Mute がシャッター音を制御するには、Shizuku アプリからの権限付与が必要です。",
            buttonLabel = "Shizuku 権限を付与",
            action = onRequestShizuku
        )
        is PermissionState.ShizukuDenied -> PermissionContent(
            title = "Shizuku 権限が拒否されました",
            message = "Shizuku アプリから権限の付与を許可してください。\n\n「許可しない」を選択した場合は、Shizuku アプリから手動で許可を設定できます。",
            buttonLabel = "再試行",
            action = onRequestShizuku
        )
        is PermissionState.WriteSecureSettingsDenied -> PermissionContent(
            title = "システム権限の付与に失敗しました",
            message = "WRITE_SECURE_SETTINGS 権限の付与に失敗しました。\n\n以下のコマンドを ADB で手動実行してみてください:\n\nadb shell pm grant net.ryuya.dev.galaxyshutter.mute android.permission.WRITE_SECURE_SETTINGS",
            buttonLabel = "再試行",
            action = onRetry
        )
        else -> PermissionContent(
            title = "権限を確認中...",
            message = "",
            buttonLabel = "",
            action = {}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 警告アイコン
        Icon(
            imageVector = Icons.Rounded.Warning,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // メッセージカード（コードブロック風）
        if (message.isNotEmpty()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (buttonLabel.isNotEmpty()) {
            Button(
                onClick = buttonAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = buttonLabel,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/** 権限画面のコンテンツをまとめるデータクラス */
private data class PermissionContent(
    val title: String,
    val message: String,
    val buttonLabel: String,
    val action: () -> Unit
)
