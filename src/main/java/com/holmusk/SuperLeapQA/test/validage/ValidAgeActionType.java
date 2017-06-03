package com.holmusk.SuperLeapQA.test.validage;

import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.HMUITestKit.model.HMChoiceType;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.model.SLNumericChoiceType;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.Zip;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.ios.IOSEngine;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 17/5/17.
 */
public interface ValidAgeActionType extends BaseActionType, ValidAgeValidationType {
    /**
     * Confirm numeric choice input (e.g. for {@link Height} and {@link Weight}).
     * @param ENGINE {@link Engine} instance.
     * @return {@link Flowable} instance.
     * @see #rxe_numericChoiceConfirm(Engine)
     * @see Engine#rxa_click(WebElement)
     */
    @NotNull
    default Flowable<?> rxa_confirmNumericChoice(@NotNull final Engine<?> ENGINE) {
        return rxe_numericChoiceConfirm(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Select the choice mode and open the choice picker. The actions are
     * different for {@link Platform#ANDROID} and {@link Platform#IOS}.
     * On {@link Platform#ANDROID}, the choice mode is outside the picker.
     * Therefore, we need to select the mode first before opening the picker.
     * On {@link Platform#IOS}, the choice mode is within the picker, so we
     * need to open the picker first, then select the mode.
     * @param ENGINE {@link Engine} instance.
     * @param CHOICE {@link HMChoiceType} instance.
     * @param NUMERIC {@link SLNumericChoiceType} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_clickInputField(Engine, HMInputType)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_selectUnitSystemPicker(
        @NotNull final Engine<?> ENGINE,
        @NotNull final HMChoiceType CHOICE,
        @NotNull final SLNumericChoiceType NUMERIC
    ) {
        final ValidAgeActionType THIS = this;

        if (ENGINE instanceof AndroidEngine) {
            return rxa_clickInputField(ENGINE, NUMERIC)
                .flatMap(a -> THIS.rxa_clickInputField(ENGINE, CHOICE));
        } else if (ENGINE instanceof IOSEngine) {
            return rxa_clickInputField(ENGINE, CHOICE)
                .flatMap(a -> THIS.rxa_clickInputField(ENGINE, NUMERIC));
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
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_textChoiceConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmTextChoice(@NotNull final Engine<?> ENGINE) {
        if (ENGINE instanceof IOSEngine) {
            return rxe_textChoiceConfirm(ENGINE).flatMap(ENGINE::rxa_click);
        } else {
            return Flowable.just(true);
        }
    }

    /**
     * Confirm the acceptable age inputs by clicking the next button, assuming
     * the user is already in the acceptable age input screen.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_click(WebElement)
     * @see #rxe_validAgeConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmValidAgeInputs(@NotNull final Engine<?> ENGINE) {
        return rxe_validAgeConfirm(ENGINE).flatMap(ENGINE::rxa_click);
    }

    /**
     * Enter random acceptable age inputs in order to access the personal
     * information input screen.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see Height#randomValue(UserMode)
     * @see Weight#randomValue(UserMode)
     * @see #rxa_clickInputField(Engine, HMInputType)
     * @see #rxa_selectChoice(Engine, HMChoiceType, String)
     * @see #rxa_confirmNumericChoice(Engine)
     * @see #rxa_confirmTextChoice(Engine)
     */
    @NotNull
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    default Flowable<?> rxa_enterValidAgeInputs(@NotNull final Engine<?> E,
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

        return rxa_clickInputField(E, GENDER)
            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_HEIGHT, HEIGHT_MODE))
            .flatMap(a -> THIS.rxa_selectChoice(E, HEIGHT))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))

            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_WEIGHT, WEIGHT_MODE))
            .flatMap(a -> THIS.rxa_selectChoice(E, WEIGHT))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))

            .flatMap(a -> THIS.rxa_clickInputField(E, ChoiceInput.ETHNICITY))
            .flatMap(a -> THIS.rxa_selectChoice(E, ChoiceInput.ETHNICITY, ETH.stringValue()))
            .flatMap(a -> THIS.rxa_confirmTextChoice(E))
            .flatMap(a -> THIS.rxa_clickInputField(E, ChoiceInput.COACH_PREF))
            .flatMap(a -> THIS.rxa_selectChoice(E, ChoiceInput.COACH_PREF, CP.stringValue()))
            .flatMap(a -> THIS.rxa_confirmTextChoice(E));
    }

    /**
     * Enter and confirm valid age inputs.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #validAgeInputProgressDelay(Engine)
     * @see #rxa_enterValidAgeInputs(Engine, UserMode)
     * @see #rxa_confirmValidAgeInputs(Engine)
     */
    @NotNull
    default Flowable<?> rxa_enterAndConfirmValidAgeInputs(
        @NotNull final Engine<?> ENGINE,
        @NotNull UserMode mode
    ) {
        final ValidAgeActionType THIS = this;

        return rxa_enterValidAgeInputs(ENGINE, mode)
            .flatMap(a -> THIS.rxa_confirmValidAgeInputs(ENGINE))
            .delay(validAgeInputProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }
}
