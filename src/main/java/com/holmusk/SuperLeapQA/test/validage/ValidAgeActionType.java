package com.holmusk.SuperLeapQA.test.validage;

import com.holmusk.HMUITestKit.model.HMChoiceType;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextChoiceType;
import com.holmusk.HMUITestKit.model.UnitSystem;
import com.holmusk.SuperLeapQA.bmi.BMIParam;
import com.holmusk.SuperLeapQA.bmi.BMIResult;
import com.holmusk.SuperLeapQA.bmi.BMIUtil;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.test.base.BaseActionType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.functional.Tuple;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.HPReactives;
import org.swiften.javautilities.util.HPLog;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.model.InputHelperType;
import org.swiften.xtestkit.ios.IOSEngine;
import org.swiften.xtestkit.mobile.Platform;
import org.swiften.xtestkitcomponents.platform.PlatformType;

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
     * @see HPObjects#nonNull(Object)
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
                .all(HPObjects::nonNull)
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
     * @see HPObjects#nonNull(Object)
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
            return HPReactives
                .concatDelayEach(
                    generalDelay(engine),
                    rxa_clickInput(engine, numeric),
                    rxa_clickInput(engine, choice)
                )
                .all(HPObjects::nonNull)
                .toFlowable();
        } else if (engine instanceof IOSEngine) {
            return HPReactives
                .concatDelayEach(
                    generalDelay(engine),
                    rxa_clickInput(engine, choice),
                    rxa_clickInput(engine, numeric)
                )
                .all(HPObjects::nonNull)
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
     * @see HPObjects#nonNull(Object)
     * @see #validAgeInputProgressDelay(Engine)
     * @see #rxa_watchProgressBar(Engine)
     * @see #rxe_validAgeConfirm(Engine)
     */
    @NotNull
    default Flowable<?> rxa_confirmValidAgeInputs(@NotNull final Engine<?> ENGINE) {
        return Flowable.concatArray(
            rxe_validAgeConfirm(ENGINE).flatMap(ENGINE::rxa_click),
            Flowable.timer(validAgeInputProgressDelay(ENGINE), TimeUnit.MILLISECONDS),
            rxa_watchProgressBar(ENGINE)
        ).all(HPObjects::nonNull).toFlowable();
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
     * @see BMIResult#height()
     * @see BMIResult#weight()
     * @see BMIUtil#findBMIResult(PlatformType, UserMode, UnitSystem, BMIParam, boolean)
     * @see CoachPref#values()
     * @see HPIterables#randomElement(Object[])
     * @see Ethnicity#values()
     * @see Gender#values()
     * @see Height#randomValue(UserMode)
     * @see HMTextChoiceType.Item#stringValue(InputHelperType, double)
     * @see UnitSystem#values()
     * @see UserMode#validAgeInfo(PlatformType)
     * @see Weight#randomValue(UserMode)
     * @see ChoiceInput#HEIGHT
     * @see ChoiceInput#WEIGHT
     * @see com.holmusk.SuperLeapQA.navigation.Screen#PERSONAL_INFO
     * @see Tuple#A
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
        UnitSystem unit = HPIterables.randomElement(UnitSystem.values());
        Gender gender = HPIterables.randomElement(Gender.values());
        Ethnicity ethnicity = HPIterables.randomElement(Ethnicity.values());
        CoachPref coachPref = HPIterables.randomElement(CoachPref.values());
        ChoiceInput cHeight = ChoiceInput.HEIGHT;
        ChoiceInput cWeight = ChoiceInput.WEIGHT;
        ChoiceInput cEthnicity = ChoiceInput.ETHNICITY;
        ChoiceInput cCoach = ChoiceInput.COACH_PREF;

        BMIParam param = BMIParam.builder().withEthnicity(ethnicity).withGender(gender).build();
        BMIResult result = BMIUtil.findBMIResult(platform, mode, unit, param, validBMI);
        List<Tuple<Height,String>> height = result.height();
        List<Tuple<Weight,String>> weight = result.weight();
        Height hMode = height.get(0).A;
        Weight wMode = weight.get(0).A;

        HPLog.printft("Selecting height: %s, weight: %s", height, weight);

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
            .all(HPObjects::nonNull)
            .toFlowable();
    }

    /**
     * Enter and confirm valid age inputs.
     * @param ENGINE {@link Engine} instance.
     * @param mode {@link UserMode} instance.
     * @param validBMI {@link Boolean} value.
     * @return {@link Flowable} instance.
     * @see Engine#rxa_navigateBackOnce()
     * @see HPObjects#nonNull(Object)
     * @see #rxa_enterValidAgeInputs(Engine, UserMode, boolean)
     * @see #rxa_scrollToBottom(Engine)
     * @see #rxa_confirmValidAgeInputs(Engine)
     * @see #rxv_dialogBlockingScreen(Engine)
     */
    @NotNull
    default Flowable<?> rxa_completeValidAgeInputs(@NotNull final Engine<?> ENGINE,
                                                   @NotNull UserMode mode,
                                                   boolean validBMI) {
        return Flowable
            .concatArray(
                rxa_enterValidAgeInputs(ENGINE, mode, validBMI),
                rxa_scrollToBottom(ENGINE),
                rxv_dialogBlockingScreen(ENGINE)
                    .flatMap(a -> {
                        if (a) {
                            return ENGINE.rxa_navigateBackOnce();
                        } else {
                            return Flowable.just(true);
                        }
                    }),
                rxa_confirmValidAgeInputs(ENGINE)
            )
            .all(HPObjects::nonNull)
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
