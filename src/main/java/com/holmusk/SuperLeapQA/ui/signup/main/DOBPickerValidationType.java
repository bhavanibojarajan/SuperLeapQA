package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.UserMode;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.BaseEngine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface DOBPickerValidationType extends SignUpActionType {
    /**
     * Validate the acceptable age screen, after the user picks DoB. This
     * method is used to check that the screen that follows matches the
     * age requirements specified by each {@link UserMode}.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    Flowable<Boolean> rx_validateAcceptableAgeScreen();

    /**
     * Validate the unacceptable age screen, after the user picks DoB. This
     * method is used to check that the screen that follows matches the
     * age requirements specified by each {@link UserMode}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    Flowable<Boolean> rxValidateUnacceptableAgeScreen(UserMode mode);

    /**
     * Get all calendar {@link WebElement} instances.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxAllCalendarElements()
     */
    @NotNull
    default Flowable<WebElement> rxDoBElements() {
        return engine().rxAllCalendarElements();
    }

    /**
     * Validate the DoB picker screen.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rx_elementContainingText(String...)
     * @see BaseEngine#rx_click(WebElement)
     * @see BaseEngine#rx_navigateBackOnce()
     * @see #rxDoBEditField()
     * @see #rxDoBElements()
     * @see #generalDelay()
     * @see ObjectUtil#nonNull(Object)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateDoBPickerScreen() {
        final BaseEngine<?> ENGINE = engine();
        long delay = generalDelay();

        return Flowable
            .concatArray(
                rxDoBEditField(),
                ENGINE.rx_elementContainingText("register_title_dateOfBirth"),
                ENGINE.rx_elementContainingText(
                    "parentSignUp_title_whatIsYourChild",
                    "teenSignUp_title_whatIsYour"
                )
            )
            .all(ObjectUtil::nonNull)
            .toFlowable()

            /* Open the DoB dialog and verify that all elements are there */
            .flatMap(a -> rxDoBEditField())
            .flatMap(ENGINE::rx_click)
            .flatMap(a -> rxDoBElements().all(ObjectUtil::nonNull).toFlowable())
            .delay(delay, TimeUnit.MILLISECONDS, Schedulers.trampoline())

            /* Dismiss the dialog by navigating back once */
            .flatMap(a -> ENGINE.rx_navigateBackOnce())
            .delay(delay, TimeUnit.MILLISECONDS, Schedulers.trampoline())
            .map(BooleanUtil::toTrue);
    }

    /**
     * Get the DoB's editable text field.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxDoBEditField() {
        return engine().rx_allEditableElements().firstElement().toFlowable();
    }

    /**
     * Validate that the DoB editable field displays a {@link String}
     * representation of a {@link Date}, assuming the user is in the
     * pre-DoB picker screen.
     * @param date A {@link Date} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rx_elementContainingText(String...)
     * @see SimpleDateFormat#format(Date)
     */
    @NotNull
    default Flowable<Boolean> rxDoBEditFieldHasDate(@NotNull Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String string = formatter.format(date);
        return engine().rx_elementContainingText(string).map(ObjectUtil::nonNull);
    }
}
