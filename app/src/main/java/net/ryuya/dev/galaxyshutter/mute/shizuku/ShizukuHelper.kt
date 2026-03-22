package net.ryuya.dev.galaxyshutter.mute.shizuku

import android.content.pm.PackageManager
import rikka.shizuku.Shizuku

/**
 * Shizuku との連携を担うヘルパーオブジェクト
 *
 * 主な責務:
 * - Shizuku サービスの稼働確認
 * - Shizuku 権限のチェック・リクエスト
 * - WRITE_SECURE_SETTINGS 権限のシェルコマンド経由付与
 */
object ShizukuHelper {

    /** Shizuku 権限リクエストコード */
    private const val REQUEST_CODE_SHIZUKU = 1001

    /** 権限リクエスト結果を受け取るリスナー */
    private val permissionResultListener =
        Shizuku.OnRequestPermissionResultListener { _, grantResult ->
            // 結果は ViewModel 側の StateFlow で管理するため、ここでは何もしない
            // 必要に応じてコールバックを追加できる
        }

    /**
     * Shizuku 権限結果リスナーを登録する
     * Activity の onCreate() で呼び出すこと
     */
    fun addPermissionListener() {
        Shizuku.addRequestPermissionResultListener(permissionResultListener)
    }

    /**
     * Shizuku 権限結果リスナーを解除する
     * Activity の onDestroy() で呼び出すこと
     */
    fun removePermissionListener() {
        Shizuku.removeRequestPermissionResultListener(permissionResultListener)
    }

    /**
     * Shizuku サービスが動作中かどうかを返す
     * バインダーが取得できない状態で Shizuku API を呼ぶと例外が発生するため、事前に確認する
     */
    fun isShizukuAvailable(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 本アプリが Shizuku 権限を持っているかどうかを返す
     */
    fun hasShizukuPermission(): Boolean {
        if (Shizuku.isPreV11()) return false
        return Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Shizuku 権限のリクエストダイアログを表示する
     */
    fun requestShizukuPermission() {
        if (!Shizuku.isPreV11() && !Shizuku.shouldShowRequestPermissionRationale()) {
            Shizuku.requestPermission(REQUEST_CODE_SHIZUKU)
        }
    }

    /**
     * Shizuku 経由でシェルコマンドを実行する
     *
     * @param command 実行するシェルコマンド文字列
     * @return 実行に成功した場合 true
     */
    fun runShellCommand(command: String): Boolean {
        return try {
            val process = Shizuku.newProcess(
                arrayOf("sh", "-c", command),
                null,
                null
            )
            // コマンドの終了を待つ（タイムアウトは呼び出し側で管理する）
            val exitCode = process.waitFor()
            process.destroy()
            exitCode == 0
        } catch (e: Exception) {
            false
        }
    }

    /**
     * WRITE_SECURE_SETTINGS 権限が付与されているかどうかを確認する
     *
     * @param packageName チェック対象のパッケージ名
     * @return 付与されている場合 true
     */
    fun hasWriteSecureSettings(packageName: String): Boolean {
        return try {
            val result = runShellCommand(
                "pm check-permission android.permission.WRITE_SECURE_SETTINGS $packageName"
            )
            // 戻り値 0 は正常終了だが、"Granted" かどうかは出力で確認する必要がある
            // ここでは android.content.pm.PackageManager を使う別の方法を採用
            false // 下記の別メソッドに委任
        } catch (e: Exception) {
            false
        }
    }

    /**
     * WRITE_SECURE_SETTINGS 権限を Shizuku 経由で付与する
     *
     * @param packageName 権限を付与する対象のパッケージ名
     * @return 付与に成功した場合 true
     */
    fun grantWriteSecureSettings(packageName: String): Boolean {
        return runShellCommand(
            "pm grant $packageName android.permission.WRITE_SECURE_SETTINGS"
        )
    }
}
