package net.ryuya.dev.galaxyshutter.mute

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import net.ryuya.dev.galaxyshutter.mute.shizuku.ShizukuHelper
import net.ryuya.dev.galaxyshutter.mute.ui.GalaxyShutterApp

/**
 * アプリのエントリポイントとなるメインアクティビティ
 * Shizuku の権限リスナーを登録・解除する責務を持つ
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Shizuku 権限結果リスナーを登録する
        ShizukuHelper.addPermissionListener()

        setContent {
            GalaxyShutterApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // メモリリークを防ぐためリスナーを解除する
        ShizukuHelper.removePermissionListener()
    }
}
