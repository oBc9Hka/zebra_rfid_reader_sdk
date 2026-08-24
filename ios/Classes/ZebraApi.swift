#if !targetEnvironment(simulator)
class ZebraApi {
    static let shared: srfidISdkApi = srfidSdkFactory.createRfidSdkApiInstance()
    
    private init() {}
}
#endif

