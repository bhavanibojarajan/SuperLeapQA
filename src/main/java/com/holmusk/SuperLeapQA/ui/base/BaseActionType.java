package com.holmusk.SuperLeapQA.ui.base;

/**
 * Created by haipham on 5/7/17.
 */

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.locator.general.type.BaseLocatorErrorType;
import org.swiften.xtestkit.base.type.BaseErrorType;

import java.util.concurrent.TimeUnit;

/**
 * Interfaces that extend this should declare methods that assist with app
 * navigation.
 */
public interface BaseActionType extends BaseErrorType, BaseValidationType, BaseLocatorErrorType {
    /**
     * Navigate backwards by clicking the back button.
     * @return {@link Flowable} instance.
     * @param ENGINE {@link Engine} instance.
     * @see #generalDelay()
     * @see #rxBackButton(Engine)
     * @see BooleanUtil#toTrue(Object)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rx_a_clickBackButton(@NotNull final Engine<?> ENGINE) {
        long delay = generalDelay();

        return rxBackButton(ENGINE)
            .flatMap(ENGINE::rx_click).map(BooleanUtil::toTrue)
            .delay(delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Watch the progress bar until it's no longer visible.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_progressBar(Engine)
     * @see Engine#rx_watchUntilHidden(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rx_a_watchProgressBarUntilHidden(@NotNull final Engine<?> ENGINE) {
        return rx_progressBar(ENGINE)
            .flatMap(ENGINE::rx_watchUntilHidden)
            .onErrorReturnItem(true);
    }
}
