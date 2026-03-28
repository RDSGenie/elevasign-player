package com.elevasign.player.domain.model

data class PlaybackItem(
    val mediaId: String,
    val localPath: String?,
    val fileUrl: String?,
    val fileType: String,
    val durationSeconds: Int,
    val sortOrder: Int,
    val playlistId: String,
    val playlistName: String,
) {
    val isVideo: Boolean get() = fileType.startsWith("video/")
    val isImage: Boolean get() = fileType.startsWith("image/")

    /** Returns local path if available, otherwise falls back to remote URL. */
    val effectiveUri: String? get() = localPath ?: fileUrl
}
