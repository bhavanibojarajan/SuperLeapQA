package com.holmusk.SuperLeapQA.onboarding.register;

import com.holmusk.SuperLeapQA.base.BaseActionType;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.type.NumericSelectableInputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.number.NumberTestUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.date.type.DateType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatComparisonType;
import org.swiften.xtestkit.base.param.TextParam;
import org.swiften.xtestkit.base.type.PlatformErrorType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 5/8/17.
 */
public interface BaseSignUpActionType extends
    BaseActionType,
    BaseSignUpValidationType,
    PlatformErrorType
{
    /**
     * Navigate to the acceptable age input screen by selecting a DoB that
     * results in an age that lies within {@link #acceptableAgeRange()}.
     * @return A {@link Flowable} instance.
     * @see #rxSelectDoBToBeOfAge(int)
     * @see #rxConfirmDoB()
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_acceptableAgeInput() {
        List<Integer> range = acceptableAgeRange();
        final int AGE = CollectionTestUtil.randomElement(range);

        return rxOpenDoBPicker()
            .flatMap(a -> rxSelectDoBToBeOfAge(AGE))
            .flatMap(a -> rxConfirmDoB());
    }

    /**
     * Open the DoB dialog in the parent sign up screen. This can be used both
     * for parent sign up and teen sign up.
     * @return A {@link Flowable} instance.
     * @see #rxDoBEditField()
     */
    @NotNull
    default Flowable<Boolean> rxOpenDoBPicker() {
        final BaseEngine<?> ENGINE = engine();

        return rxDoBEditField()
            .flatMap(ENGINE::rxClick)
            .delay(generalDelay(), TimeUnit.MILLISECONDS)
            .flatMap(a -> ENGINE.rxImplicitlyWait(this::generalDelay));
    }

    /**
     * Confirm the DoB by clicking OK, assuming the user is already in the
     * DoB dialog. On Android, this action will bring the user directly to
     * the next screen, so if we want to check whether the date was properly
     * stored in the DoB text field, we need to navigate back once.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmDoB() {
        final BaseEngine<?> ENGINE = engine();
        PlatformType platform = ENGINE.platform();

        if (platform.equals(Platform.ANDROID)) {
            return ENGINE.rxElementContainingText("ok").flatMap(ENGINE::rxClick);
        } else {
            return RxUtil.error(PLATFORM_UNAVAILABLE);
        }
    }

    /**
     * Select a DoB without confirming, assuming the user is already in the
     * DoB picker screen.
     * @param DATE A {@link Date} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxSelectDate(DateType)
     */
    @NotNull
    default Flowable<Boolean> rxSelectDoB(@NotNull final Date DATE) {
        return engine().rxSelectDate(() -> DATE);
    }

    /**
     * Select a DoB so that the user is of a certain age.
     * @param age An {@link Integer} value.
     * @return A {@link Flowable} instance.
     * @see #rxSelectDoB(Date)
     */
    @NotNull
    default Flowable<Boolean> rxSelectDoBToBeOfAge(int age) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -age);

        /* We need to add 1 to the day field to avoid the birthday. E.g.
         * if the current date is 10/05/2017 and we want the user to be 4
         * years-old, we need to select 11/05/2013 - any lower than that then
         * the user would be 5+ years-old */
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return rxSelectDoB(calendar.getTime());
    }

    /**
     * Sequentially select DoBs and validate that DoBs that fall out of
     * {@link #acceptableAgeRange()} should not bring the user to the correct
     * sign up screen. This action assumes the user is in the DoB selection
     * screen, but has not opened the DoB picker yet.
     * @param AGES A {@link List} of {@link Integer}.
     * @return A {@link Flowable} instance.
     * @see #acceptableAgeRange()
     * @see #rxOpenDoBPicker()
     * @see #rxValidateAcceptableAgeScreen()
     * @see #rxValidateUnacceptableAgeScreen()
     * @see #rxNavigateBackWithBackButton()
     */
    @NotNull
    default Flowable<Boolean> rxValidateDoBs(@NotNull final List<Integer> AGES) {
        final List<Integer> RANGE = acceptableAgeRange();
        final int LENGTH = AGES.size();

        class IterateDoBs {
            @NotNull
            @SuppressWarnings("WeakerAccess")
            Flowable<Boolean> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    final int AGE = AGES.get(INDEX);
                    final boolean VALID = RANGE.contains(AGE);

                    return rxOpenDoBPicker()
                        .flatMap(a -> rxSelectDoBToBeOfAge(AGE))
                        .flatMap(a -> rxConfirmDoB())
                        .flatMap(a -> {
                            if (VALID) {
                                return rxValidateAcceptableAgeScreen();
                            } else {
                                return rxValidateUnacceptableAgeScreen();
                            }
                        })
                        .flatMap(a -> rxNavigateBackWithBackButton())
                        .flatMap(a -> new IterateDoBs().repeat(INDEX + 1));
                } else {
                    return Flowable.empty();
                }
            }
        }

        return new IterateDoBs().repeat(0);
    }

    /**
     * Select a random DoB, assuming the user is already in the DoB selection
     * screen.
     * @return A {@link Flowable} instance.
     * @see #rxSelectDoB(Date)
     * @see #rxConfirmDoB()
     * @see #rxNavigateBackWithBackButton()
     * @see #rxDoBEditFieldHasDate(Date)
     */
    @NotNull
    default Flowable<Boolean> rxSelectRandomDoB() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberTestUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberTestUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberTestUtil.randomBetween(2010, 2030));
        final Date DATE = calendar.getTime();

        // When
        return rxOpenDoBPicker()
            .flatMap(a -> rxSelectDoB(DATE))
            .flatMap(a -> rxConfirmDoB())
            .flatMap(a -> rxNavigateBackWithBackButton())
            .flatMap(a -> rxDoBEditFieldHasDate(DATE));
    }

    /**
     * Select a {@link Gender}.
     * @param gender A {@link Gender} instance.
     * @return A {@link Flowable} instance.
     * @see #rxGenderPicker(Gender)
     */
    @NotNull
    default Flowable<Boolean> rxSelectGender(@NotNull Gender gender) {
        return rxGenderPicker(gender).flatMap(engine()::rxClick);
    }

    /**
     * Select a {@link Height} mode.
     * @param mode A {@link Height} instance.
     * @return A {@link Flowable} instance.
     * @see #rxHeightModePicker(Height)
     */
    @NotNull
    default Flowable<Boolean> rxSelectHeightMode(@NotNull Height mode) {
        return rxHeightModePicker(mode).flatMap(engine()::rxClick);
    }

    /**
     * Select a value, assuming the user is in the value selection screen.
     * @param MODE A {@link NumericSelectableInputType} instance.
     * @param NUMERIC_VALUE A {@link Double} value.
     * @return A {@link Flowable} instance.
     * @see #rxPickerItemViews(TextInput)
     */
    @NotNull
    default Flowable<Boolean> rxSelectNumericInput(
        @NotNull final NumericSelectableInputType MODE,
        final double NUMERIC_VALUE)
    {
        final BaseSignUpActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();
        final String HEIGHT_STR = MODE.stringValue(NUMERIC_VALUE);

        final TextParam TEXT_PARAM = TextParam.builder()
            .withText(HEIGHT_STR)
            .withRetries(0)
            .build();

        SwipeRepeatComparisonType repeater = new SwipeRepeatComparisonType() {
            @NotNull
            @Override
            public Flowable<Integer> rxInitialDifference(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(MODE::numericValue)
                    .map(a -> a - NUMERIC_VALUE)
                    .map(Double::intValue);
            }

            @NotNull
            @Override
            public Flowable<?> rxCompareFirst(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(MODE::numericValue)
                    .filter(a -> a > NUMERIC_VALUE);
            }

            @NotNull
            @Override
            public Flowable<?> rxCompareLast(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(MODE::numericValue)
                    .filter(a -> a < NUMERIC_VALUE);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rxScrollViewChildItems() {
                return THIS.rxPickerItemViews(TextInput.HEIGHT);
            }

            @Override
            public double elementSwipeRatio() {
                return 0.7d;
            }

            @NotNull
            @Override
            public Flowable<Boolean> rxShouldKeepSwiping() {
                return ENGINE
                    .rxElementWithText(TEXT_PARAM)
                    .filter(a -> ENGINE.getText(a).equals(HEIGHT_STR))
                    .flatMap(ENGINE::rxClick);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rxScrollableViewToSwipe() {
                return rxScrollableInputPickerView(TextInput.HEIGHT);
            }

            @NotNull
            @Override
            public Flowable<Boolean> rxSwipeOnce(@NotNull SwipeType param) {
                return ENGINE.rxSwipeOnce(param);
            }
        };

        return repeater.rxRepeatSwipe();
    }

    /**
     * Select a {@link Weight} mode.
     * @param mode A {@link Weight} mode.
     * @return A {@link Flowable} instance.
     * @see #rxWeightModePicker(Weight)
     */
    @NotNull
    default Flowable<Boolean> rxSelectWeightMode(@NotNull Weight mode) {
        return rxWeightModePicker(mode).flatMap(engine()::rxClick);
    }

    /**
     * Select an {@link Ethnicity} for {@link TextInput#ETHNICITY}.
     * @param E An {@link Ethnicity} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     * @see #rxOpenPickerWindow(TextInput)
     */
    @NotNull
    default Flowable<Boolean> rxSelectEthnicity(@NotNull final Ethnicity E) {
        final BaseEngine<?> ENGINE = engine();

        return rxOpenPickerWindow(TextInput.ETHNICITY)
            .flatMap(a -> ENGINE.rxElementContainingText(E.value()))
            .flatMap(ENGINE::rxClick);
    }

    /**
     * Select a {@link CoachPref} for {@link TextInput#COACH_PREFERENCE}.
     * @param CP A {@link CoachPref} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String)
     * @see #rxOpenPickerWindow(TextInput)
     */
    @NotNull
    default Flowable<Boolean> rxSelectCoachPref(@NotNull final CoachPref CP) {
        final BaseEngine<?> ENGINE = engine();

        return rxOpenPickerWindow(TextInput.COACH_PREFERENCE)
            .flatMap(a -> ENGINE.rxElementContainingText(CP.value()))
            .flatMap(ENGINE::rxClick);
    }

    /**
     * Enter random inputs for acceptable age screen, assuming the user is
     * already in the acceptable age input screen.
     * @return A {@link Flowable} instance.
     * @see #rxSelectGender(Gender)
     * @see #rxSelectHeightMode(Height)
     * @see #rxOpenPickerWindow(TextInput)
     * @see #rxEditFieldHasValue(TextInput, String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterRandomAcceptableAgeInputs() {
        final double HEIGHT_CM = Height.CM.randomSelectableNumericValue();
        final double HEIGHT_FT = Height.FT.randomSelectableNumericValue();
        final String HEIGHT_CM_STR = Height.CM.stringValue(HEIGHT_CM);
        final String HEIGHT_CM_FT_STR = Height.CM.ftString(HEIGHT_CM);
        final String HEIGHT_FT_STR = Height.FT.stringValue(HEIGHT_FT);
        final double WEIGHT_KG = Weight.KG.randomSelectableNumericValue();
        final double WEIGHT_LB = Weight.LB.randomSelectableNumericValue();
        final String WEIGHT_KG_STR = Weight.KG.stringValue(WEIGHT_KG);
        final String WEIGHT_KG_LB_STR = Weight.KG.lbString(WEIGHT_KG);
        final String WEIGHT_LB_STR = Weight.LB.stringValue(WEIGHT_LB);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        return Flowable
            .concatArray(
                rxSelectGender(Gender.MALE),
                rxSelectGender(Gender.FEMALE),
                rxSelectEthnicity(ETH),
                rxSelectCoachPref(CP),

                rxSelectHeightMode(Height.CM)
                    .flatMap(a -> rxOpenPickerWindow(TextInput.HEIGHT))
                    .flatMap(a -> rxSelectNumericInput(Height.CM, HEIGHT_CM))
                    .flatMap(a -> rxEditFieldHasValue(TextInput.HEIGHT, HEIGHT_CM_STR)),

                rxSelectHeightMode(Height.FT)
                    .flatMap(a -> rxEditFieldHasValue(TextInput.HEIGHT, HEIGHT_CM_FT_STR))
                    .flatMap(a -> rxOpenPickerWindow(TextInput.HEIGHT))
                    .flatMap(a -> rxSelectNumericInput(Height.FT, HEIGHT_FT))
                    .flatMap(a -> rxEditFieldHasValue(TextInput.HEIGHT, HEIGHT_FT_STR)),

                rxSelectWeightMode(Weight.KG)
                    .flatMap(a -> rxOpenPickerWindow(TextInput.WEIGHT))
                    .flatMap(a -> rxSelectNumericInput(Weight.KG, WEIGHT_KG))
                    .flatMap(a -> rxEditFieldHasValue(TextInput.WEIGHT, WEIGHT_KG_STR)),

                rxSelectWeightMode(Weight.LB)
                    .flatMap(a -> rxEditFieldHasValue(TextInput.WEIGHT, WEIGHT_KG_LB_STR))
                    .flatMap(a -> rxOpenPickerWindow(TextInput.WEIGHT))
                    .flatMap(a -> rxSelectNumericInput(Weight.LB, WEIGHT_LB))
                    .flatMap(a -> rxEditFieldHasValue(TextInput.WEIGHT, WEIGHT_LB_STR))
            )
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }
}
