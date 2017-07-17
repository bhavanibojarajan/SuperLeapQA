package com.holmusk.SuperLeapQA.config;

import com.holmusk.HMUITestKit.android.HMAndroidEngine;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.android.type.AndroidSDK;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.TestMode;
import org.swiften.xtestkit.kit.TestKit;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by haipham on 5/7/17.
 */
public final class Config {
    @NotNull private static final String ANDROID_APP_PACKAGE;
    @NotNull private static final String ANDROID_APP_ACTIVITY;
    @NotNull private static final String ANDROID_APP_PATH;
    @NotNull private static final String IOS_APP_PACKAGE;
    @NotNull private static final String IOS_APP_PATH;
    @NotNull private static final List<Engine> ENGINES;
    @NotNull public static final TestKit TEST_KIT;

    public static final int MAX_PHOTO_COUNT = 4;
    public static final int STEP_PER_MIN = 130;

    static {
        ANDROID_APP_PACKAGE = "com.holmusk.superleap";
        ANDROID_APP_ACTIVITY = ".ui.activity.SplashActivity";
        ANDROID_APP_PATH = Paths.get("app", "app-debug.apk").toString();
        IOS_APP_PACKAGE = "com.holmusk.superleap";
        IOS_APP_PATH = Paths.get("app", "Superleap.app").toString();

        ENGINES = new LinkedList<>();

//        ENGINES.add(HMIOSEngine.builder()
//            .withDeviceUID("6F4DE59F-5320-4FA4-BCC5-8932A4A41BBD")
//            .withApp(IOS_APP_PATH)
//            .withAppPackage(IOS_APP_PACKAGE)
//            .withDeviceName("iPhone 7")
//            .withPlatformVersion("10.3")
//            .withTestMode(TestMode.SIMULATED)
//            .build());
//
//        ENGINES.add(HMIOSEngine.builder()
//            .withDeviceUID("771F28EC-C5FC-45B8-8C2A-AF346B219936")
//            .withApp(IOS_APP_PATH)
//            .withAppPackage(IOS_APP_PACKAGE)
//            .withDeviceName("iPhone 7 Plus")
//            .withPlatformVersion("10.3")
//            .withTestMode(TestMode.SIMULATED)
//            .build());
//
//        ENGINES.add(HMIOSEngine.builder()
//            .withDeviceUID("346114F4-41D4-455A-B6BD-427BCDB3E11A")
//            .withApp(IOS_APP_PATH)
//            .withAppPackage(IOS_APP_PACKAGE)
//            .withDeviceName("iPhone 6 Plus iOS 9.3")
//            .withPlatformVersion("9.3")
//            .withTestMode(TestMode.SIMULATED)
//            .build());
//
//        ENGINES.add(HMAndroidEngine.builder()
//            .withSDK(AndroidSDK.SDK_18)
//            .withAppActivity(ANDROID_APP_ACTIVITY)
//            .withAppPackage(ANDROID_APP_PACKAGE)
//            .withDeviceName("Nexus_4_API_18")
//            .withTestMode(TestMode.SIMULATED)
//            .build());
//
        ENGINES.add(HMAndroidEngine.builder()
            .withSDK(AndroidSDK.SDK_22)
            .withAppActivity(ANDROID_APP_ACTIVITY)
            .withAppPackage(ANDROID_APP_PACKAGE)
            .withDeviceName("Nexus_4_API_22")
            .withTestMode(TestMode.SIMULATED)
            .build());

//        ENGINES.add(HMAndroidEngine.builder()
//            .withSDK(AndroidSDK.SDK_23)
//            .withAppActivity(ANDROID_APP_ACTIVITY)
//            .withAppPackage(ANDROID_APP_PACKAGE)
//            .withApp(ANDROID_APP_PATH)
//            .withDeviceName("Nexus_4_API_23")
//            .withTestMode(TestMode.SIMULATED)
//            .build());

        TEST_KIT = TestKit.builder()
            .withEngines(ENGINES)
            .addResourceBundle("SuperleapQA", Locale.US)
            .addResourceBundle("HMUITestKit", Locale.US)
            .build();
    }

    public static int runCount() {
        return ENGINES.size();
    }
}
