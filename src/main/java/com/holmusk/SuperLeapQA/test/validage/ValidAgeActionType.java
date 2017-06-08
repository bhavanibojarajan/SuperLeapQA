package com.holmusk.SuperLeapQA.test.validage;

import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.bmi.BMIParam;
import com.holmusk.SuperLeapQA.bmi.BMIUtil;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.HMUITestKit.model.HMChoiceType;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.SuperLeapQA.model.SLNumericChoiceType;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.collection.Zip;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.platform.PlatformType;
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
     * Select a {@link Gender}. We need a separate method for this action
     * because the flow differs between {@link Platform#ANDROID} and
     * {@link Platform#IOS}.
     * @param ENGINE {@link Engine} instance.
     * @param gender {@link Gender} instance.
     * @return {@link Flowable} instance.
     * @see ChoiceInput#GENDER
     * @see Gender#stringValue()
     * @see #rxa_clickInput(Engine, HMInputType)
     * @see #rxa_selectChoice(Engine, HMChoiceType, String)
     * @see #rxa_confirmTextChoice(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_selectGender(@NotNull final Engine<?> ENGINE,
                                         @NotNull Gender gender) {
        if (ENGINE instanceof AndroidEngine) {
            return rxa_clickInput(ENGINE, gender);
        } else if (ENGINE instanceof IOSEngine) {
            final ValidAgeActionType THIS = this;
            final HMChoiceType CHOICE = ChoiceInput.GENDER;
            final String STR = gender.stringValue();

            return rxa_clickInput(ENGINE, CHOICE)
                .flatMap(a -> THIS.rxa_selectChoice(ENGINE, CHOICE, STR))
                .flatMap(a -> THIS.rxa_confirmTextChoice(ENGINE));
        } else {
            throw new RuntimeException(NOT_AVAILABLE);
        }
    }

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
     * @see #rxa_clickInput(Engine, HMInputType)
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
            return rxa_clickInput(ENGINE, NUMERIC)
                .flatMap(a -> THIS.rxa_clickInput(ENGINE, CHOICE));
        } else if (ENGINE instanceof IOSEngine) {
            return rxa_clickInput(ENGINE, CHOICE)
                .flatMap(a -> THIS.rxa_clickInput(ENGINE, NUMERIC));
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
     * Enter random acceptable age inputs in order to access
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}.
     * When the user is {@link UserMode#isParent()}, he/she needs to enter the
     * child's name and NRIC as well.
     * @param E {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @param validBMI {@link Boolean} value. If this is true, we will select
     *                  height and weight such that the calculated BMI value
     *                  passes the test.
     * @return {@link Flowable} instance.
     * @see BMIParam.Builder#withEthnicity(Ethnicity)
     * @see BMIParam.Builder#withGender(Gender)
     * @see BMIParam.Builder#withHeight(List)
     * @see BMIParam.Builder#withWeight(List)
     * @see BMIUtil#outOfWidestInvalidRange(UserMode, BMIParam)
     * @see BMIUtil#withinTightestInvalidRange(UserMode, BMIParam)
     * @see ChoiceInput#HEIGHT
     * @see ChoiceInput#WEIGHT
     * @see CoachPref#values()
     * @see CollectionUtil#randomElement(Object[])
     * @see Ethnicity#values()
     * @see Gender#values()
     * @see Height#randomValue(UserMode)
     * @see HMTextChoiceType.Item#stringValue()
     * @see com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO
     * @see UnitSystem#values()
     * @see UserMode#validAgeInfo(PlatformType)
     * @see Weight#randomValue(UserMode)
     * @see Zip#A
     * @see #rxa_selectGender(Engine, Gender)
     * @see #rxa_clickInput(Engine, HMInputType)
     * @see #rxa_selectChoice(Engine, HMChoiceType, String)
     * @see #rxa_selectUnitSystemPicker(Engine, HMChoiceType, SLNumericChoiceType)
     * @see #rxa_confirmNumericChoice(Engine)
     * @see #rxa_confirmTextChoice(Engine)
     * @see #rxa_randomInputs(Engine, List)
     */
    @NotNull
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    default Flowable<?> rxa_enterValidAgeInputs(@NotNull final Engine<?> E,
                                                @NotNull UserMode mode,
                                                boolean validBMI) {
        final ValidAgeActionType THIS = this;
        PlatformType platform = E.platform();
        UnitSystem unit = CollectionUtil.randomElement(UnitSystem.values());
        final Gender GENDER = CollectionUtil.randomElement(Gender.values());
        final Ethnicity ETH = CollectionUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionUtil.randomElement(CoachPref.values());
        final ChoiceInput C_HEIGHT = ChoiceInput.HEIGHT;
        final ChoiceInput C_WEIGHT = ChoiceInput.WEIGHT;
        final ChoiceInput C_ETH = ChoiceInput.ETHNICITY;
        final ChoiceInput C_COACH = ChoiceInput.COACH_PREF;

        List<Zip<Height,String>> height;
        List<Zip<Weight,String>> weight;
        BMIParam param;

        /* Keep randomizing until BMI falls out of healthy range, otherwise
         * the BMI check will fail */
        do {
            height = Height.random(platform, mode, unit);
            weight = Weight.random(platform, mode, unit);

            param = BMIParam.builder()
                .withEthnicity(ETH)
                .withGender(GENDER)
                .withHeight(height)
                .withWeight(weight)
                .build();

            LogUtil.printft("Current BMI: %.2f", param.bmi());
        } while (validBMI
            ? BMIUtil.withinTightestInvalidRange(mode, param)
            : BMIUtil.outOfWidestInvalidRange(mode, param));

        LogUtil.printft(
            "Selecting height: %s, weight: %s, BMI: %.2f",
            height,
            weight,
            param.bmi()
        );

        final Height H_MODE = height.get(0).A;
        final Weight W_MODE = weight.get(0).A;
        final List<Zip<Height,String>> HEIGHT = height;
        final List<Zip<Weight,String>> WEIGHT = weight;

        return rxa_randomInputs(E, mode.validAgeInfo(platform))

            /* Select gender */
            .flatMap(a -> THIS.rxa_selectGender(E, GENDER))

            /* Select unit system and height */
            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_HEIGHT, H_MODE))
            .flatMap(a -> THIS.rxa_selectChoice(E, HEIGHT))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))

            /* Select unit system and weight */
            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_WEIGHT, W_MODE))
            .flatMap(a -> THIS.rxa_selectChoice(E, WEIGHT))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))

            /* Select ethnicity */
            .flatMap(a -> THIS.rxa_clickInput(E, C_ETH))
            .flatMap(a -> THIS.rxa_selectChoice(E, C_ETH, ETH.stringValue()))
            .flatMap(a -> THIS.rxa_confirmTextChoice(E))

            /* Select coach preference */
            .flatMap(a -> THIS.rxa_clickInput(E, C_COACH))
            .flatMap(a -> THIS.rxa_selectChoice(E, C_COACH, CP.stringValue()))
            .flatMap(a -> THIS.rxa_confirmTextChoice(E));
    }

    /**
     * Enter and confirm valid age inputs.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @param validBMI {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see #validAgeInputProgressDelay(Engine)
     * @see #rxa_enterValidAgeInputs(Engine, UserMode, boolean)
     * @see #rxa_scrollToBottom(Engine)
     * @see #rxa_confirmValidAgeInputs(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completeValidAgeInputs(@NotNull final Engine<?> ENGINE,
                                                   @NotNull UserMode mode,
                                                   boolean validBMI) {
        final ValidAgeActionType THIS = this;

        return rxa_enterValidAgeInputs(ENGINE, mode, validBMI)
            .flatMap(a -> THIS.rxa_scrollToBottom(ENGINE))
            .flatMap(a -> THIS.rxa_confirmValidAgeInputs(ENGINE))
            .delay(validAgeInputProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Same as above, but declare valid BMI by default.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rxa_completeValidAgeInputs(Engine, UserMode, boolean)
     */
    @NotNull
    default Flowable<?> rxa_completeValidAgeInputs(@NotNull Engine<?> engine,
                                                   @NotNull UserMode mode) {
        return rxa_completeValidAgeInputs(engine, mode, true);
    }
}
