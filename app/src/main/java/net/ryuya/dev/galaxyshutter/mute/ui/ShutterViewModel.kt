package net.ryuya.dev.galaxyshutter.mute.ui

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.ryuya.dev.galaxyshutter.mute.settings.ShutterSettingsManager

/**
 * シャッター音トグル画面の状態を管理する ViewModel
 * Manager アプリ側で WRITE_SECURE_SETTINGS を付与する設計となったため、ここではシステムネイティブで権限をチェックします。
 */
class ShutterViewModel(private val context: Context) : ViewModel() {

    private val shutterManager = ShutterSettingsManager(context)

    /** 画面全体の UI 状態 */
    private val _uiState = MutableStateFlow(ShutterUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkStatusAndLoad()
    }

    /**
     * WRITE_SECURE_SETTINGS 権限・現在のシャッター設定値を確認してUIに反映する
     */
    fun checkStatusAndLoad() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val permissionState = if (hasWriteSecureSettings()) {
                PermissionState.Granted
            } else {
                PermissionState.WriteSecureSettingsDenied
            }

            val isSoundEnabled = if (permissionState == PermissionState.Granted) {
                shutterManager.isShutterSoundEnabled()
            } else {
                null
            }

            _uiState.update {
                it.copy(
                    permissionState = permissionState,
                    isShutterSoundEnabled = isSoundEnabled,
                    isLoading = false
                )
            }
        }
    }

    /**
     * シャッター音のオン・オフを切り替える
     */
    fun toggleShutterSound() {
        val current = _uiState.value.isShutterSoundEnabled ?: return
        val newValue = !current
        val success = shutterManager.setShutterSoundEnabled(newValue)
        if (success) {
            _uiState.update { it.copy(isShutterSoundEnabled = newValue) }
        }
    }

    /**
     * このアプリに WRITE_SECURE_SETTINGS が付与されているかを確認する
     */
    private fun hasWriteSecureSettings(): Boolean {
        return context.checkSelfPermission(android.Manifest.permission.WRITE_SECURE_SETTINGS) ==
                PackageManager.PERMISSION_GRANTED
    }

    /** ViewModelFactory（Context を渡すために必要） */
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ShutterViewModel(context) as T
        }
    }
}

/**
 * シャッター画面の UI 全体状態を表すデータクラス
 */
data class ShutterUiState(
    val permissionState: PermissionState = PermissionState.Checking,
    val isShutterSoundEnabled: Boolean? = null,
    val isLoading: Boolean = true
)

/**
 * 権限の状態を表す sealed class
 */
sealed class PermissionState {
    /** 確認中 */
    object Checking : PermissionState()

    /** 権限 OK */
    object Granted : PermissionState()

    /** WRITE_SECURE_SETTINGS の付与がない */
    object WriteSecureSettingsDenied : PermissionState()
}
