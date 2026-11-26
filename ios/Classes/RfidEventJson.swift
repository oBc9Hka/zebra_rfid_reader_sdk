import Foundation

enum RfidEventJson {
    static func trigger(action: String, ignored: String? = nil) -> String {
        var map: [String: Any] = ["rfidEvent": "trigger", "action": action]
        if let ignored {
            map["ignored"] = ignored
        }
        return toJson(map)
    }

    static func inventory(action: String, code: String?, message: String?) -> String {
        toJson([
            "rfidEvent": "inventory",
            "action": action,
            "ok": false,
            "code": code as Any,
            "message": message as Any,
        ])
    }

    static func connectError(
        code: String?,
        message: String?,
        regionAuto: String? = nil,
        action: String? = nil
    ) -> String {
        var map: [String: Any] = [
            "rfidEvent": "connectError",
            "ok": false,
            "code": code as Any,
            "message": message as Any,
        ]
        if let regionAuto {
            map["regionAuto"] = regionAuto
        }
        if let action {
            map["action"] = action
        }
        return toJson(map)
    }

    static func config(field: String, code: String?, message: String?) -> String {
        toJson([
            "rfidEvent": "config",
            "ok": false,
            "field": field,
            "code": code as Any,
            "message": message as Any,
        ])
    }

    static func summary(sdkCount: Int?) -> String {
        toJson(["rfidEvent": "summary", "sdkCount": sdkCount as Any])
    }

    private static func toJson(_ map: [String: Any]) -> String {
        guard JSONSerialization.isValidJSONObject(map),
              let data = try? JSONSerialization.data(withJSONObject: map, options: []),
              let string = String(data: data, encoding: .utf8)
        else {
            return "{\"rfidEvent\":\"unknown\"}"
        }
        return string
    }
}
