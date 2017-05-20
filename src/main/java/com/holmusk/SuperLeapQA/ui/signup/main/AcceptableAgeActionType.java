package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.Zipped;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatComparisonType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeRepeatType;
import org.swiften.xtestkit.base.element.action.swipe.type.SwipeType;
import org.swiften.xtestkit.base.element.locator.general.param.TextParam;
import org.swiften.xtestkit.mobile.android.AndroidEngine;

import java.util.List;

/**
 * Created by haipham on 17/5/17.
 */
public interface AcceptableAgeActionType extends AcceptableAgeValidationType, DOBPickerActionType {
    /**
     * Select a value, assuming the user is in the value selection screen.
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link SLNumericInputType} instance.
     * @param NUMERIC_VALUE {@link Double} value.
     * @return {@link Flowable} instance.
     * @see #rx_e_pickerItemViews(Engine, SLChoiceInputType)
     * @see Engine#rx_click(WebElement)
     * @see SwipeRepeatComparisonType#rx_scrollViewChildCount()
     */
    @NotNull
    @SuppressWarnings("Convert2MethodRef")
    default <P extends SLChoiceInputType & SLNumericInputType>
    Flowable<?> rx_a_selectNumericInput(@NotNull final Engine<?> ENGINE,
                                        @NotNull final P MODE,
                                        final double NUMERIC_VALUE) {
        final AcceptableAgeActionType THIS = this;
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
                return THIS.rx_e_pickerItemViews(ENGINE, MODE);
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

            @NotNull
            @Override
            public Flowable<Double> rx_elementSwipeRatio() {
                double ratio;

                if (ENGINE instanceof AndroidEngine) {
                    /* Since NumberPicker only shows 1 element at at time,
                     * we need to scroll very slowly in order to arrive at
                     * the element that we want */
                    ratio = 1d / 3;
                } else {
                    ratio = 0.7;
                }

                return Flowable.just(ratio);
            }

            @NotNull
            @Override
            public Flowable<Boolean> rx_shouldKeepSwiping() {
                return ENGINE
                    .rx_withText(TEXT_PARAM)
                    .firstElement()
                    .toFlowable()
                    .filter(a -> ENGINE.getText(a).equals(STR_VALUE))
                    .flatMap(ENGINE::rx_click)
                    .map(BooleanUtil::toTrue);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rx_scrollableViewToSwipe() {
                return rx_e_scrollableChoicePicker(ENGINE, MODE);
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
     * @param ENGINE {@link Engine} instance.
     * @param inputs {@link List} of {@link Zipped} instances.
     * @return {@link Flowable} instance.
     * @see #rx_a_selectNumericInput(Engine, SLChoiceInputType, double)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    default <P extends SLChoiceInputType & SLNumericInputType>
    Flowable<?> rx_a_selectNumericInput(@NotNull final Engine<?> ENGINE,
                                        @NotNull List<Zipped<P,Double>> inputs) {
        final AcceptableAgeActionType THIS = this;
        LogUtil.printfThread("Selecting inputs for %s", inputs);

        return Flowable
            .fromIterable(inputs)
            .concatMap(a -> {
                if (ObjectUtil.nonNull(a.A, a.B)) {
                    return THIS
                        .rx_a_selectNumericInput(ENGINE, a.A, a.B)
                        .map(BooleanUtil::toTrue)

                        /* We need this statement because each app may have
                         * different set of required numeric choice inputs.
                         * For e.g., on Android there is no cm decimal field
                         * so if we do not catch the error, the test will
                         * fail */
                        .onErrorReturnItem(true);
                } else {
                    return RxUtil.error(NOT_AVAILABLE);
                }
            })
            .all(BooleanUtil::isTrue)
            .toFlowable();
    }

    /**
     * Confirm numeric choice input (e.g. for {@link Height} and {@link Weight}).
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_click(WebElement)
     * @see BooleanUtil#toTrue(Object)
     */
    @NotNull
    default Flowable<?> rx_a_confirmNumericChoice(@NotNull final Engine ENGINE) {
        return rx_e_numericChoiceConfirm(ENGINE).flatMap(ENGINE::rx_click);
    }

    /**
     * Select {@link Ethnicity} for {@link ChoiceInput#ETHNICITY}.
     * @param ENGINE {@link Engine} instance.
     * @param E {@link Ethnicity} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     * @see #rx_a_clickInputField(Engine, SLInputType)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_selectEthnicity(@NotNull final Engine<?> ENGINE,
                                             @NotNull final Ethnicity E) {
        return rx_a_clickInputField(ENGINE, ChoiceInput.ETHNICITY)
            .flatMap(a -> ENGINE.rx_containsText(E.value()))
            .firstElement()
            .toFlowable()
            .flatMap(ENGINE::rx_click)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Select {@link CoachPref} for {@link ChoiceInput#COACH_PREF}.
     * @param ENGINE {@link Engine} instance.
     * @param CP {@link CoachPref} instance.
     * @return {@link Flowable} instance.
     * @see Engine#rx_containsText(String...)
     * @see #rx_a_clickInputField(Engine, SLInputType)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_selectCoachPref(@NotNull final Engine<?> ENGINE,
                                             @NotNull final CoachPref CP) {
        return rx_a_clickInputField(ENGINE, ChoiceInput.COACH_PREF)
            .flatMap(a -> ENGINE.rx_containsText(CP.value()))
            .firstElement()
            .toFlowable()
            .flatMap(ENGINE::rx_click)
            .map(BooleanUtil::toTrue);
    }

    /**
     * Confirm the acceptable age inputs by clicking the next button, assuming
     * the user is already in the acceptable age input screen.
     * @return {@link Flowable} instance.
     * @see #rx_e_acceptableAgeConfirm(Engine)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_confirmAcceptableAgeInputs(@NotNull final Engine<?> ENGINE) {
        return rx_e_acceptableAgeConfirm(ENGINE).flatMap(ENGINE::rx_click);
    }

    /**
     * Enter random acceptable age inputs in order to access the personal
     * information input screen.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see Height#randomValue(UserMode)
     * @see Weight#randomValue(UserMode)
     * @see #rx_a_clickInputField(Engine, SLInputType)
     * @see #rx_a_selectEthnicity(Engine, Ethnicity)
     * @see #rx_a_selectCoachPref(Engine, CoachPref)
     * @see #rx_a_selectNumericInput(Engine, SLChoiceInputType, double)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_a_enterAcceptableAgeInputs(@NotNull final Engine<?> E,
                                                      @NotNull UserMode mode) {
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

        return rx_a_clickInputField(E, GENDER)
            .flatMap(a -> rx_a_selectEthnicity(E, ETH))
            .flatMap(a -> rx_a_selectCoachPref(E, CP))

            .flatMap(a -> rx_a_clickInputField(E, HEIGHT_MODE))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_a_selectNumericInput(E, HEIGHT_MODE, HEIGHT))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> rx_a_clickInputField(E, WEIGHT_MODE))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rx_a_selectNumericInput(E, WEIGHT_MODE, WEIGHT))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E));
    }
}
