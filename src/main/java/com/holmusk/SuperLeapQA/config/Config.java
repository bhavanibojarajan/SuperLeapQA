package com.holmusk.SuperLeapQA.config;

import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.engine.base.PlatformEngine;
import org.swiften.xtestkit.engine.mobile.Automation;
import org.swiften.xtestkit.engine.base.TestMode;
import org.swiften.xtestkit.engine.mobile.android.AndroidEngine;
import org.swiften.xtestkit.kit.TestKit;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by haipham on 5/7/17.
 */
public final class Config {
    @NotNull
    private static final String APP_PACKAGE;
    @NotNull private static final String ANDROID_APP_ACTIVITY;
    @NotNull private static final List<PlatformEngine> ENGINES;
    @NotNull public static final TestKit TEST_KIT;

    static {
        APP_PACKAGE = "com.holmusk.superleap";
        ANDROID_APP_ACTIVITY = ".ui.activity.SplashActivity";

        ENGINES = new LinkedList<>();

//        ENGINES.add(IOSEngine.builder()
//            .withDeviceUID("D10524D4-939E-46CA-BE40-AB21F8E745A8")
//            .withAppPackage(APP_PACKAGE)
//            .withDeviceName("iPhone 7")
//            .withPlatformVersion("10.2")
//            .withTestMode(TestMode.SIMULATED)
//            .build());
//
//        ENGINES.add(IOSEngine.builder()
//            .withDeviceUID("CF6E7ACD-F818-4145-A140-75CF1F229A8C")
//            .withAppPackage(APP_PACKAGE)
//            .withDeviceName("iPhone 7 Plus")
//            .withPlatformVersion("10.2")
//            .withTestMode(TestMode.SIMULATED)
//            .build());

        ENGINES.add(AndroidEngine.builder()
            .withAppActivity(ANDROID_APP_ACTIVITY)
            .withDeviceUID("4d00167552895059")
            .withAppPackage(APP_PACKAGE)
            .withDeviceName("GT_9500")
            .withAutomation(Automation.APPIUM)
            .withPlatformVersion("5.1")
            .withTestMode(TestMode.ACTUAL)
            .build());
//
//        ENGINES.add(AndroidEngine.builder()
//            .withAppActivity(ANDROID_APP_ACTIVITY)
//            .withApp(ANDROID_APP_NAME)
//            .withAppPackage(APP_PACKAGE)
//            .withDeviceName("Nexus_4_API_22")
//            .withTestMode(TestMode.SIMULATED)
//            .withPlatformVersion("5.1")
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
