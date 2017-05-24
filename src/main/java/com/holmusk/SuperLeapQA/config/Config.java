package com.holmusk.SuperLeapQA.config;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.TestMode;
import org.swiften.xtestkit.kit.TestKit;
import org.swiften.xtestkit.mobile.android.AndroidEngine;
import org.swiften.xtestkit.mobile.ios.IOSEngine;

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
    @NotNull private static final String IOS_APP_PACKAGE;
    @NotNull private static final String IOS_APP_PATH;
    @NotNull private static final List<Engine> ENGINES;
    @NotNull public static final TestKit TEST_KIT;

    static {
        ANDROID_APP_PACKAGE = "com.holmusk.superleap";
        ANDROID_APP_ACTIVITY = ".ui.activity.SplashActivity";
        IOS_APP_PACKAGE = "com.holmusk.superleap";
        IOS_APP_PATH = Paths.get("app", "Superleap.app").toString();

        ENGINES = new LinkedList<>();

//        ENGINES.add(IOSEngine.builder()
//            .withDeviceUID("6F4DE59F-5320-4FA4-BCC5-8932A4A41BBD")
//            .withApp(IOS_APP_PATH)
//            .withAppPackage(IOS_APP_PACKAGE)
//            .withDeviceName("iPhone 7")
//            .withPlatformVersion("10.3")
//            .withTestMode(TestMode.SIMULATED)
//            .build());
//
        ENGINES.add(IOSEngine.builder()
            .withDeviceUID("771F28EC-C5FC-45B8-8C2A-AF346B219936")
            .withApp(IOS_APP_PATH)
            .withAppPackage(IOS_APP_PACKAGE)
            .withDeviceName("iPhone 7 Plus")
            .withPlatformVersion("10.3")
            .withTestMode(TestMode.SIMULATED)
            .build());

//        ENGINES.add(AndroidEngine.builder()
//            .withAppActivity(ANDROID_APP_ACTIVITY)
//            .withDeviceUID("4d00167552895059")
//            .withAppPackage(ANDROID_APP_PACKAGE)
//            .withDeviceName("GT_9500")
//            .withAutomation(Automation.APPIUM)
//            .withPlatformVersion("5.1")
//            .withTestMode(TestMode.ACTUAL)
//            .build());
//
//        ENGINES.add(AndroidEngine.builder()
//            .withAppActivity(ANDROID_APP_ACTIVITY)
//            .withAppPackage(ANDROID_APP_PACKAGE)
//            .withDeviceName("Nexus_4_API_22")
//            .withPlatformVersion("5.1")
//            .withTestMode(TestMode.SIMULATED)
//            .build());
//
//        ENGINES.add(AndroidEngine.builder()
//            .withAppActivity(ANDROID_APP_ACTIVITY)
//            .withAppPackage(ANDROID_APP_PACKAGE)
//            .withDeviceName("Nexus_4_API_23")
//            .withPlatformVersion("5.1")
//            .withTestMode(TestMode.SIMULATED)
//            .build());

        TEST_KIT = TestKit.builder()
            .withEngines(ENGINES)
            .addResourceBundle("Strings", Locale.US)
            .build();
    }

    public static int runCount() {
        return ENGINES.size();
    }
}
