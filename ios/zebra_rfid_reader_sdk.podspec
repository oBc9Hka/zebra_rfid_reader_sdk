Pod::Spec.new do |s|
  s.name             = 'zebra_rfid_reader_sdk'
  s.version          = '0.1.0'
  s.summary          = 'A Flutter plugin for Zebra RFID readers.'
  s.description      = <<-DESC
  This plugin allows Flutter apps to interact with Zebra RFID readers.
  DESC
  s.homepage         = 'https://yourpluginhomepage.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Name' => 'your_email@example.com' }
  s.source           = { :path => '.' }
  s.source_files     = 'Classes/**/*.{h,m,swift}'
  s.static_framework = true
  s.dependency       'Flutter'
  s.platform         = :ios, '14.0'
  s.frameworks       = 'CoreBluetooth', 'ExternalAccessory'

  # libsymbolrfid-sdk.a is device arm64 + simulator x86_64 only.
  # It has no arm64-simulator slice, so vendoring it makes CocoaPods inject
  # EXCLUDED_ARCHS[sdk=iphonesimulator*]=arm64 into the host app and Xcode
  # then reports no matching simulator destination on Apple Silicon.
  # Keep the binary as a path resource and link it only for iphoneos.
  s.preserve_paths = 'libraries/symbolrfid-sdk/**/*'

  s.subspec 'symbolrfid-sdk' do |symbolrfid|
    symbolrfid.source_files   = 'libraries/symbolrfid-sdk/include/*'
    symbolrfid.xcconfig       = { 'HEADER_SEARCH_PATHS' => "${PODS_ROOT}/../.symlinks/plugins/#{s.name}/ios/libraries/symbolrfid-sdk/include" }
  end

  s.info_plist = {
    'UISupportedExternalAccessoryProtocols' => ['com.zebra.rfd8X00_easytext', 'com.zebra.scanner.SSI']
  }

  zebra_lib = '"${PODS_ROOT}/../.symlinks/plugins/zebra_rfid_reader_sdk/ios/libraries/symbolrfid-sdk/libsymbolrfid-sdk.a"'

  s.pod_target_xcconfig = {
    'DEFINES_MODULE' => 'YES',
    'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386',
    'HEADER_SEARCH_PATHS' => '"$(PODS_TARGET_SRCROOT)/libraries/symbolrfid-sdk/include"'
  }
  # Resolve Zebra symbols when the host app links a device build.
  # Do not add this flag for iphonesimulator — that would pull in the
  # device-only arm64 slice and break destination matching again.
  s.user_target_xcconfig = {
    'OTHER_LDFLAGS[sdk=iphoneos*]' => "$(inherited) -force_load #{zebra_lib}"
  }
  s.swift_version = '5.0'
end
