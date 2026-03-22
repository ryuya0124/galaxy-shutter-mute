# Kotlin / Compose 向け ProGuard ルール

# Shizuku のモデルクラスを保持する
-keep class rikka.shizuku.** { *; }

# Compose のリフレクション参照を保持する
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
