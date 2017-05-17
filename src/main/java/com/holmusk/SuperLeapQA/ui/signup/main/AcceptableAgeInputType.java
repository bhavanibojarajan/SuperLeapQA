package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatComparisonType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeType;
import org.swiften.xtestkit.base.element.locator.general.param.TextParam;
import org.swiften.xtestkit.mobile.Platform;

import java.util.List;

/**
 * Created by haipham on 17/5/17.
 */
public interface AcceptableAgeInputType extends
    AcceptableInputValidationType,
    DOBPickerActionType
{
    //region Bridged Navigation
    /**
     * Bridge method that helps navigate from splash screen to acceptable
     * age input.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_DoBPicker(UserMode)
     * @see #rx_DoBPicker_acceptableAge(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_acceptableAge(@NotNull UserMode mode) {
        final AcceptableAgeInputType THIS = this;
        return rx_splash_DoBPicker(mode).flatMap(a -> THIS.rx_DoBPicker_acceptableAge(mode));
    }
    //endregion

    /**
     * Navigate to the acceptable age input screen by selecting a DoB that
     * results in an age that lies within {@link UserMode#acceptableAgeRange()}.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_DoBPicker_inputScreenForAge(int)
     */
    @NotNull
    default Flowable<Boolean> rx_DoBPicker_acceptableAge(@NotNull UserMode mode) {
        List<Integer> range = mode.acceptableAgeRange();
        int age = CollectionTestUtil.randomElement(range);
        return rx_DoBPicker_inputScreenForAge(age);
    }

    /**
     * Select a value, assuming the user is in the value selection screen.
     * @param MODE A {@link NumericSelectableInputType} instance.
     * @param NUMERIC_VALUE A {@link Double} value.
     * @return A {@link Flowable} instance.
     * @see #rxPickerItemViews(InputType)
     * @see BaseEngine#rxClick(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rxSelectNumericInput(
        @NotNull final NumericSelectableInputType MODE,
        final double NUMERIC_VALUE)
    {
        final AcceptableAgeInputType THIS = this;
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
                return THIS.rxPickerItemViews(ChoiceInput.HEIGHT);
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
                return rxScrollableInputPickerView(ChoiceInput.HEIGHT);
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
     * Select an {@link Ethnicity} for {@link ChoiceInput#ETHNICITY}.
     * @param E An {@link Ethnicity} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     * @see #rxClickInputField(InputType)
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
     * @see #rxClickInputField(InputType)
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
     * @see #rxClickInputField(InputType)
     * @see #rxSelectEthnicity(Ethnicity)
     * @see #rxSelectCoachPref(CoachPref)
     * @see #rxSelectNumericInput(NumericSelectableInputType, double)
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
        final Height HEIGHT_MODE = Height.CM; // TODO: Randomize when ft bug is fixed
        final Weight WEIGHT_MODE = Weight.KG; // TODO: Randomize when lb bug is fixed
        final double HEIGHT = HEIGHT_MODE.randomSelectableNumericValue();
        final double WEIGHT = WEIGHT_MODE.randomSelectableNumericValue();

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
     * @return A {@link Flowable} instance.
     * @see #rxClickInputField(InputType)
     * @see #rxEditFieldHasValue(InputType, String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAndValidateAcceptableAgeInputs() {
        final AcceptableAgeInputType THIS = this;
        final double HEIGHT_CM = Height.CM.randomSelectableNumericValue();
        final double HEIGHT_FT = Height.FT.randomSelectableNumericValue();
        final String HEIGHT_CM_STR = Height.CM.stringValue(HEIGHT_CM);
        final String HEIGHT_CM_FT_STR = Height.CM.ftString(HEIGHT_CM);
        final String HEIGHT_FT_STR = Height.FT.stringValue(HEIGHT_FT);
        final String HEIGHT_FT_CM_STR = Height.FT.cmString(HEIGHT_FT);
        final double WEIGHT_KG = Weight.KG.randomSelectableNumericValue();
        final double WEIGHT_LB = Weight.LB.randomSelectableNumericValue();
        final String WEIGHT_KG_STR = Weight.KG.stringValue(WEIGHT_KG);
        final String WEIGHT_KG_LB_STR = Weight.KG.lbString(WEIGHT_KG);
        final String WEIGHT_LB_STR = Weight.LB.stringValue(WEIGHT_LB);
        final String WEIGHT_LB_KG_STR = Weight.LB.kgString(WEIGHT_LB);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        return rxClickInputField(Gender.MALE)
            .flatMap(a -> THIS.rxClickInputField(Gender.FEMALE))
            .flatMap(a -> THIS.rxSelectEthnicity(ETH))
            .flatMap(a -> THIS.rxSelectCoachPref(CP))

            .flatMap(a -> THIS.rxClickInputField(Height.CM))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.CM, HEIGHT_CM))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_CM_STR))
            .flatMap(a -> THIS.rxClickInputField(Height.FT))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_CM_FT_STR))

            .flatMap(a -> THIS.rxClickInputField(Height.FT))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.FT, HEIGHT_FT))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_FT_STR))
            .flatMap(a -> THIS.rxClickInputField(Height.CM))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.HEIGHT, HEIGHT_FT_CM_STR))

            .flatMap(a -> THIS.rxClickInputField(Weight.KG))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.KG, WEIGHT_KG))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_KG_STR))
            .flatMap(a -> THIS.rxClickInputField(Weight.LB))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_KG_LB_STR))

            .flatMap(a -> THIS.rxClickInputField(Weight.LB))
            .flatMap(a -> THIS.rxClickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.LB, WEIGHT_LB))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_LB_STR))
            .flatMap(a -> THIS.rxClickInputField(Weight.KG))
            .flatMap(a -> THIS.rxEditFieldHasValue(ChoiceInput.WEIGHT, WEIGHT_LB_KG_STR));
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
        final double HEIGHT_CM = Height.CM.randomSelectableNumericValue();
        final double HEIGHT_FT = Height.FT.randomSelectableNumericValue();
        final double WEIGHT_KG = Weight.KG.randomSelectableNumericValue();
        final double WEIGHT_LB = Weight.LB.randomSelectableNumericValue();
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        /* At this stage, the gender error message should be shown */
        return rxConfirmAcceptableAgeInputs()
            .flatMap(a -> THIS.rxIsShowingError(GENDER.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxClickInputField(GENDER))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Height.CM))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Height.CM.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.CM, HEIGHT_CM))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Height.FT))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Height.FT.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Height.FT, HEIGHT_FT))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Weight.KG))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Weight.KG.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.KG, WEIGHT_KG))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rxClickInputField(Weight.LB))
            .flatMap(a -> THIS.rxConfirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Weight.LB.emptyInputError(MODE)))
            .flatMap(a -> THIS.rxSelectNumericInput(Weight.LB, WEIGHT_LB))

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
