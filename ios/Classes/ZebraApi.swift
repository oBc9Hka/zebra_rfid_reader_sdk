#if !targetEnvironment(simulator)
import ZebraRfidSdkFramework

class ZebraApi {
    static let shared: srfidISdkApi = srfidSdkFactory.createRfidSdkApiInstance()
    
    private init() {}
}
#endif

