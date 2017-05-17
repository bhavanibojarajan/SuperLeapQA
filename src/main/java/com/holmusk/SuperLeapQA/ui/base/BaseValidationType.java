package com.holmusk.SuperLeapQA.ui.base;

/**
 * Created by haipham on 5/7/17.
 */

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.mobile.android.AndroidEngine;
import org.swiften.xtestkit.test.type.BaseTestType;

/**
 * Interfaces that extend this should declare methods that assist with app
 * validation (e.g. make sure all views are present).
 */
public interface BaseValidationType extends BaseTestType, BaseErrorType, AppDelayType {
    /**
     * Get the common back button.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#platform()
     * @see BaseEngine#rxElementContainingID(String...)
     */
    @NotNull
    default Flowable<WebElement> rxBackButton() {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (engine instanceof AndroidEngine) {
            return engine.rxElementContainingID("btnBack");
        } else {
            return RxUtil.error();
        }
    }

    /**
     * Get the common probress bar.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxElementContainingID(String...)
     */
    @NotNull
    default Flowable<WebElement> rxProgressBar() {
        BaseEngine<?> engine = engine();

        if (engine instanceof AndroidEngine) {
            return engine.rxElementContainingID("pb_general");
        } else {
            return RxUtil.error();
        }
    }
}
