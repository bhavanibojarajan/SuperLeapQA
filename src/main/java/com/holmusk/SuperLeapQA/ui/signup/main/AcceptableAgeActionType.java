package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.Pair;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.BaseEngine;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatComparisonType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeType;
import org.swiften.xtestkit.base.element.locator.general.param.TextParam;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.base.element.action.input.type.NumericInputType;
import org.swiften.xtestkit.mobile.android.AndroidEngine;

import java.util.List;

/**
 * Created by haipham on 17/5/17.
 */
public interface AcceptableAgeActionType extends
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
        final AcceptableAgeActionType THIS = this;
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
        final AcceptableAgeActionType THIS = this;
        return rxEnterAcceptableAgeInputs().flatMap(a -> THIS.rxConfirmAcceptableAgeInputs());
    }

    /**
     * Select a value, assuming the user is in the value selection screen.
     * @param MODE A {@link NumericInputType} instance.
     * @param NUMERIC_VALUE A {@link Double} value.
     * @return A {@link Flowable} instance.
     * @see #rxPickerItemViews(SLChoiceInputType)
     * @see BaseEngine#rxClick(WebElement)
     * @see SwipeRepeatComparisonType#rxScrollViewChildCount()
     */
    @NotNull
    @SuppressWarnings("Convert2MethodRef")
    default <P extends SLChoiceInputType & SLNumericInputType>
    Flowable<Boolean> rxSelectNumericInput(@NotNull final P MODE,
                                           final double NUMERIC_VALUE) {
        final AcceptableAgeActionType THIS = this;
        final BaseEngine<?> ENGINE = engine();
        final String STR_VALUE = MODE.stringValue(NUMERIC_VALUE);
        LogUtil.printfThread("Selecting %d for %s", (int)NUMERIC_VALUE, MODE);

        final TextParam TEXT_PARAM = TextParam.builder()
            .withText(STR_VALUE)
            .withRetries(0)
            .build();

        SwipeRepeatType repeater = new SwipeRepeatComparisonType() {
            @NotNull
            @Override
            public Flowable<Integer> rxInitialDifference(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(a -> MODE.numericValue(a))
                    .map(a -> a - NUMERIC_VALUE)
                    .map(Double::intValue);
            }

            @NotNull
            @Override
            public Flowable<?> rxCompareFirst(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(a -> MODE.numericValue(a))
                    .filter(a -> a > NUMERIC_VALUE);
            }

            @NotNull
            @Override
            public Flowable<?> rxCompareLast(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(a -> MODE.numericValue(a))
                    .filter(a -> a < NUMERIC_VALUE);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rxScrollViewChildItems() {
                return THIS.rxPickerItemViews(MODE);
            }

            @NotNull
            public Flowable<Long> rxScrollViewChildCount() {
                /* We need to override this for Android because it is using
                 * a NumberPicker, which displays 3+ elements but only return
                 * one active element when queries with XPath */
                if (ENGINE instanceof AndroidEngine) {
                    return Flowable.just(3L);
                } else {
                    return SwipeRepeatComparisonType.super.rxScrollViewChildCount();
                }
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
                    .filter(a -> ENGINE.getText(a).equals(STR_VALUE))
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
     * @see #rxSelectNumericInput(SLChoiceInputType, double)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    default <P extends SLChoiceInputType & SLNumericInputType>
    Flowable<Boolean> rxSelectNumericInput(@NotNull List<Pair<P,Double>> inputs) {
        final AcceptableAgeActionType THIS = this;
        LogUtil.printfThread("Selecting inputs for %s", inputs);

        return Flowable
            .fromIterable(inputs)
            .concatMap(a -> {
                if (ObjectUtil.nonNull(a.A, a.B)) {
                    return THIS
                        .rxSelectNumericInput(a.A, a.B)

                        /* We need this statement because each app may have
                         * different set of required numeric choice inputs.
                         * For e.g., on Android there is no cm decimal field
                         * so if we do not catch the error, the test will
                         * fail */
                        .onErrorReturnItem(true);
                } else {
                    return RxUtil.error(NOT_IMPLEMENTED);
                }
            })
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Confirm numeric choice input (e.g. for {@link Height} and
     * {@link Weight}).
     * @return A {@link Flowable} instance.
     * @see #engine()
     * @see BaseEngine#rxClick(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rxConfirmNumericChoiceInput() {
        final BaseEngine<?> ENGINE = engine();

        return rxNumericChoiceInputConfirmButton()
            .flatMap(ENGINE::rxClick)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Select an {@link Ethnicity} for {@link ChoiceInput#ETHNICITY}.
     * @param E An {@link Ethnicity} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     * @see #rxClickInputField(SLInputType)
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
     * @see #rxClickInputField(SLInputType)
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
     * @see #rxClickInputField(SLInputType)
     * @see #rxSelectEthnicity(Ethnicity)
     * @see #rxSelectCoachPref(CoachPref)
     * @see #rxSelectNumericInput(SLChoiceInputType, double)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAcceptableAgeInputs() {
        final AcceptableAgeActionType THIS = this;
        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());
//        final Height HEIGHT_MODE = CollectionTestUtil.randomElement(Height.values());
//        final Weight WEIGHT_MODE = CollectionTestUtil.randomElement(Weight.values());
        final Height HEIGHT_MODE = Height.CHILD_CM; // TODO: Randomize when ft bug is fixed
        final Weight WEIGHT_MODE = Weight.CHILD_KG; // TODO: Randomize when lb bug is fixed
        final double HEIGHT = HEIGHT_MODE.randomValue();
        final double WEIGHT = WEIGHT_MODE.randomValue();

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
}
