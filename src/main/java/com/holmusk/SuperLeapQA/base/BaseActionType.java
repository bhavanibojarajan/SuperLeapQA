package com.holmusk.SuperLeapQA.base;

/**
 * Created by haipham on 5/7/17.
 */

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.locator.general.type.BaseLocatorErrorType;
import org.swiften.xtestkit.base.type.BaseErrorType;
import org.swiften.xtestkit.base.type.PlatformErrorType;
import org.swiften.xtestkit.test.type.BaseTestType;

import java.util.concurrent.TimeUnit;

/**
 * Interfaces that extend this should declare methods that assist with app
 * navigation.
 */
public interface BaseActionType extends
    BaseErrorType,
    BaseValidationType,
    BaseLocatorErrorType,
    PlatformErrorType
{
    /**
     * Navigate backwards by clicking the back button.
     * @return A {@link Flowable} instance.
     * @see #generalDelay()
     * @see #rxBackButton()
     * @see #engine()
     * @see org.swiften.xtestkit.base.BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxNavigateBackWithBackButton() {
        long delay = generalDelay();

        return rxBackButton()
            .flatMap(engine()::rxClick).map(a -> true)
            .delay(delay, TimeUnit.MILLISECONDS, Schedulers.trampoline());
    }

    /**
     * Watch the progress bar until it's no longer visible.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see #rxProgressBar()
     * @see BaseEngine#rxWatchUntilNoLongerVisible(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxWatchUntilProgressBarNoLongerVisible() {
        final BaseEngine<?> ENGINE = engine();

        return rxProgressBar()
            .flatMap(ENGINE::rxWatchUntilNoLongerVisible)
            .onErrorReturnItem(true);
    }
}
