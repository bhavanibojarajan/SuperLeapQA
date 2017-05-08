package com.holmusk.SuperLeapQA.base;

/**
 * Created by haipham on 5/7/17.
 */

import io.reactivex.Completable;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.engine.base.BaseEngine;
import org.swiften.xtestkit.engine.base.type.PlatformType;
import org.swiften.xtestkit.engine.mobile.Platform;
import org.swiften.xtestkit.test.BaseTestType;

import java.util.concurrent.TimeUnit;

/**
 * Interfaces that extend this should declare methods that assist with app
 * navigation.
 */
public interface BaseInteractionType extends AppDelayType, BaseTestType {
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

    /**
     * Navigate backwards by clicking the back button.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxNavigateBackWithBackButton() {
        return rxBackButton()
            .flatMapCompletable(a -> Completable.fromAction(a::click))
            .<Boolean>toFlowable()
            .defaultIfEmpty(true)
            .delay(generalDelay(), TimeUnit.MILLISECONDS);
    }
}
