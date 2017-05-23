package com.holmusk.SuperLeapQA.ui.signup.validage;

import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.type.SLChoiceInputType;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLNumericChoiceInputType;
import com.holmusk.SuperLeapQA.ui.signup.dob.DOBPickerActionType;
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
import org.swiften.xtestkit.base.element.locator.general.param.ByXPath;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.android.AndroidEngine;

import java.util.List;

/**
 * Created by haipham on 17/5/17.
 */
public interface ValidAgeActionType extends ValidAgeValidationType, DOBPickerActionType {
    /**
     * Select a value, assuming the user is in the value selection screen.
     * @param ENGINE {@link Engine} instance.
     * @param INPUT {@link SLNumericChoiceInputType} instance.
     * @param STR_VALUE {@link String} value.
     * @return {@link Flowable} instance.
     * @see #rx_e_pickerItemViews(Engine, SLChoiceInputType)
     * @see Engine#rx_click(WebElement)
     * @see SwipeRepeatComparisonType#rx_scrollViewChildCount()
     */
    @NotNull
    @SuppressWarnings("Convert2MethodRef")
    default Flowable<?> rx_a_selectChoiceInput(@NotNull final Engine<?> ENGINE,
                                               @NotNull final SLChoiceInputType INPUT,
                                               @NotNull final String STR_VALUE) {
        LogUtil.printfThread("Selecting %s for %s", STR_VALUE, INPUT);

        final ValidAgeActionType THIS = this;
        final double NUMERIC_VALUE = INPUT.numericValue(STR_VALUE);
        final String LC_STR = STR_VALUE.toLowerCase();
        PlatformType platform = ENGINE.platform();

        final ByXPath QUERY = ByXPath.builder()
            .withXPath(INPUT.targetChoiceItemXPath(platform, STR_VALUE))
            .withRetries(1)
            .build();

        SwipeRepeatType repeater = new SwipeRepeatComparisonType() {
            @NotNull
            @Override
            public Flowable<Integer> rx_initialDifference(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(a -> INPUT.numericValue(a))
                    .map(a -> a - NUMERIC_VALUE)
                    .map(Double::intValue);
            }

            @NotNull
            @Override
            public Flowable<?> rx_compareFirst(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(a -> INPUT.numericValue(a))
                    .filter(a -> a > NUMERIC_VALUE);
            }

            @NotNull
            @Override
            public Flowable<?> rx_compareLast(@NotNull WebElement element) {
                return Flowable.just(element)
                    .map(ENGINE::getText)
                    .map(a -> INPUT.numericValue(a))
                    .filter(a -> a < NUMERIC_VALUE);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rx_scrollViewChildItems() {
                return THIS.rx_e_pickerItemViews(ENGINE, INPUT);
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
                    .rx_byXPath(QUERY)
                    .firstElement()
                    .toFlowable()
                    .filter(a -> ENGINE.getText(a).toLowerCase().equals(LC_STR))
                    .flatMap(ENGINE::rx_click)
                    .map(BooleanUtil::toTrue);
            }

            @NotNull
            @Override
            public Flowable<WebElement> rx_scrollableViewToSwipe() {
                return rx_e_scrollableChoicePicker(ENGINE, INPUT);
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
     * Select values for a set {@link SLNumericChoiceInputType}. This is
     * useful when we want to select {@link Height} or {@link Weight} based
     * on different units of measurement (metric/imperial), since the app
     * requires a combination of two inputs from two
     * {@link SLNumericChoiceInputType} (e.g. {@link Height#CM} and
     * {@link Height#CM_DEC}).
     * @param ENGINE {@link Engine} instance.
     * @param inputs {@link List} of {@link Zipped} instances.
     * @return {@link Flowable} instance.
     * @see #rx_a_selectChoiceInput(Engine, SLChoiceInputType, String)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    default <P extends SLChoiceInputType & SLNumericChoiceInputType>
    Flowable<?> rx_a_selectChoiceInput(@NotNull final Engine<?> ENGINE,
                                       @NotNull List<Zipped<P,String>> inputs) {
        final ValidAgeActionType THIS = this;

        return Flowable
            .fromIterable(inputs)
            .concatMap(a -> {
                if (ObjectUtil.nonNull(a.A, a.B)) {
                    return THIS
                        .rx_a_selectChoiceInput(ENGINE, a.A, a.B)
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
     * Confirm the acceptable age inputs by clicking the next button, assuming
     * the user is already in the acceptable age input screen.
     * @return {@link Flowable} instance.
     * @see #rx_e_validAgeConfirm(Engine)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_confirmValidAgeInputs(@NotNull final Engine<?> ENGINE) {
        return rx_e_validAgeConfirm(ENGINE).flatMap(ENGINE::rx_click);
    }

    /**
     * Enter random acceptable age inputs in order to access the personal
     * information input screen.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see Height#randomValue(UserMode)
     * @see Weight#randomValue(UserMode)
     * @see #rx_a_clickInputField(Engine, SLInputType)
     * @see #rx_a_selectChoiceInput(Engine, SLChoiceInputType, String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_a_enterValidAgeInputs(@NotNull final Engine<?> E,
                                                 @NotNull UserMode mode) {
        final ValidAgeActionType THIS = this;
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
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.ETHNICITY))
            .flatMap(a -> THIS.rx_a_selectChoiceInput(E, ChoiceInput.ETHNICITY, ETH.toString()))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.COACH_PREF))
            .flatMap(a -> THIS.rx_a_selectChoiceInput(E, ChoiceInput.COACH_PREF, CP.toString()))

            .flatMap(a -> rx_a_clickInputField(E, HEIGHT_MODE))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_a_selectChoiceInput(E, HEIGHT_MODE, String.valueOf(HEIGHT)))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> THIS.rx_a_clickInputField(E, WEIGHT_MODE))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rx_a_selectChoiceInput(E, WEIGHT_MODE, String.valueOf(WEIGHT)))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E));
    }
}
