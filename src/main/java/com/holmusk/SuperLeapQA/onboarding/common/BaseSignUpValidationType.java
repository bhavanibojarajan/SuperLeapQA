package com.holmusk.SuperLeapQA.onboarding.common;

import com.holmusk.SuperLeapQA.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.type.InputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.localizer.LocalizationFormat;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.locator.general.xpath.XPath;
import org.swiften.xtestkit.base.element.locator.general.param.ByXPath;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.mobile.android.AndroidView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by haipham on 5/8/17.
 */
public interface BaseSignUpValidationType extends BaseActionType {
    //region DoB Age Range
    /**
     * Validate the DoB picker screen.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     * @see #rxDoBEditField()
     * @see #rxDoBElements()
     */
    @NotNull
    default Flowable<Boolean> rxValidateParentDoBPickerScreen() {
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .concat(
                ENGINE.rxElementContainingText("parentSignUp_title_dateOfBirth"),
                rxDoBEditField()
            )
            .all(ObjectUtil::nonNull)
            .<Boolean>toFlowable()
            .defaultIfEmpty(true)

            /* Open the DoB dialog and verify that all elements are there */
            .flatMap(a -> rxDoBEditField())
            .flatMap(ENGINE::rxClick)
            .flatMap(a -> rxDoBElements())
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .delay(generalDelay(), TimeUnit.MILLISECONDS)

            /* Dismiss the dialog by navigating back once */
            .flatMap(a -> ENGINE.rxNavigateBackOnce())
            .delay(generalDelay(), TimeUnit.MILLISECONDS)
            .map(BooleanUtil::toTrue);
    }

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
     * @param mode A {@link UserMode} instance.
     * @return A {@link List} of {@link Integer}.
     * @see #ageRange(int, int)
     * @see UserMode#minAcceptableAge()
     * @see UserMode#maxAcceptableAge()
     */
    @NotNull
    default List<Integer> acceptableAgeRange(@NotNull UserMode mode) {
        int minAge = mode.minAcceptableAge();
        int maxAge = mode.maxAcceptableAge();
        return ageRange(minAge, maxAge);
    }

    /**
     * Get an age range with lower/upper bounds that are lower/higher than
     * the min/max age by an offset value.
     * @param mode A {@link UserMode} instance.
     * @param offset An {@link Integer} value.
     * @return A {@link List} of {@link Integer}.
     * @see #ageRange(int, int)
     * @see UserMode#minAcceptableAge()
     * @see UserMode#maxAcceptableAge()
     */
    @NotNull
    default List<Integer> ageOffsetFromAcceptableRange(@NotNull UserMode mode, int offset) {
        int minAge = mode.minAcceptableAge() - offset;
        int maxAge = mode.maxAcceptableAge() + offset;
        return ageRange(minAge, maxAge);
    }

    /**
     * Get the {@link String} representation of the acceptable age range.
     * @param mode A {@link UserMode} instance.
     * @return A {@link String} value.
     * @see #acceptableAgeRange(UserMode)
     * @see UserMode#minAcceptableAge()
     * @see UserMode#maxAcceptableAge()
     */
    @NotNull
    default String acceptableAgeRangeString(@NotNull UserMode mode) {
        int minAge = mode.minAcceptableAge();
        int maxAge = mode.maxAcceptableAge();
        return String.format("%1$d-%2$d", minAge, maxAge);
    }
    //endregion

    //region DoB Picker
    /**
     * Get the DoB's editable text field.
     * @return A {@link Flowable} instance.
     */
    @NotNull
    default Flowable<WebElement> rxDoBEditField() {
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
    default Flowable<Boolean> rxDoBEditFieldHasDate(@NotNull Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String string = formatter.format(date);
        return engine().rxElementContainingText(string).map(ObjectUtil::nonNull);
    }
    //endregion

    //region Outside Acceptable Age Range
    /**
     * Get all calendar {@link WebElement} instances.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxAllCalendarElements()
     */
    @NotNull
    default Flowable<WebElement> rxDoBElements() {
        return  engine().rxAllCalendarElements();
    }

    /**
     * Validate the screen after the DoB picker whereby the user is notified
     * that he/she/the child is not qualified for the program due to age
     * restrictions.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     * @see #rxEditFieldForInput(InputType)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateUnacceptableAgeScreen(@NotNull UserMode mode) {
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .concatArray(
                ENGINE.rxElementContainingText("register_title_weAreOnlyAccepting"),
                ENGINE.rxElementContainingText(acceptableAgeRangeString(mode)),
                ENGINE.rxElementContainingText("+65"),
                rxEditFieldForInput(TextInput.NAME),
                rxEditFieldForInput(TextInput.PHONE),
                rxEditFieldForInput(TextInput.EMAIL)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Get the confirm button for the unacceptable age inputs.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<WebElement> rxUnacceptableAgeSubmitButton() {
        return engine().rxElementContainingText("register_title_submit");
    }

    /**
     * Get the continue button after the unacceptable age input is confirmed.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<WebElement> rxUnacceptableAgeInputOkButton() {
        return engine().rxElementContainingText("register_title_ok");
    }

    /**
     * Validate the confirmation screen after unacceptable age input is
     * submitted.
     * @return A {@link Flowable} instance.
     * @see #rxUnacceptableAgeInputOkButton()
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<Boolean> rxValidateUnacceptableAgeInputConfirmation() {
        final BaseEngine<?> ENGINE = engine();

        return Flowable
            .concat(
                rxUnacceptableAgeInputOkButton(),
                ENGINE.rxElementContainingText("register_title_thanksForInterest"),
                ENGINE.rxElementContainingText("register_title_notifiedWhenLaunched")
            )
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }
    //endregion

    //region Within Acceptable Age Range
    /**
     * Get the editable {@link WebElement} that corresponds to a
     * {@link InputType}.
     * @param input A {@link InputType} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingID(String)
     */
    @NotNull
    default Flowable<WebElement> rxEditFieldForInput(@NotNull InputType input) {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID(input.androidViewId());
        } else {
            return RxUtil.error();
        }
    }

    /**
     * Get the next confirm button for acceptable age input screen.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<WebElement> rxAcceptableAgeConfirmButton() {
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
     * Get the scrollable height selector view, assuming the user is already
     * in the height picker window.
     * @param input A {@link InputType} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingID(String)
     */
    @NotNull
    default Flowable<WebElement> rxScrollableInputPickerView(@NotNull InputType input) {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID("select_dialog_listview");
        } else {
            return RxUtil.error();
        }
    }

    /**
     * Get all input value items within the scrollable view as emitted by
     * {@link #rxScrollableInputPickerView(InputType)}, assuming the user
     * is already in the picker window.
     * @param input A {@link InputType} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementsByXPath(ByXPath)
     */
    @NotNull
    default Flowable<WebElement> rxPickerItemViews(@NotNull InputType input) {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            XPath xPath = engine.newXPathBuilder()
                .ofClass(AndroidView.ViewType.TEXT_VIEW.className())
                .containsID("text1")
                .build();

            ByXPath byXPath = ByXPath.builder()
                .withXPath(xPath)
                .withError(NO_SUCH_ELEMENT)
                .build();

            return engine.rxElementsByXPath(byXPath);
        } else {
            return RxUtil.error();
        }
    }

    /**
     * Validate the screen after the DoB picker whereby the user qualifies
     * for the program.
     * @return A {@link Flowable} instance.
     * @see #rxEditFieldForInput(InputType)
     * @see #rxAcceptableAgeConfirmButton()
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateAcceptableAgeScreen() {
        return Flowable
            .concatArray(
                rxEditFieldForInput(Gender.MALE),
                rxEditFieldForInput(Gender.FEMALE),
                rxEditFieldForInput(ChoiceInput.HEIGHT),
                rxEditFieldForInput(Height.FT),
                rxEditFieldForInput(Height.CM),
                rxEditFieldForInput(ChoiceInput.WEIGHT),
                rxEditFieldForInput(Weight.LB),
                rxEditFieldForInput(Weight.KG),
                rxEditFieldForInput(ChoiceInput.ETHNICITY),
                rxEditFieldForInput(ChoiceInput.COACH_PREFERENCE),
                rxAcceptableAgeConfirmButton(),
                rxParentAcceptableAgeInputTitleLabel()
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Check if an editable field has an input.
     * @param input A {@link InputType} instance.
     * @param VALUE A {@link String} value.
     * @return A {@link Flowable} instance.
     * @see #rxEditFieldForInput(InputType)
     */
    @NotNull
    default Flowable<Boolean> rxEditFieldHasValue(@NotNull InputType input,
                                                  @NotNull final String VALUE) {
        return rxEditFieldForInput(input)
            .map(engine()::getText)
            .doOnNext(LogUtil::println)
            .filter(a -> a.equals(VALUE))
            .switchIfEmpty(RxUtil.error("Value does not equal " + VALUE))
            .map(BooleanUtil::toTrue);
    }

    /**
     * Get the view that pops up when an error is notified to the user. This
     * only works in specific cases however, so use with care.
     * @param error A {@link LocalizationFormat} value.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<WebElement> rxErrorPopup(@NotNull LocalizationFormat error) {
        return engine().rxElementContainingText(error);
    }

    /**
     * Check whether an error is being shown to the user. This only works in
     * specific cases however, so use with care.
     * @param error A {@link LocalizationFormat} value.
     * @return A {@link Flowable} instance.
     * @see #rxErrorPopup(LocalizationFormat)
     */
    @NotNull
    default Flowable<Boolean> rxIsShowingError(@NotNull LocalizationFormat error) {
        return rxErrorPopup(error).map(a -> true);
    }
    //endregion

    //region Personal Information
    /**
     * Get the submit button for the personal info screen.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<WebElement> rxPersonalInfoSubmitButton() {
        return engine().rxElementContainingText("register_title_submit");
    }

    /**
     * Get the Terms and Conditions checkbox.
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#platform()
     * @see BaseEngine#rxElementContainingID(String)
     */
    @NotNull
    default Flowable<WebElement> rxTOCCheckBox() {
        BaseEngine<?> engine = engine();
        PlatformType platform = engine.platform();

        if (platform.equals(Platform.ANDROID)) {
            return engine.rxElementContainingID("ctv_toc");
        } else {
            return RxUtil.error();
        }
    }
    //endregion
}
