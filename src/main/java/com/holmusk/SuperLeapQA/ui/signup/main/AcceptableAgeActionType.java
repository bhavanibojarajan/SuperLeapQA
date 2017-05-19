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
import org.swiften.xtestkit.mobile.android.AndroidEngine;

import java.util.List;

/**
 * Created by haipham on 17/5/17.
 */
public interface AcceptableAgeActionType extends AcceptableInputValidationType, DOBPickerActionType {
    //region Bridged Navigation
    /**
     * Bridge method that helps navigate from splash screen to personal info
     * input.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_splash_acceptableAge(UserMode)
     * @see #rx_acceptableAge_personalInfo(UserMode)
     */
    @NotNull
    default Flowable<Boolean> rx_splash_personalInfo(@NotNull final UserMode MODE) {
        final AcceptableAgeActionType THIS = this;

        return rx_splash_acceptableAge(MODE)
            .flatMap(a -> THIS.rx_acceptableAge_personalInfo(MODE));
    }
    //endregion

    /**
     * Navigate from the acceptable age input to the personal info input
     * screen.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_enterAcceptableAgeInputs(UserMode)
     * @see #rx_confirmAcceptableAgeInputs()
     */
    @NotNull
    default Flowable<Boolean> rx_acceptableAge_personalInfo(@NotNull final UserMode MODE) {
        final AcceptableAgeActionType THIS = this;

        return rx_enterAcceptableAgeInputs(MODE)
            .flatMap(a -> THIS.rx_confirmAcceptableAgeInputs());
    }

    /**
     * Select a value, assuming the user is in the value selection screen.
     * @param MODE A {@link SLNumericInputType} instance.
     * @param NUMERIC_VALUE A {@link Double} value.
     * @return A {@link Flowable} instance.
     * @see #rxPickerItemViews(SLChoiceInputType)
     * @see BaseEngine#rx_click(WebElement)
     * @see SwipeRepeatComparisonType#rx_scrollViewChildCount()
     */
    @NotNull
    @SuppressWarnings("Convert2MethodRef")
    default <P extends SLChoiceInputType & SLNumericInputType>
    Flowable<Boolean> rx_selectNumericInput(@NotNull final P MODE,
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
            public Flowable<Integer> rx_initialDifference(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(a -> MODE.numericValue(a))
                    .map(a -> a - NUMERIC_VALUE)
                    .map(Double::intValue);
            }

            @NotNull
            @Override
            public Flowable<?> rx_compareFirst(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(a -> MODE.numericValue(a))
                    .filter(a -> a > NUMERIC_VALUE);
            }

            @NotNull
            @Override
            public Flowable<?> rx_compareLast(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(a -> MODE.numericValue(a))
                    .filter(a -> a < NUMERIC_VALUE);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rx_scrollViewChildItems() {
                return THIS.rxPickerItemViews(MODE);
            }

            @NotNull
            public Flowable<Long> rx_scrollViewChildCount() {
                /* We need to override this for Android because it is using
                 * a NumberPicker, which displays 3+ elements but only return
                 * one active element when queries with XPath */
                if (ENGINE instanceof AndroidEngine) {
                    return Flowable.just(3L);
                } else {
                    return SwipeRepeatComparisonType.super.rx_scrollViewChildCount();
                }
            }

            @Override
            public double elementSwipeRatio() {
                return 0.7d;
            }

            @NotNull
            @Override
            public Flowable<Boolean> rx_shouldKeepSwiping() {
                return ENGINE
                    .rxElementWithText(TEXT_PARAM)
                    .filter(a -> ENGINE.getText(a).equals(STR_VALUE))
                    .flatMap(ENGINE::rx_click)
                    .map(BooleanUtil::toTrue);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rx_scrollableViewToSwipe() {
                return rxScrollableChoicePicker(MODE);
            }

            @NotNull
            @Override
            public Flowable<Boolean> rx_swipeOnce(@NotNull SwipeType param) {
                return ENGINE.rx_swipeOnce(param);
            }
        };

        return repeater.rx_repeatSwipe();
    }

    /**
     * Select values for a set {@link SLNumericInputType}. This is
     * useful when we want to select {@link Height} or {@link Weight} based
     * on different units of measurement (metric/imperial), since the app
     * requires a combination of two inputs from two
     * {@link SLNumericInputType} (e.g. {@link Height#CM} and
     * {@link Height#CM_DEC}).
     * @param inputs A {@link List} of {@link Pair} instances.
     * @return A {@link Flowable} instance.
     * @see #rx_selectNumericInput(SLChoiceInputType, double)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    default <P extends SLChoiceInputType & SLNumericInputType>
    Flowable<Boolean> rx_selectNumericInput(@NotNull List<Pair<P,Double>> inputs) {
        final AcceptableAgeActionType THIS = this;
        LogUtil.printfThread("Selecting inputs for %s", inputs);

        return Flowable
            .fromIterable(inputs)
            .concatMap(a -> {
                if (ObjectUtil.nonNull(a.A, a.B)) {
                    return THIS
                        .rx_selectNumericInput(a.A, a.B)

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
     * @see BaseEngine#rx_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<Boolean> rx_confirmNumericChoiceInput() {
        final BaseEngine<?> ENGINE = engine();

        return rx_numericChoiceInputConfirmButton()
            .flatMap(ENGINE::rx_click)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Select an {@link Ethnicity} for {@link ChoiceInput#ETHNICITY}.
     * @param E An {@link Ethnicity} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     * @see #rx_clickInputField(SLInputType)
     * @see BaseEngine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rx_selectEthnicity(@NotNull final Ethnicity E) {
        final BaseEngine<?> ENGINE = engine();

        return rx_clickInputField(ChoiceInput.ETHNICITY)
            .flatMap(a -> ENGINE.rxElementContainingText(E.value()))
            .flatMap(ENGINE::rx_click)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Select a {@link CoachPref} for {@link ChoiceInput#COACH_PREF}.
     * @param CP A {@link CoachPref} instance.
     * @return A {@link Flowable} instance.
     * @see BaseEngine#rxElementContainingText(String...)
     * @see #rx_clickInputField(SLInputType)
     * @see BaseEngine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rx_selectCoachPref(@NotNull final CoachPref CP) {
        final BaseEngine<?> ENGINE = engine();

        return rx_clickInputField(ChoiceInput.COACH_PREF)
            .flatMap(a -> ENGINE.rxElementContainingText(CP.value()))
            .flatMap(ENGINE::rx_click)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Confirm the acceptable age inputs by clicking the next button, assuming
     * the user is already in the acceptable age input screen.
     * @return A {@link Flowable} instance.
     * @see #rxAcceptableAgeConfirmButton()
     * @see BaseEngine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<Boolean> rx_confirmAcceptableAgeInputs() {
        return rxAcceptableAgeConfirmButton().flatMap(engine()::rx_click).map(a -> true);
    }

    /**
     * Enter random acceptable age inputs in order to access the personal
     * information input screen.
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see Height#randomValue(UserMode)
     * @see Weight#randomValue(UserMode)
     * @see #rx_clickInputField(SLInputType)
     * @see #rx_selectEthnicity(Ethnicity)
     * @see #rx_selectCoachPref(CoachPref)
     * @see #rx_selectNumericInput(SLChoiceInputType, double)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rx_enterAcceptableAgeInputs(@NotNull UserMode mode) {
        final AcceptableAgeActionType THIS = this;
        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());
//        final Height HEIGHT_MODE = CollectionTestUtil.randomElement(Height.values());
//        final Weight WEIGHT_MODE = CollectionTestUtil.randomElement(Weight.values());
        final Height HEIGHT_MODE = Height.CM; // TODO: Randomize when ft bug is fixed
        final Weight WEIGHT_MODE = Weight.KG; // TODO: Randomize when lb bug is fixed
        final double HEIGHT = HEIGHT_MODE.randomValue(mode);
        final double WEIGHT = WEIGHT_MODE.randomValue(mode);

        return rx_clickInputField(GENDER)
            .flatMap(a -> rx_selectEthnicity(ETH))
            .flatMap(a -> rx_selectCoachPref(CP))

            .flatMap(a -> rx_clickInputField(HEIGHT_MODE))
            .flatMap(a -> THIS.rx_clickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_selectNumericInput(HEIGHT_MODE, HEIGHT))

            .flatMap(a -> rx_clickInputField(WEIGHT_MODE))
            .flatMap(a -> THIS.rx_clickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rx_selectNumericInput(WEIGHT_MODE, WEIGHT));
    }
}
