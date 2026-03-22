# Galaxy Shutter Mute

<p align="center">
  <img alt="License" src="https://img.shields.io/badge/license-MIT-blue.svg">
  <img alt="Android" src="https://img.shields.io/badge/Android-9.0%2B-green.svg">
  <img alt="OneUI" src="https://img.shields.io/badge/OneUI-1.0%20~%208.5-blue.svg">
</p>

Galaxy端末のカメラシャッター音をワンタップでオン／オフできるアプリです。

## 概要

GalaxyのCSC設定キー `csc_pref_camera_forced_shuttersound_key` を書き換えることで、カメラアプリのシャッター音を制御します。

| 値 | 状態 |
|---|---|
| `1` | シャッター音 ON |
| `0` | シャッター音 OFF |

## 動作確認済み環境

- **OneUI 1.0 〜 OneUI 8.5** (Android 9.0 〜 Android 16)

## 必要なもの

- **[Shizuku](https://shizuku.rikka.app/)** アプリ（権限付与のために必要）
- `WRITE_SECURE_SETTINGS` 権限（Shizuku が自動で付与します）

## インストール方法

> [!WARNING]
> このアプリは `targetSdkVersion=21` のため、通常の方法ではインストールできません。  
> **[Galaxy Shutter Mute Manager](https://github.com/ryuya-dev/galaxy-shutter-mute-manager)** を使ってインストールしてください。

### Manager を使ったインストール（推奨）

1. [Galaxy Shutter Mute Manager](https://github.com/ryuya-dev/galaxy-shutter-mute-manager/releases/latest) をダウンロードしてインストール
2. Manager アプリを起動し、Shizuku の認証を許可
3. 「インストール」ボタンをタップ
4. 自動的に `WRITE_SECURE_SETTINGS` 権限が付与されて完了

### ADB を使った手動インストール

```bash
# APKをリリースページからダウンロード後
adb shell pm install --bypass-low-target-sdk-block galaxy-shutter-mute.apk
adb shell pm grant net.ryuya.dev.galaxyshutter.mute android.permission.WRITE_SECURE_SETTINGS
```

## 使い方

1. アプリを起動します
2. Shizuku が未起動の場合は案内に従って起動してください
3. メイン画面のトグルでシャッター音をオン／オフします

## Galaxy Shutter Mute Manager との連携

| 機能 | Manager | Shutter Mute |
|---|---|---|
| インストール | ✅ Manager が実施 | — |
| `WRITE_SECURE_SETTINGS` 付与 | ✅ 自動付与 | — |
| シャッター音制御 | — | ✅ |
| アップデート | ✅ Manager が実施 | — |

## 技術スタック

- **Kotlin** / **Jetpack Compose** / **Material 3**
- **Shizuku API** 13.1.5
- `targetSdkVersion=21`（`csc_pref_camera_forced_shuttersound_key` へのアクセスに必要）

## パッケージ名

`net.ryuya.dev.galaxyshutter.mute`

## ライセンス

MIT License
