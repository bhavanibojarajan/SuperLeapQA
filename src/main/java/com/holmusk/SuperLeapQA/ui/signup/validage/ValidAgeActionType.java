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
import org.swiften.xtestkit.base.element.action.choice.ChoiceSelector;
import org.swiften.xtestkit.base.element.action.choice.type.ChoiceSelectorSwipeType;
import org.swiften.xtestkit.base.element.action.input.type.ChoiceInputType;
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
     * @param engine {@link Engine} instance.
     * @param input {@link SLNumericChoiceInputType} instance.
     * @param selected {@link String} value.
     * @return {@link Flowable} instance.
     * @see Engine#rx_click(WebElement)
     * @see ChoiceSelector#rx_repeatSwipe()
     */
    @NotNull
    @SuppressWarnings("Convert2MethodRef")
    default Flowable<?> rx_a_selectChoice(@NotNull Engine<?> engine,
                                          @NotNull SLChoiceInputType input,
                                          @NotNull String selected) {
        LogUtil.printfThread("Selecting %s for %s", selected, input);

        ChoiceSelector selector = ChoiceSelector.builder()
            .withEngine(engine)
            .withInput(input)
            .withSelectedChoice(selected)
            .build();

        return selector.rx_repeatSwipe();
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
     * @see #rx_a_selectChoice(Engine, SLChoiceInputType, String)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    default <P extends SLChoiceInputType & SLNumericChoiceInputType>
    Flowable<?> rx_a_selectChoice(@NotNull final Engine<?> ENGINE,
                                  @NotNull List<Zipped<P,String>> inputs) {
        final ValidAgeActionType THIS = this;

        return Flowable
            .fromIterable(inputs)
            .concatMap(a -> {
                if (ObjectUtil.nonNull(a.A, a.B)) {
                    return THIS
                        .rx_a_selectChoice(ENGINE, a.A, a.B)
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
     * @see #rx_a_selectChoice(Engine, SLChoiceInputType, String)
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
        final int HEIGHT = HEIGHT_MODE.randomValue(mode);
        final int WEIGHT = WEIGHT_MODE.randomValue(mode);

        return rx_a_clickInputField(E, GENDER)
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.ETHNICITY))
            .flatMap(a -> THIS.rx_a_selectChoice(E, ChoiceInput.ETHNICITY, ETH.stringValue()))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.COACH_PREF))
            .flatMap(a -> THIS.rx_a_selectChoice(E, ChoiceInput.COACH_PREF, CP.stringValue()))

            .flatMap(a -> rx_a_clickInputField(E, HEIGHT_MODE))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_a_selectChoice(E, HEIGHT_MODE, String.valueOf(HEIGHT)))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> THIS.rx_a_clickInputField(E, WEIGHT_MODE))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rx_a_selectChoice(E, WEIGHT_MODE, String.valueOf(WEIGHT)))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E));
    }
}
