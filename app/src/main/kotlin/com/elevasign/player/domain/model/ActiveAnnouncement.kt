package com.elevasign.player.domain.model

enum class AnnouncementDisplayType { OVERLAY, FULLSCREEN, TICKER }

data class ActiveAnnouncement(
    val id: String,
    val title: String,
    val body: String,
    val displayType: AnnouncementDisplayType,
    val bgColor: String,
    val textColor: String,
    val priority: Int,
) {
    companion object {
        fun fromString(type: String): AnnouncementDisplayType = when (type) {
            "fullscreen" -> AnnouncementDisplayType.FULLSCREEN
            "ticker" -> AnnouncementDisplayType.TICKER
            else -> AnnouncementDisplayType.OVERLAY
        }
    }
}
