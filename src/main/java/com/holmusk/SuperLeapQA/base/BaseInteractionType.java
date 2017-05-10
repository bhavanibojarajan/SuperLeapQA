package com.holmusk.SuperLeapQA.base;

/**
 * Created by haipham on 5/7/17.
 */

import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.xtestkit.base.element.locator.general.type.BaseLocatorErrorType;
import org.swiften.xtestkit.base.element.property.type.BaseElementInteractionErrorType;
import org.swiften.xtestkit.base.type.PlatformErrorType;
import org.swiften.xtestkit.test.type.BaseTestType;

import java.util.concurrent.TimeUnit;

/**
 * Interfaces that extend this should declare methods that assist with app
 * navigation.
 */
public interface BaseInteractionType extends
    AppDelayType,
    BaseTestType,
    BaseLocatorErrorType,
    BaseValidationType,
    PlatformErrorType
{
    /**
     * Navigate backwards by clicking the back button.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxNavigateBackWithBackButton() {
        return rxBackButton()
            .flatMap(engine()::rxClick)
            .delay(generalDelay(), TimeUnit.MILLISECONDS);
    }
}
