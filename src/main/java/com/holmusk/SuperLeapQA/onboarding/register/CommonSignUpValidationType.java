package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseInteractionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.xtestkit.engine.base.BaseEngine;

/**
 * Created by haipham on 5/8/17.
 */
public interface CommonSignUpValidationType extends BaseInteractionType {
    /**
     * Get the DoB's editable text field.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxDoBEditableField() {
        return currentEngine()
            .rxAllEditableElements()
            .firstElement()
            .toFlowable();
    }

    /**
     * Get all calendar {@link WebElement} instances.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxDoBElements() {
        BaseEngine<?> engine = currentEngine();
        return engine.rxAllCalendarElements();
    }
}
