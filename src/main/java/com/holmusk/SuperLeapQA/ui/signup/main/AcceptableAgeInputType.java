package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.Zip;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.Pair;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatComparisonType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeType;
import org.swiften.xtestkit.base.element.locator.general.param.TextParam;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.base.element.action.input.type.NumericInputType;
import org.swiften.xtestkit.mobile.android.element.action.input.type.AndroidInputType;

import java.util.List;

/**
 * Created by haipham on 17/5/17.
 */
public interface AcceptableAgeInputType extends
    AcceptableInputValidationType, DOBPickerActionType
{
    //region Bridged Navigation
    /**
     * Bridge method that helps navigate from splash screen to personal info
     * input.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_acceptableAge(UserMode)
     * @see #rx_acceptableAge_personalInfo()
     */
    @NotNull
    default Flowable<Boolean> rx_splash_personalInfo(@NotNull UserMode mode) {
        final AcceptableAgeInputType THIS = this;
        return rx_splash_acceptableAge(mode).flatMap(a -> THIS.rx_acceptableAge_personalInfo());
    }
    //endregion

    /**
     * Navigate from the acceptable age input to the personal info input
     * screen.
     * @return A {@link Flowable} instance.
     * @see #rxEnterAcceptableAgeInputs()
     * @see #rxConfirmAcceptableAgeInputs()
     */
    @NotNull
    default Flowable<Boolean> rx_acceptableAge_personalInfo() {
        final AcceptableAgeInputType THIS = this;
        return rxEnterAcceptableAgeInputs().flatMap(a -> THIS.rxConfirmAcceptableAgeInputs());
    }

    /**
     * Select a value, assuming the user is in the value selection screen.
     * @param MODE A {@link NumericInputType} instance.
     * @param NUMERIC_VALUE A {@link Double} value.
     * @return A {@link Flowable} instance.
     * @see #rxPickerItemViews(SLNumericInputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxSelectNumericInput(
        @NotNull final SLNumericInputType MODE,
        final double NUMERIC_VALUE
    ) {
        final AcceptableAgeInputType THIS = this;
        final BaseEngine<?> ENGINE = engine();
        final String HEIGHT_STR = MODE.stringValue(NUMERIC_VALUE);

        final TextParam TEXT_PARAM = TextParam.builder()
            .withText(HEIGHT_STR)
            .withRetries(0)
            .build();

        SwipeRepeatType repeater = new SwipeRepeatComparisonType() {
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
                return THIS.rxPickerItemViews(MODE);
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
                    .flatMap(ENGINE::rxClick)
                    .map(BooleanUtil::toTrue);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rxScrollableViewToSwipe() {
                return rxScrollableChoicePicker(MODE);
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
     * Select values for a set {@link SLNumericInputType}. This is
     * useful when we want to select {@link Height} or {@link Weight} based
     * on different units of measurement (metric/imperial), since the app
     * requires a combination of two inputs from two
     * {@link SLNumericInputType} (e.g. {@link Height#CHILD_CM} and
     * {@link Height#CHILD_CM_DEC}).
     * @param inputs A {@link List} of {@link Pair} instances.
     * @return A {@link Flowable} instance.
     * @see #rxSelectNumericInput(SLNumericInputType, double)
     */
    @NotNull
    default Flowable<Boolean> rxSelectNumericInput(
        @NotNull List<Pair<SLNumericInputType,Double>> inputs
    ) {
        final AcceptableAgeInputType THIS = this;

        return Flowable
            .fromIterable(inputs)
            .concatMap(a -> {
                if (ObjectUtil.nonNull(a.A) && ObjectUtil.nonNull(a.B)) {
                    return THIS.rxSelectNumericInput(a.A, a.B);
                } else {
                    return RxUtil.error(NOT_IMPLEMENTED);
                }
            })
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Select an {@link Ethnicity} for {@link ChoiceInput#ETHNICITY}.
     * @param E An {@link Ethnicity} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     * @see #rxClickInputField(AndroidInputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxSelectEthnicity(@NotNull final Ethnicity E) {
        final BaseEngine<?> ENGINE = engine();

        return rxClickInputField(ChoiceInput.ETHNICITY)
            .flatMap(a -> ENGINE.rxElementContainingText(E.value()))
            .flatMap(ENGINE::rxClick)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Select a {@link CoachPref} for {@link ChoiceInput#COACH_PREFERENCE}.
     * @param CP A {@link CoachPref} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     * @see #rxClickInputField(AndroidInputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxSelectCoachPref(@NotNull final CoachPref CP) {
        final BaseEngine<?> ENGINE = engine();

        return rxClickInputField(ChoiceInput.COACH_PREFERENCE)
            .flatMap(a -> ENGINE.rxElementContainingText(CP.value()))
            .flatMap(ENGINE::rxClick)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Confirm the acceptable age inputs by clicking the next button, assuming
     * the user is already in the acceptable age input screen.
     * @return A {@link Flowable} instance.
     * @see #rxAcceptableAgeConfirmButton()
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmAcceptableAgeInputs() {
        return rxAcceptableAgeConfirmButton().flatMap(engine()::rxClick).map(a -> true);
    }

    /**
     * Enter random acceptable age inputs in order to access the personal
     * information input screen.
     * @return A {@link Flowable} instance.
     * @see #rxClickInputField(AndroidInputType)
     * @see #rxSelectEthnicity(Ethnicity)
     * @see #rxSelectCoachPref(CoachPref)
     * @see #rxSelectNumericInput(SLNumericInputType, double)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAcceptableAgeInputs() {
        final AcceptableAgeInputType THIS = this;
        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());
//        final Height HEIGHT_MODE = CollectionTestUtil.randomElement(Height.values());
//        final Weight WEIGHT_MODE = CollectionTestUtil.randomElement(Weight.values());
        final Height HEIGHT_MODE = Height.CHILD_CM; // TODO: Randomize when ft bug is fixed
        final Weight WEIGHT_MODE = Weight.CHILD_KG; // TODO: Randomize when lb bug is fixed
        final double HEIGHT = HEIGHT_MODE.randomNumericValue();
        final double WEIGHT = WEIGHT_MODE.randomNumericValue();

        return rxClickInputField(GENDER)
            .flatMap(a -> rxSelectEthnicity(ETH))
            .flatMap(a -> rxSelectCoachPref(CP))

            .flatMap(a -> rxClickInputField(HEIGHT_MODE))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(HEIGHT_MODE, HEIGHT))

            .flatMap(a -> rxClickInputField(WEIGHT_MODE))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(WEIGHT_MODE, WEIGHT));
    }

    /**
     * Enter random inputs for acceptable age screen, assuming the user is
     * already in the acceptable age input screen.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxClickInputField(AndroidInputType)
     * @see #rxSelectNumericInput(List)
     * @see #rxEditFieldHasValue(AndroidInputType, String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAndValidateAcceptableAgeInputs(@NotNull UserMode mode) {
        final AcceptableAgeInputType THIS = this;
        final List<Pair<SLNumericInputType,Double>> HEIGHT_M = Height.randomMetric(mode);
        final List<Pair<SLNumericInputType,Double>> WEIGHT_M = Weight.randomMetric(mode);
        final List<Pair<SLNumericInputType,Double>> HEIGHT_I = Height.randomImperial(mode);
        final List<Pair<SLNumericInputType,Double>> WEIGHT_I = Weight.randomImperial(mode);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        return rxClickInputField(Gender.MALE)
            .flatMap(a -> THIS.rxClickInputField(Gender.FEMALE))
            .flatMap(a -> THIS.rxSelectEthnicity(ETH))
            .flatMap(a -> THIS.rxSelectCoachPref(CP))

            .flatMap(a -> THIS.rxClickInputField(Height.CHILD_CM))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(HEIGHT_M))

            .flatMap(a -> THIS.rxClickInputField(Height.CHILD_FT))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(HEIGHT_I))

            .flatMap(a -> THIS.rxClickInputField(Weight.CHILD_KG))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(WEIGHT_M))

            .flatMap(a -> THIS.rxClickInputField(Weight.CHILD_LB))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(WEIGHT_I));
    }

    /**
     * Sequentially validate error messages due to empty inputs (refer to
     * {@link TextInput} and {@link ChoiceInput}, assuming the user is
     * already in the acceptable age input screen.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rxConfirmAcceptableAgeInputs()
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxValidateAcceptableAgeEmptyInputErrors(@NotNull final UserMode MODE) {
        final AcceptableAgeInputType THIS = this;
        final BaseEngine<?> ENGINE = engine();

        /* If we are testing on iOS, there is not need to check for empty
         * error messages, since the confirm button is not enabled until
         * all inputs are filled */
        if (ENGINE.platform().equals(Platform.IOS)) {
            return Flowable.just(true);
        }

        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final double HEIGHT_CM = Height.CHILD_CM.randomNumericValue();
        final double HEIGHT_FT = Height.CHILD_FT.randomNumericValue();
        final double WEIGHT_KG = Weight.CHILD_KG.randomNumericValue();
        final double WEIGHT_LB = Weight.CHILD_LB.randomNumericValue();
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        /* At this stage, the gender error message should be shown */
        return rxConfirmAcceptableAgeInputs()
            .flatMap(a -> THIS.rxIsShowingError(GENDER.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxClickInputField(GENDER))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Height.CHILD_CM))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Height.CHILD_CM.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.CHILD_CM, HEIGHT_CM))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Height.CHILD_FT))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Height.CHILD_FT.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.CHILD_FT, HEIGHT_FT))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Weight.CHILD_KG))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Weight.CHILD_KG.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.CHILD_KG, WEIGHT_KG))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Weight.CHILD_LB))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Weight.CHILD_LB.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.CHILD_LB, WEIGHT_LB))

            /* At this stage, the ethnicity error message should be shown */
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(ChoiceInput.ETHNICITY.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectEthnicity(ETH))

            /* At this stage, the coach pref error message should be shown */
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(ChoiceInput.COACH_PREFERENCE.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectCoachPref(CP));
    }
}
