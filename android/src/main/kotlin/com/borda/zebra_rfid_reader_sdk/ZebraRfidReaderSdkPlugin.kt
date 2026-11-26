package com.borda.zebra_rfid_reader_sdk

import android.content.Context
import android.util.Log
import com.borda.zebra_rfid_reader_sdk.utils.BordaReaderDevice
import com.borda.zebra_rfid_reader_sdk.utils.ConnectionStatus
import com.borda.zebra_rfid_reader_sdk.utils.LOG_TAG
import com.google.gson.Gson
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** ZebraRfidReaderSdkPlugin */
class ZebraRfidReaderSdkPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var methodChannel: MethodChannel
    private lateinit var connectionHelper: ZebraConnectionHelper
    private lateinit var applicationContext: Context
    private var initError: Exception? = null

    private lateinit var tagHandlerEvent: EventChannel
    private lateinit var tagFindingEvent: EventChannel
    private lateinit var readTagEvent: EventChannel
    private lateinit var tagDataEventHandler: TagDataEventHandler
    private lateinit var readTagEventHandler: TagDataEventHandler
    private lateinit var tagFindingEventHandler: TagDataEventHandler


    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel =
            MethodChannel(flutterPluginBinding.binaryMessenger, "borda/zebra_rfid_reader_sdk")
        methodChannel.setMethodCallHandler(this)

        tagHandlerEvent = EventChannel(flutterPluginBinding.binaryMessenger, "tagHandlerEvent")
        tagFindingEvent = EventChannel(flutterPluginBinding.binaryMessenger, "tagFindingEvent")
        readTagEvent = EventChannel(flutterPluginBinding.binaryMessenger, "readTagEvent")

        tagDataEventHandler = TagDataEventHandler()
        tagFindingEventHandler = TagDataEventHandler()
        readTagEventHandler = TagDataEventHandler()


        tagHandlerEvent.setStreamHandler(tagDataEventHandler)
        tagFindingEvent.setStreamHandler(tagFindingEventHandler)
        readTagEvent.setStreamHandler(readTagEventHandler)
        Log.d(LOG_TAG, "onAttachedToEngine called")
        applicationContext = flutterPluginBinding.applicationContext
        ensureConnectionHelper()
    }

    private fun ensureConnectionHelper(): Boolean {
        if (::connectionHelper.isInitialized) return true
        return try {
            connectionHelper = ZebraConnectionHelper(
                applicationContext,
                tagDataEventHandler,
                tagFindingEventHandler,
                readTagEventHandler,
            )
            true
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Failed to create ZebraConnectionHelper", e)
            initError = e
            false
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (!ensureConnectionHelper()) {
            result.error("init_error", initError?.toString(), Log.getStackTraceString(initError))
            return
        }
        handleMethodCall(call, result)
    }

    private fun handleMethodCall(call: MethodCall, result: Result) =
        when (call.method) {
            "connect" -> {
                Log.d(LOG_TAG, "connect called")
                val name = call.argument<String>("name")!!
                val readerConfig = call.argument<HashMap<String, Any>>("readerConfig")!!
                Log.d(LOG_TAG, "will try to connect to -> $name")
                Log.d(LOG_TAG, "USER CONFIG -> $readerConfig")
                connectionHelper.connect(name, readerConfig)
            }

            "disconnect" -> {
                Log.d(LOG_TAG, "disconnect called")
                connectionHelper.disconnect()
            }

            "setAntennaPower" -> {
                val transmitPowerIndex = call.argument<Int>("transmitPowerIndex")!!
                connectionHelper.setAntennaConfig(transmitPowerIndex)
            }

            "setDynamicPower" -> {
                val isEnable = call.argument<Boolean>("isEnable")!!
                connectionHelper.setDynamicPower(isEnable)
            }

            "setBeeperVolume" -> {
                val level = call.argument<Int>("level")!!
                connectionHelper.setBeeperVolumeConfig(level)
            }

            "findTheTag" -> {
                val tag = call.argument<String>("tag")!!
                Log.d("ENGIN", "findTheTag called with tag -> $tag")
                connectionHelper.findTheTag(tag)
            }

            "stopFindingTheTag" -> {
                connectionHelper.stopFindingTheTag()
            }

            "getAvailableReaderList" -> {
                Log.d(LOG_TAG, "getAvailableReaderList called")
                val readers = connectionHelper.getAvailableReaderList()
                val dataList = mutableListOf<BordaReaderDevice>()
                for (reader in readers) {
                    val device = BordaReaderDevice(
                        ConnectionStatus.notConnected,
                        reader.name.toString(),
                        null,
                        null
                    )
                    dataList.add(device)
                }
                result.success(Gson().toJson(dataList))
            }

            else -> result.notImplemented()
        }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
        tagHandlerEvent.setStreamHandler(null)
        tagFindingEvent.setStreamHandler(null)
        readTagEvent.setStreamHandler(null)
        if (::connectionHelper.isInitialized) connectionHelper.dispose()
    }
}
