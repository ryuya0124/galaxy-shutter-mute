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
 * Manager アプリからインストールして権限を付与するように促すデザイン
 */
@Composable
fun PermissionScreen(
    permissionState: PermissionState,
    onRetry: () -> Unit
) {
    val (title, message, buttonLabel, buttonAction) = when (permissionState) {
        is PermissionState.WriteSecureSettingsDenied -> PermissionContent(
            title = "Managerアプリが必要です",
            message = "WRITE_SECURE_SETTINGS 権限が付与されていません。\n\n「Galaxy Shutter Mute Manager」アプリを使用してこのアプリをインストールすると自動的に権限が付与されます。\n\nADBで手動付与する場合は以下のコマンドを実行してください:\n\nadb shell pm grant net.ryuya.dev.galaxyshutter.mute android.permission.WRITE_SECURE_SETTINGS",
            buttonLabel = "権限を再確認する",
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
