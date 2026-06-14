package com.smartpocket.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

data class UpdateInfo(
    val latestVersion: String,
    val downloadUrl: String,
    val releaseNotes: String
)

class UpdateManager(private val context: Context) {

    companion object {
        private const val GITHUB_API = "https://api.github.com/repos/ShadowMan2010/SmartPocket/releases/latest"
        private const val APK_FILE = "SmartPocket-Update.apk"
    }

    suspend fun checkForUpdate(): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val conn = URL(GITHUB_API).openConnection() as HttpURLConnection
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json")
            conn.connectTimeout = 10_000
            conn.readTimeout = 10_000

            if (conn.responseCode != 200) return@withContext null

            val json = conn.inputStream.bufferedReader().readText()
            conn.disconnect()

            val release = JSONObject(json)
            val tag = release.getString("tag_name")
            val notes = release.optString("body", "")

            val assets = release.getJSONArray("assets")
            var apkUrl: String? = null
            for (i in 0 until assets.length()) {
                val asset = assets.getJSONObject(i)
                if (asset.getString("name").endsWith(".apk")) {
                    apkUrl = asset.getString("browser_download_url")
                    break
                }
            }

            if (apkUrl == null) return@withContext null

            val currentVersion = "v${context.packageManager.getPackageInfo(context.packageName, 0).versionName}"
            if (tag <= currentVersion) return@withContext null

            UpdateInfo(tag, apkUrl, notes)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun downloadAndInstall(update: UpdateInfo, onProgress: (Int) -> Unit = {}): Boolean = withContext(Dispatchers.IO) {
        try {
            val dir = File(context.cacheDir, "updates")
            dir.mkdirs()

            val file = File(dir, APK_FILE)
            if (file.exists()) file.delete()

            val conn = URL(update.downloadUrl).openConnection() as HttpURLConnection
            conn.connectTimeout = 15_000
            conn.readTimeout = 15_000
            conn.connect()

            val total = conn.contentLengthLong
            val input = conn.inputStream
            val output = FileOutputStream(file)

            val buffer = ByteArray(8192)
            var downloaded: Long = 0
            var lastReported = -1
            var bytesRead: Int

            while (input.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
                downloaded += bytesRead
                if (total > 0) {
                    val pct = ((downloaded * 100) / total).toInt()
                    if (pct != lastReported) {
                        lastReported = pct
                        withContext(Dispatchers.Main) { onProgress(pct) }
                    }
                }
            }

            output.close()
            input.close()
            conn.disconnect()

            withContext(Dispatchers.Main) {
                installApk(file)
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun installApk(file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.startActivity(intent)
    }
}
