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
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputHelperType;
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
     * @param engine {@link Engine} instance.
     * @param gender {@link Gender} instance.
     * @return {@link Flowable} instance.
     * @see ChoiceInput#GENDER
     * @see Gender#stringValue(InputHelperType)
     * @see ObjectUtil#nonNull(Object)
     * @see #rxa_clickInput(Engine, HMInputType)
     * @see #rxa_selectChoice(Engine, HMChoiceType, String)
     * @see #rxa_confirmTextChoice(Engine)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    default Flowable<?> rxa_selectGender(@NotNull Engine<?> engine,
                                         @NotNull Gender gender) {
        if (engine instanceof AndroidEngine) {
            return rxa_clickInput(engine, gender);
        } else if (engine instanceof IOSEngine) {
            HMChoiceType choice = ChoiceInput.GENDER;
            String genderString = gender.stringValue(engine);

            return Flowable
                .concatArray(
                    rxa_clickInput(engine, choice),
                    rxa_selectChoice(engine, choice, genderString),
                    rxa_confirmTextChoice(engine)
                )
                .all(ObjectUtil::nonNull)
                .toFlowable();
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
     * @param engine {@link Engine} instance.
     * @param choice {@link HMChoiceType} instance.
     * @param numeric {@link SLNumericChoiceType} instance.
     * @return {@link Flowable} instance.
     * @see ObjectUtil#nonNull(Object)
     * @see #rxa_clickInput(Engine, HMInputType)
     * @see #NOT_AVAILABLE
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rxa_selectUnitSystemPicker(
        @NotNull Engine<?> engine,
        @NotNull HMChoiceType choice,
        @NotNull SLNumericChoiceType numeric
    ) {
        if (engine instanceof AndroidEngine) {
            return RxUtil
                .concatDelayEach(
                    generalDelay(engine),
                    rxa_clickInput(engine, numeric),
                    rxa_clickInput(engine, choice)
                )
                .all(ObjectUtil::nonNull)
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return RxUtil
                .concatDelayEach(
                    generalDelay(engine),
                    rxa_clickInput(engine, choice),
                    rxa_clickInput(engine, numeric)
                )
                .all(ObjectUtil::nonNull)
                .toFlowable();
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
     * @see #validAgeInputProgressDelay(Engine)
     * @see #rxe_validAgeConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmValidAgeInputs(@NotNull final Engine<?> ENGINE) {
        return rxe_validAgeConfirm(ENGINE)
            .flatMap(ENGINE::rxa_click)
            .delay(validAgeInputProgressDelay(ENGINE), TimeUnit.MILLISECONDS);
    }

    /**
     * Enter random acceptable age inputs in order to access
     * {@link com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO}.
     * When the user is {@link UserMode#isParent()}, he/she needs to enter the
     * child's name and NRIC as well.
     * @param engine {@link Engine} instance.
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
     * @see HMTextChoiceType.Item#stringValue(InputHelperType, double)
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
    default Flowable<?> rxa_enterValidAgeInputs(@NotNull Engine<?> engine,
                                                @NotNull UserMode mode,
                                                boolean validBMI) {
        PlatformType platform = engine.platform();
        UnitSystem unit = CollectionUtil.randomElement(UnitSystem.values());
        Gender gender = CollectionUtil.randomElement(Gender.values());
        Ethnicity ethnicity = CollectionUtil.randomElement(Ethnicity.values());
        CoachPref coachPref = CollectionUtil.randomElement(CoachPref.values());
        ChoiceInput cHeight = ChoiceInput.HEIGHT;
        ChoiceInput cWeight = ChoiceInput.WEIGHT;
        ChoiceInput cEthnicity = ChoiceInput.ETHNICITY;
        ChoiceInput cCoach = ChoiceInput.COACH_PREF;

        List<Zip<Height,String>> height;
        List<Zip<Weight,String>> weight;
        BMIParam param;

        /* Keep randomizing until BMI falls out of healthy range, otherwise
         * the BMI check will fail */
        do {
            height = Height.random(platform, mode, unit);
            weight = Weight.random(platform, mode, unit);

            param = BMIParam.builder()
                .withEthnicity(ethnicity)
                .withGender(gender)
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

        Height hMode = height.get(0).A;
        Weight wMode = weight.get(0).A;

        return Flowable
            .concatArray(
                rxa_randomInputs(engine, mode.validAgeInfo(platform)),

                /* Select gender */
                rxa_selectGender(engine, gender),

                /* Select unit system and height */
                rxa_selectUnitSystemPicker(engine, cHeight, hMode),
                rxa_selectChoice(engine, height),
                rxa_confirmNumericChoice(engine),

                /* Select unit system and weight */
                rxa_selectUnitSystemPicker(engine, cWeight, wMode),
                rxa_selectChoice(engine, weight),
                rxa_confirmNumericChoice(engine),

                /* Select ethnicity */
                rxa_clickInput(engine, cEthnicity),
                rxa_selectChoice(engine, cEthnicity, ethnicity.stringValue(engine)),
                rxa_confirmTextChoice(engine),

                /* Select coach preference */
                rxa_clickInput(engine, cCoach),
                rxa_selectChoice(engine, cCoach, coachPref.stringValue(engine)),
                rxa_confirmTextChoice(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
    }

    /**
     * Enter and confirm valid age inputs.
     * @param engine {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @param validBMI {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_navigateBackOnce()
     * @see ObjectUtil#nonNull(Object)
     * @see #rxa_enterValidAgeInputs(Engine, UserMode, boolean)
     * @see #rxa_scrollToBottom(Engine)
     * @see #rxa_confirmValidAgeInputs(Engine)
     * @see #rxv_dialogBlockingScreen(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completeValidAgeInputs(@NotNull Engine<?> engine,
                                                   @NotNull UserMode mode,
                                                   boolean validBMI) {
        return Flowable
            .concatArray(
                rxa_enterValidAgeInputs(engine, mode, validBMI),
                rxa_scrollToBottom(engine),
                rxv_dialogBlockingScreen(engine).flatMap(a -> {
                    if (a) {
                        return engine.rxa_navigateBackOnce();
                    } else {
                        return Flowable.just(true);
                    }
                }),
                rxa_confirmValidAgeInputs(engine)
            )
            .all(ObjectUtil::nonNull)
            .toFlowable();
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
