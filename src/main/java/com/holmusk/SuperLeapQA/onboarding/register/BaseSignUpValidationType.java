package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.EditableInput;
import com.holmusk.SuperLeapQA.model.Gender;
import com.holmusk.SuperLeapQA.model.Height;
import com.holmusk.SuperLeapQA.model.Weight;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
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
public interface BaseSignUpValidationType extends BaseActionType {
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
     * Create an age range from inclusive lower/upper bounds.
     * @param min An {@link Integer} value.
     * @param max An {@link Integer} value.
     * @return A {@link List} of {@link Integer}.
     */
    @NotNull
    default List<Integer> ageRange(int min, int max) {
        return Arrays.asList(IntStream
            .range(min, max + 1)
            .boxed()
            .toArray(Integer[]::new));
    }

    /**
     * Get the acceptable age range for the current sign up mode.
     * @return A {@link List} of {@link Integer}.
     * @see #ageRange(int, int)
     */
    @NotNull
    default List<Integer> acceptableAgeRange() {
        int minAge = minAcceptableAge();
        int maxAge = maxAcceptableAge();
        return ageRange(minAge, maxAge);
    }

    /**
     * Get an age range with lower/upper bounds that are lower/higher than
     * the min/max age by an offset value.
     * @param offset An {@link Integer} value.
     * @return A {@link List} of {@link Integer}.
     * @see #ageRange(int, int)
     */
    @NotNull
    default List<Integer> ageOffsetFromAcceptableRange(int offset) {
        int minAge = minAcceptableAge() - offset;
        int maxAge = maxAcceptableAge() + offset;
        return ageRange(minAge, maxAge);
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

    //region Outside Acceptable Age Range
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
     * Validate the screen after the DoB picker whereby the user is notified
     * that he/she/the child is not qualified for the program due to age
     * restrictions.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     * @see #rxEditFieldForInput(EditableInput)
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
                rxEditFieldForInput(EditableInput.NAME),
                rxEditFieldForInput(EditableInput.PHONE),
                rxEditFieldForInput(EditableInput.EMAIL)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
    //endregion

    //region Within Acceptable Age Range
    /**
     * Get the {@link WebElement} that corresponds to a {@link Gender}.
     * @param gender A {@link Gender} instance.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxGenderPicker(@NotNull Gender gender) {
        return engine().rxElementContainingText(gender.localizable());
    }

    /**
     * Get the editable {@link WebElement} that corresponds to a
     * {@link EditableInput}.
     * @param input A {@link EditableInput} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingID(String)
     */
    @NotNull
    default Flowable<WebElement> rxEditFieldForInput(@NotNull EditableInput input) {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID(input.androidViewId());
        } else {
            return RxUtil.error(PLATFORM_UNAVAILABLE);
        }
    }

    /**
     * Get the {@link WebElement} that corresponds to a {@link Height} mode.
     * @param mode A {@link Height} instance.
     * @return A {@link Flowable} instance.
     * * @see BaseEngine#rxElementContainingID(String)
     */
    @NotNull
    default Flowable<WebElement> rxHeightModePicker(@NotNull Height mode) {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID(mode.androidViewId());
        } else {
            return RxUtil.error(PLATFORM_UNAVAILABLE);
        }
    }

    /**
     * Get the {@link WebElement} that corresponds to a {@link Weight} mode.
     * @param mode A {@link Weight} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingID(String)
     */
    @NotNull
    default Flowable<WebElement> rxWeightModePicker(@NotNull Weight mode) {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID(mode.androidViewId());
        } else {
            return RxUtil.error(PLATFORM_UNAVAILABLE);
        }
    }

    /**
     * Get the next confirm button.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<WebElement> rxRegisterConfirmButton() {
        return engine().rxElementContainingText("register_title_next");
    }

    /**
     * Get the back button's title label.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<WebElement> rxParentAcceptableAgeInputTitleLabel() {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            String title = "parentSignUp_title_enterChildDetails";
            return engine.rxElementContainingText(title);
        } else {
            return Flowable.empty();
        }
    }

    /**
     * Validate the screen after the DoB picker whereby the user qualifies
     * for the program.
     * @return A {@link Flowable} instance.
     * @see #rxGenderPicker(Gender)
     * @see #rxEditFieldForInput(EditableInput)
     * @see #rxHeightModePicker(Height)
     * @see #rxWeightModePicker(Weight)
     * @see #rxRegisterConfirmButton()
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateAcceptableAgeScreen() {
        return Flowable
            .concatArray(
                rxGenderPicker(Gender.MALE),
                rxGenderPicker(Gender.FEMALE),
                rxEditFieldForInput(EditableInput.HEIGHT),
                rxHeightModePicker(Height.FT),
                rxHeightModePicker(Height.CM),
                rxEditFieldForInput(EditableInput.WEIGHT),
                rxWeightModePicker(Weight.LBS),
                rxWeightModePicker(Weight.KG),
                rxEditFieldForInput(EditableInput.ETHNICITY),
                rxEditFieldForInput(EditableInput.COACH_PREFERENCE),
                rxRegisterConfirmButton(),
                rxParentAcceptableAgeInputTitleLabel()
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }
    //endregion
}
