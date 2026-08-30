# Remote Android SDK Controller (Java)

Drive the internals of an Android device — Bluetooth, Wi‑Fi, audio, telephony/SMS,
location, media playback, USB and the file system — **from an external test process
on your PC**, without writing an on‑device instrumentation test for every case.

It was built for **hardware‑in‑the‑loop automotive‑infotainment automation**: a bench
of head units flashed by CI, exercised end to end as a user while their radios and
buses are manipulated from the outside.

## How it works

```
JUnit test (PC)
    │
    ├─ AppiumRemoteSdk  ──► Appium  (mobile: shell)  ──┐
    └─ DDMLibRemoteSdk  ──► adb / ddmlib              ──┤
                                                       ▼
                              on-device broadcast receivers
                              (RemoteAndroidApk companion app)
                                                       ▼
                        Android framework managers (Bluetooth,
                        Wifi, Audio, Telephony, Location, ...)
```

A thin `Shell` abstraction runs shell commands and broadcasts on the device. The
first call installs and launches the companion app
([`RemoteAndroidApk`](https://github.com/FruNoman/RemoteAndroidApk)) automatically;
the app holds the privileged permissions and calls the real framework managers.
Two interchangeable transports:

| Controller | Transport | Use when |
|---|---|---|
| `DDMLibRemoteSdk` | `adb` via ddmlib | pure device control, no UI driver needed |
| `AppiumRemoteSdk` | Appium `mobile: shell` | you already have an `AndroidDriver` for UI steps |

## Adapters

`bluetooth` · `wifi` · `audio` · `avrcp` · `location` · `telephony` (calls, contacts,
call history) · `sms` · `player` · `usb` · `environment` (storage volumes, remote files)

## Usage

Maven, via JitPack:

```xml
<repositories>
  <repository><id>jitpack.io</id><url>https://jitpack.io</url></repository>
</repositories>

<dependency>
  <groupId>com.github.FruNoman</groupId>
  <artifactId>JavaRemoteAndroidSDKController</artifactId>
  <version>master-SNAPSHOT</version>
</dependency>
```

(or pin a tag once one is published)

```java
AndroidDebugBridge.initIfNeeded(true);
IDevice device = /* first connected device */;

DDMLibRemoteSdk sdk = new DDMLibRemoteSdk(device);
BluetoothAdapter bluetooth = sdk.getBluetoothAdapter();

bluetooth.enable();
assertTrue(bluetooth.isEnabled());
for (BluetoothDevice d : bluetooth.getBondedDevices()) {
    System.out.println(d.getName() + " " + d.getAddress());
}
```

With Appium instead:

```java
AppiumRemoteSdk sdk = new AppiumRemoteSdk(androidDriver);
sdk.getWifiAdapter().disable();
```

See `src/test/java` for runnable examples per adapter.

## Requirements

- Java 8+
- `adb` on `PATH`
- The companion app must be able to hold system/privileged permissions — a
  userdebug/eng build, a rooted device, or the app installed as a privileged app.
  On automotive images this is the normal bench setup.

## Related

- [`RemoteAndroidApk`](https://github.com/FruNoman/RemoteAndroidApk) — the on‑device companion app
- [`PythonRemoteAndroidSDKController`](https://github.com/FruNoman/python-remote-android-sdk-controller) — Python port

## License

MIT — see [LICENSE](LICENSE).
