package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseInteractionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.date.CalendarElement;

import java.util.Calendar;
import java.util.Date;

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

    /**
     * Validate that the date picker works correctly, assuming the use is
     * already in the DoB dialog screen.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<Boolean> rxValidateDoBDatePicker() {
        BaseEngine<?> engine = currentEngine();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberTestUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberTestUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberTestUtil.randomBetween(2017, 2018));
        final Date DATE = calendar.getTime();
        return engine.rxSelectDate(() -> DATE);
    }
}
