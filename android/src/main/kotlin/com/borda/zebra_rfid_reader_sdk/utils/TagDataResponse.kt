package com.borda.zebra_rfid_reader_sdk.utils

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class TagDataResponse {
    @SerializedName("tagId")
    private var tagId: String = ""

    @SerializedName("lastSeenTime")
    private var lastSeenTime: String = ""

    constructor(tagId: String, lastSeenTime: String) {
        this.tagId = tagId
        this.lastSeenTime = lastSeenTime
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}