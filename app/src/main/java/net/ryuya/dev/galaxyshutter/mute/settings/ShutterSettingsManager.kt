package net.ryuya.dev.galaxyshutter.mute.settings

import android.content.ContentResolver
import android.content.Context
import android.provider.Settings

/**
 * Galaxy 端末のシャッター音設定を読み書きするマネージャー
 *
 * Galaxy 固有の設定キー:
 * - csc_pref_camera_forced_shuttersound_key
 *   1 = シャッター音あり（ON）
 *   2 = シャッター音なし（OFF）
 */
class ShutterSettingsManager(private val context: Context) {

    companion object {
        /** Galaxy 固有のシャッター音設定キー */
        private const val SHUTTER_SOUND_KEY = "csc_pref_camera_forced_shuttersound_key"

        /** シャッター音 ON を表す値 */
        const val VALUE_SOUND_ON = 1

        /** シャッター音 OFF を表す値 */
        const val VALUE_SOUND_OFF = 2
    }

    /**
     * 現在のシャッター音の状態を返す
     * キーが存在しない場合は true（音あり）をデフォルトとする
     *
     * @return シャッター音が ON の場合 true
     */
    fun isShutterSoundEnabled(): Boolean {
        val value = Settings.System.getInt(
            context.contentResolver,
            SHUTTER_SOUND_KEY,
            VALUE_SOUND_ON  // キーが存在しない場合のデフォルト値
        )
        return value == VALUE_SOUND_ON
    }

    /**
     * シャッター音の状態を設定する
     * WRITE_SECURE_SETTINGS 権限が付与されていることが前提
     *
     * @param enabled true の場合は音あり（1）、false の場合は音なし（2）を設定
     * @return 書き込みに成功した場合 true
     */
    fun setShutterSoundEnabled(enabled: Boolean): Boolean {
        return try {
            Settings.System.putInt(
                context.contentResolver,
                SHUTTER_SOUND_KEY,
                if (enabled) VALUE_SOUND_ON else VALUE_SOUND_OFF
            )
            true
        } catch (e: SecurityException) {
            // WRITE_SECURE_SETTINGS 権限がない場合に発生する
            false
        }
    }
}
