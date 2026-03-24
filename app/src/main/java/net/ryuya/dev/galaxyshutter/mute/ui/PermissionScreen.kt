package net.ryuya.dev.galaxyshutter.mute.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.ryuya.dev.galaxyshutter.mute.R
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material.icons.rounded.ContentCopy

/**
 * 権限が不足している場合に表示する案内画面
 * Manager アプリからインストールして権限を付与するように促すデザイン
 */
@Composable
fun PermissionScreen(
    permissionState: PermissionState,
    onRetry: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val (title, message, buttonLabel, buttonAction) = when (permissionState) {
        is PermissionState.WriteSecureSettingsDenied -> PermissionContent(
            title = stringResource(R.string.permission_required_title),
            message = stringResource(R.string.permission_required_message),
            buttonLabel = stringResource(R.string.permission_retry),
            action = onRetry
        )
        else -> PermissionContent(
            title = stringResource(R.string.permission_checking_title),
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

        if (permissionState is PermissionState.WriteSecureSettingsDenied) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.permission_adb_command_title),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            val adbCommand = stringResource(R.string.permission_adb_command)
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                onClick = {
                    clipboardManager.setText(AnnotatedString(adbCommand))
                    android.widget.Toast.makeText(context, context.getString(R.string.permission_copied), android.widget.Toast.LENGTH_SHORT).show()
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = adbCommand,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Rounded.ContentCopy,
                        contentDescription = "Copy",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
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
