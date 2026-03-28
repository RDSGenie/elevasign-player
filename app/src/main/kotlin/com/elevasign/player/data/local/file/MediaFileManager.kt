package com.elevasign.player.data.local.file

import android.content.Context
import android.os.StatFs
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaFileManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val mediaDir: File
        get() = File(context.filesDir, "media").also { it.mkdirs() }

    /** Returns the local File for a given mediaId + file extension (derived from fileType MIME). */
    fun localFile(mediaId: String, fileType: String): File {
        val ext = extensionFromMime(fileType)
        return File(mediaDir, "$mediaId.$ext")
    }

    /** Returns true if the local cache file exists. */
    fun isCached(mediaId: String, fileType: String): Boolean =
        localFile(mediaId, fileType).exists()

    /** Delete a single cached file. */
    fun deleteFile(mediaId: String, fileType: String) {
        localFile(mediaId, fileType).delete()
    }

    /** Delete all cached media files. */
    fun clearAll() {
        mediaDir.listFiles()?.forEach { it.delete() }
        Log.i("MediaFileManager", "Cache cleared")
    }

    /**
     * Evict oldest files if storage usage exceeds 60% of total internal storage.
     * Keeps files in [keepIds] (current playlist). Removes oldest files first.
     */
    fun evictIfNeeded(keepIds: Set<String>) {
        val stat = StatFs(context.filesDir.path)
        val totalBytes = stat.blockCountLong * stat.blockSizeLong
        val freeBytes = stat.availableBlocksLong * stat.blockSizeLong
        val usedPercent = ((totalBytes - freeBytes).toDouble() / totalBytes.toDouble()) * 100.0

        if (usedPercent < 60.0) return

        Log.w("MediaFileManager", "Storage at %.1f%%, evicting old files".format(usedPercent))

        val allFiles = mediaDir.listFiles()
            ?.sortedBy { it.lastModified() }
            ?: return

        for (file in allFiles) {
            val id = file.nameWithoutExtension
            if (id !in keepIds) {
                val deleted = file.delete()
                if (deleted) Log.d("MediaFileManager", "Evicted: ${file.name}")

                // Re-check usage after each delete
                val statAfter = StatFs(context.filesDir.path)
                val totalAfter = statAfter.blockCountLong * statAfter.blockSizeLong
                val freeAfter = statAfter.availableBlocksLong * statAfter.blockSizeLong
                val usedAfter = ((totalAfter - freeAfter).toDouble() / totalAfter.toDouble()) * 100.0
                if (usedAfter < 50.0) break
            }
        }
    }

    /** Storage stats for heartbeat telemetry (in MB). */
    fun storageStats(): Pair<Long, Long> {
        val stat = StatFs(context.filesDir.path)
        val totalMb = (stat.blockCountLong * stat.blockSizeLong) / (1024 * 1024)
        val freeMb = (stat.availableBlocksLong * stat.blockSizeLong) / (1024 * 1024)
        return Pair(totalMb, freeMb)
    }

    private fun extensionFromMime(mimeType: String): String = when {
        mimeType.startsWith("image/jpeg") || mimeType.startsWith("image/jpg") -> "jpg"
        mimeType.startsWith("image/png") -> "png"
        mimeType.startsWith("image/gif") -> "gif"
        mimeType.startsWith("image/webp") -> "webp"
        mimeType.startsWith("video/mp4") -> "mp4"
        mimeType.startsWith("video/webm") -> "webm"
        mimeType.startsWith("video/") -> "mp4"
        mimeType.startsWith("image/") -> "jpg"
        else -> "bin"
    }
}
