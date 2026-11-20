package com.borda.zebra_rfid_reader_sdk.utils

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class TagDataResponse {
    @SerializedName("tagId")
    private var tagId: String = ""

    @SerializedName("lastSeenTime")
    private var lastSeenTime: String = ""

    @SerializedName("sdkLog")
    private var sdkLog: String? = null

    constructor(tagId: String, lastSeenTime: String, sdkLog: String? = null) {
        this.tagId = tagId
        this.lastSeenTime = lastSeenTime
        this.sdkLog = sdkLog
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}
