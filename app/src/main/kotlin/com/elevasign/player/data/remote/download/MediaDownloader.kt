package com.elevasign.player.data.remote.download

import android.util.Log
import com.elevasign.player.data.local.file.MediaFileManager
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaDownloader @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val mediaFileManager: MediaFileManager,
) {
    companion object {
        private const val TAG = "MediaDownloader"
    }

    /**
     * Downloads [url] to the local cache file for [mediaId].
     * Supports resume via Range headers if a partial file already exists.
     * Returns the local File on success, throws IOException on failure.
     */
    @Throws(IOException::class)
    suspend fun download(url: String, mediaId: String, fileType: String): File {
        val destFile = mediaFileManager.localFile(mediaId, fileType)

        // Already fully downloaded (non-empty file exists)
        if (destFile.exists() && destFile.length() > 0) {
            Log.d(TAG, "Already cached: $mediaId")
            return destFile
        }

        val partialFile = File(destFile.parent, "${destFile.name}.part")
        val existingBytes = if (partialFile.exists()) partialFile.length() else 0L

        val requestBuilder = Request.Builder().url(url)
        if (existingBytes > 0) {
            requestBuilder.addHeader("Range", "bytes=$existingBytes-")
            Log.d(TAG, "Resuming download from byte $existingBytes: $mediaId")
        } else {
            Log.d(TAG, "Starting download: $mediaId")
        }

        val response = okHttpClient.newCall(requestBuilder.build()).execute()

        if (!response.isSuccessful && response.code != 206) {
            response.close()
            throw IOException("HTTP ${response.code} downloading $mediaId")
        }

        val body = response.body ?: run {
            response.close()
            throw IOException("Empty response body for $mediaId")
        }

        try {
            val appendMode = response.code == 206 // 206 Partial Content = resume
            partialFile.outputStream().let { out ->
                if (!appendMode) {
                    out.use { body.byteStream().copyTo(it) }
                } else {
                    partialFile.appendBytes(body.bytes())
                }
            }
        } finally {
            body.close()
            response.close()
        }

        // Rename partial → final
        partialFile.renameTo(destFile)
        Log.i(TAG, "Downloaded: $mediaId (${destFile.length()} bytes)")
        return destFile
    }

    /** Cancel all in-flight downloads. */
    fun cancelAll() {
        okHttpClient.dispatcher.cancelAll()
    }
}
