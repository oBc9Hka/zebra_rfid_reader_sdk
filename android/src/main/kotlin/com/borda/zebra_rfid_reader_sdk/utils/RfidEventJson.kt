package com.borda.zebra_rfid_reader_sdk.utils

import com.google.gson.Gson

/**
 * Builds JSON envelopes for diagnostic events sent on [tagHandlerEvent]
 * without colliding with [ReaderResponse] / [BordaReaderDevice] parsing.
 */
object RfidEventJson {
    private val gson = Gson()

    fun trigger(action: String, ignored: String? = null): String {
        val map = linkedMapOf<String, Any?>("rfidEvent" to "trigger", "action" to action)
        if (ignored != null) map["ignored"] = ignored
        return gson.toJson(map)
    }

    fun inventory(action: String, code: String?, message: String?): String {
        val map = linkedMapOf<String, Any?>(
            "rfidEvent" to "inventory",
            "action" to action,
            "ok" to false,
            "code" to code,
            "message" to message,
        )
        return gson.toJson(map)
    }

    fun connectError(
        code: String?,
        message: String?,
        regionAuto: String? = null,
        action: String? = null,
    ): String {
        val map = linkedMapOf<String, Any?>(
            "rfidEvent" to "connectError",
            "ok" to false,
            "code" to code,
            "message" to message,
        )
        if (regionAuto != null) map["regionAuto"] = regionAuto
        if (action != null) map["action"] = action
        return gson.toJson(map)
    }

    fun config(field: String, code: String?, message: String?): String {
        val map = linkedMapOf<String, Any?>(
            "rfidEvent" to "config",
            "ok" to false,
            "field" to field,
            "code" to code,
            "message" to message,
        )
        return gson.toJson(map)
    }

    fun summary(sdkCount: Int?): String {
        val map = linkedMapOf<String, Any?>("rfidEvent" to "summary", "sdkCount" to sdkCount)
        return gson.toJson(map)
    }
}
