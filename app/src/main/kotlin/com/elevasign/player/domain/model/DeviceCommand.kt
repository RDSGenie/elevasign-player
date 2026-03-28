package com.elevasign.player.domain.model

enum class CommandType {
    RESTART_APP,
    REBOOT_DEVICE,
    TAKE_SCREENSHOT,
    CLEAR_CACHE,
    FORCE_SYNC,
    SET_VOLUME,
    UNKNOWN,
}

data class DeviceCommand(
    val id: String,
    val type: CommandType,
    val payload: Map<String, Any>?,
) {
    companion object {
        fun fromString(type: String): CommandType = when (type) {
            "restart_app" -> CommandType.RESTART_APP
            "reboot_device" -> CommandType.REBOOT_DEVICE
            "take_screenshot" -> CommandType.TAKE_SCREENSHOT
            "clear_cache" -> CommandType.CLEAR_CACHE
            "force_sync" -> CommandType.FORCE_SYNC
            "set_volume" -> CommandType.SET_VOLUME
            else -> CommandType.UNKNOWN
        }
    }
}
