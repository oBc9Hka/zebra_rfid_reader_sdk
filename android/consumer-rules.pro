# Qualcomm RFID service used by Zebra API3 for the QC_SERIAL transport; provided by device firmware.
-dontwarn com.qti.qrs.**

# Gson serializes these fields by reflection. R8 otherwise deletes them and tag events become {}.
-keepclassmembers class com.borda.zebra_rfid_reader_sdk.utils.TagDataResponse {
    <fields>;
}
