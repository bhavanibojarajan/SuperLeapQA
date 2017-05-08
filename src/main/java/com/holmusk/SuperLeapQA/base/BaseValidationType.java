package com.holmusk.SuperLeapQA.base;

/**
 * Created by haipham on 5/7/17.
 */

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.engine.base.BaseEngine;
import org.swiften.xtestkit.engine.base.type.PlatformType;
import org.swiften.xtestkit.engine.mobile.Platform;
import org.swiften.xtestkit.test.type.BaseTestType;

/**
 * Interfaces that extend this should declare methods that assist with app
 * validation (e.g. make sure all views are present).
 */
public interface BaseValidationType extends BaseTestType, AppDelayType {
    /**
     * Get the back button in register screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxBackButton() {
        BaseEngine<?> engine = currentEngine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID("btnBack");
        } else {
            return Flowable.empty();
        }
    }
}
