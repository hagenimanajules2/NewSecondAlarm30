SECOND ALARM - Android Studio project

New feature:
- 30-minute repeating alarm with ON/OFF controls.
- When turned ON, the first alarm rings after 30 minutes (not immediately).
- After each ring, the next alarm is scheduled 30 minutes later.
- Turning OFF cancels the next scheduled repeating alarm.
- Repeating alarm state is saved and restored after a phone reboot; the 30-minute countdown restarts after boot.
- The original one-time date/time alarm remains available.

Build:
1. Install Android Studio on a computer.
2. Extract this ZIP.
3. In Android Studio choose Open, then select the extracted SecondAlarm folder.
4. Allow Gradle sync to finish. Android Studio may download the Android Gradle plugin and SDK components.
5. Choose Build > Build Bundle(s) / APK(s) > Build APK(s).
6. Find the APK using the "locate" link in the build notification, then transfer it to the phone and install it.

Important limitations:
- This ZIP contains the Android Studio source project, not a prebuilt APK.
- Android/TECNO battery management can delay alarms. Exempt Second Alarm from battery optimization if that option is available.
- Keep the phone powered on. If the phone is rebooted, the repeating alarm is restored and its 30-minute countdown starts again after boot.
- Alarm delivery depends on Android device power management and manufacturer restrictions. Test before relying on it.
