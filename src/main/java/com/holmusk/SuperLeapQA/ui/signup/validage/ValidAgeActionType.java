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
import org.swiften.javautilities.collection.Zip;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.element.action.choice.ChoiceMode;
import org.swiften.xtestkit.base.element.action.choice.ChoiceParam;
import org.swiften.xtestkit.base.element.action.choice.ChoiceType;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.ios.IOSEngine;

import java.util.List;

/**
 * Created by haipham on 17/5/17.
 */
public interface ValidAgeActionType extends ValidAgeValidationType, DOBPickerActionType {
    /**
     * Select a value, assuming the user is in the value selection picker.
     * @param engine {@link Engine} instance.
     * @param input {@link SLNumericChoiceInputType} instance.
     * @param selected {@link String} value.
     * @return {@link Flowable} instance.
     * @see ChoiceMode#GENERAL
     * @see Engine#rx_selectChoice(ChoiceType)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rx_a_selectChoice(@NotNull Engine<?> engine,
                                          @NotNull SLChoiceInputType input,
                                          @NotNull String selected) {
        LogUtil.printfThread("Selecting %s for %s", selected, input);

        ChoiceParam param = ChoiceParam.builder()
            .withMode(ChoiceMode.GENERAL)
            .withInput(input)
            .withSelectedChoice(selected)
            .build();

        return engine.rx_selectChoice(param);
    }

    /**
     * Select values for a set {@link SLNumericChoiceInputType}. This is
     * useful when we want to select {@link Height} or {@link Weight} based
     * on different units of measurement (metric/imperial), since the app
     * requires a combination of two inputs from two
     * {@link SLNumericChoiceInputType} (e.g. {@link Height#CM} and
     * {@link Height#CM_DEC}).
     * @param ENGINE {@link Engine} instance.
     * @param inputs {@link List} of {@link Zip} instances.
     * @return {@link Flowable} instance.
     * @see #rx_a_selectChoice(Engine, SLChoiceInputType, String)
     * @see BooleanUtil#isTrue(boolean)
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    default <P extends SLChoiceInputType & SLNumericChoiceInputType>
    Flowable<?> rx_a_selectChoice(@NotNull final Engine<?> ENGINE,
                                  @NotNull List<Zip<P,String>> inputs) {
        final ValidAgeActionType THIS = this;

        return Flowable
            .fromIterable(inputs)
            .concatMap(a -> THIS.rx_a_selectChoice(ENGINE, a.A, a.B))
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Confirm numeric choice input (e.g. for {@link Height} and {@link Weight}).
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_numericChoiceConfirm(Engine)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_confirmNumericChoice(@NotNull final Engine<?> ENGINE) {
        return rx_e_numericChoiceConfirm(ENGINE).flatMap(ENGINE::rx_click);
    }

    /**
     * Select the choice mode and open the choice picker. The actions are
     * different for {@link Platform#ANDROID} and {@link Platform#IOS}.
     * On {@link Platform#ANDROID}, the choice mode is outside the picker.
     * Therefore, we need to select the mode first before opening the picker.
     * On {@link Platform#IOS}, the choice mode is within the picker, so we
     * need to open the picker first, then select the mode.
     * @param ENGINE {@link Engine} instance.
     * @param CHOICE {@link SLChoiceInputType} instance.
     * @param NUMERIC {@link SLNumericChoiceInputType} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_clickInputField(Engine, SLInputType)
     */
    @NotNull
    default Flowable<?> rx_a_selectUnitSystemPicker(
        @NotNull final Engine<?> ENGINE,
        @NotNull final SLChoiceInputType CHOICE,
        @NotNull final SLNumericChoiceInputType NUMERIC
    ) {
        final ValidAgeActionType THIS = this;

        if (ENGINE instanceof AndroidEngine) {
            return rx_a_clickInputField(ENGINE, NUMERIC)
                .flatMap(a -> THIS.rx_a_clickInputField(ENGINE, CHOICE));
        } else if (ENGINE instanceof IOSEngine) {
            return rx_a_clickInputField(ENGINE, CHOICE)
                .flatMap(a -> THIS.rx_a_clickInputField(ENGINE, NUMERIC));
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

    /**
     * Confirm text choice input, (e.g. for {@link ChoiceInput#ETHNICITY} or
     * {@link ChoiceInput#COACH_PREF}.
     * This is only relevant for {@link Platform#IOS} since we need to
     * click on a Done button. On {@link Platform#ANDROID}, clicking the target
     * item automatically dismisses the picker dialog.
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rx_e_textChoiceConfirm(Engine)
     * @see Engine#rx_click(WebElement)
     */
    @NotNull
    default Flowable<?> rx_a_confirmTextChoice(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof IOSEngine) {
            return rx_e_textChoiceConfirm(ENGINE).flatMap(ENGINE::rx_click);
        } else {
            return Flowable.just(true);
        }
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
     * @see #rx_a_confirmNumericChoice(Engine)
     * @see #rx_a_confirmTextChoice(Engine)
     */
    @NotNull
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    default Flowable<?> rx_a_enterValidAgeInputs(@NotNull final Engine<?> E,
                                                 @NotNull UserMode mode) {
        final ValidAgeActionType THIS = this;
        PlatformType platform = E.platform();
        UnitSystem unit = CollectionTestUtil.randomElement(UnitSystem.values());
        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());
        final List<Zip<Height,String>> HEIGHT = Height.random(platform, mode, unit);
        final List<Zip<Weight,String>> WEIGHT = Weight.random(platform, mode, unit);
        final ChoiceInput C_HEIGHT = ChoiceInput.HEIGHT;
        final ChoiceInput C_WEIGHT = ChoiceInput.WEIGHT;
        final Height HEIGHT_MODE = HEIGHT.get(0).A;
        final Weight WEIGHT_MODE = WEIGHT.get(0).A;

        return rx_a_clickInputField(E, GENDER)
            .flatMap(a -> THIS.rx_a_selectUnitSystemPicker(E, C_HEIGHT, HEIGHT_MODE))
            .flatMap(a -> THIS.rx_a_selectChoice(E, HEIGHT))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> THIS.rx_a_selectUnitSystemPicker(E, C_WEIGHT, WEIGHT_MODE))
            .flatMap(a -> THIS.rx_a_selectChoice(E, WEIGHT))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.ETHNICITY))
            .flatMap(a -> THIS.rx_a_selectChoice(E, ChoiceInput.ETHNICITY, ETH.stringValue()))
            .flatMap(a -> THIS.rx_a_confirmTextChoice(E))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.COACH_PREF))
            .flatMap(a -> THIS.rx_a_selectChoice(E, ChoiceInput.COACH_PREF, CP.stringValue()))
            .flatMap(a -> THIS.rx_a_confirmTextChoice(E));
    }

    /**
     * Enter and confirm valid age inputs.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_enterValidAgeInputs(Engine, UserMode)
     * @see #rx_a_confirmValidAgeInputs(Engine)
     */
    @NotNull
    default Flowable<?> rx_a_enterAndConfirmValidAgeInputs(@NotNull final Engine<?> ENGINE,
                                                           @NotNull UserMode mode) {
        final ValidAgeActionType THIS = this;

        return rx_a_enterValidAgeInputs(ENGINE, mode).flatMap(a ->
            THIS.rx_a_confirmValidAgeInputs(ENGINE)
        );
    }
}
