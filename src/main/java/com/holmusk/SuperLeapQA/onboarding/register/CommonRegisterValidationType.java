package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseInteractionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by haipham on 5/8/17.
 */
public interface CommonRegisterValidationType extends BaseInteractionType {
    /**
     * Get the minimum acceptable age for the current sign up mode.
     * @return An {@link Integer} value.
     */
    int minAcceptableAge();

    /**
     * Get the maximum acceptable age for the current sign up mode.
     * @return An {@link Integer} value.
     */
    int maxAcceptableAge();

    /**
     * Get the acceptable age range for the current sign up mode.
     * @return A {@link List} of {@link Integer}.
     */
    @NotNull
    default List<Integer> acceptableAgeRange() {
        int minAge = minAcceptableAge();
        int maxAge = maxAcceptableAge() + 1;

        return Arrays.asList(IntStream
            .range(minAge, maxAge)
            .boxed()
            .toArray(Integer[]::new));
    }

    /**
     * Get the {@link String} representation of the acceptable age range.
     * @return A {@link String} value.
     * @see #acceptableAgeRange()
     */
    @NotNull
    default String acceptableAgeRangeString() {
        int minAge = minAcceptableAge();
        int maxAge = maxAcceptableAge();
        return String.format("%1$d-%2$d", minAge, maxAge);
    }

    /**
     * Get the DoB's editable text field.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxDoBEditableField() {
        return engine()
            .rxAllEditableElements()
            .firstElement()
            .toFlowable();
    }

    /**
     * Validate that the DoB editable field displays a {@link String}
     * representation of a {@link Date}, assuming the user is in the
     * pre-DoB picker screen.
     * @param date A {@link Date} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<Boolean> rxDoBEditableFieldHasDate(@NotNull Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String string = formatter.format(date);
        return engine().rxElementContainingText(string).map(ObjectUtil::nonNull);
    }

    /**
     * Get all calendar {@link WebElement} instances.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxDoBElements() {
        BaseEngine<?> engine = engine();
        return engine.rxAllCalendarElements();
    }

    /**
     * Get the editable field for the user's name for future notifications.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingID(String)
     */
    @NotNull
    default Flowable<WebElement> rxNameFieldForFutureNotification() {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID("et_name");
        } else {
            return RxUtil.error(PLATFORM_UNAVAILABLE);
        }
    }

    /**
     * Get the editable field for the user's email for future notifications.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingID(String)
     */
    @NotNull
    default Flowable<WebElement> rxEmailFieldForFutureNotification() {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID("et_email");
        } else {
            return RxUtil.error(PLATFORM_UNAVAILABLE);
        }
    }

    /**
     * Get the editable field for the user's phone for future notifications.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingID(String)
     */
    @NotNull
    default Flowable<WebElement> rxPhoneFieldForFutureNotification() {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID("et_phone");
        } else {
            return RxUtil.error(PLATFORM_UNAVAILABLE);
        }
    }

    /**
     * Validate the screen after the DoB picker whereby the user is notified
     * that he/she/the child is not qualified for the program due to age
     * restrictions.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateUnacceptableAgeScreen() {
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .concatArray(
                ENGINE.rxElementContainingText("register_title_weAreOnlyAccepting"),
                ENGINE.rxElementContainingText(acceptableAgeRangeString()),
                ENGINE.rxElementContainingText("+65"),
                rxNameFieldForFutureNotification(),
                rxPhoneFieldForFutureNotification(),
                rxEmailFieldForFutureNotification()
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
}
