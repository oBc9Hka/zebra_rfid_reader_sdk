import Foundation

class TagDataResponse: Codable {
    var tagId: String
    var lastSeenTime: String
    var sdkLog: String?

    init(tagId: String, lastSeenTime: String, sdkLog: String? = nil) {
        self.tagId = tagId
        self.lastSeenTime = lastSeenTime
        self.sdkLog = sdkLog
    }

    func toJson() -> String? {
        let encoder = JSONEncoder()
        encoder.outputFormatting = .prettyPrinted
        if let jsonData = try? encoder.encode(self) {
            return String(data: jsonData, encoding: .utf8)
        }
        return nil
    }
}
