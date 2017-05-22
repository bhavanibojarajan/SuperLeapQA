package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.Zipped;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.ios.IOSEngine;

import java.util.Arrays;
import java.util.List;

/**
 * Created by haipham on 19/5/17.
 */
public interface AcceptableAgeTestHelperType extends AcceptableAgeActionType {
    /**
     * Enter random inputs for acceptable age screen, assuming the user is
     * already in the acceptable age input screen.
     * @param E {@link Engine} instance.
     * @param m {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_clickInputField(Engine, SLInputType)
     * @see #rx_a_selectNumericInput(Engine, List)
     * @see #rx_v_editFieldHasValue(Engine, SLInputType, String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_h_enterAndCheckAcceptableAgeInputs(@NotNull final Engine<?> E,
                                                              @NotNull UserMode m) {
        PlatformType p = E.platform();
        final AcceptableAgeActionType THIS = this;
        final List<Zipped<Height,Double>> HEIGHT_M = Height.random(p, m, UnitSystem.METRIC);
        final List<Zipped<Height,Double>> HEIGHT_I = Height.random(p, m, UnitSystem.IMPERIAL);
        final List<Zipped<Weight,Double>> WEIGHT_M = Weight.random(p, m, UnitSystem.METRIC);
        final List<Zipped<Weight,Double>> WEIGHT_I = Weight.random(p, m, UnitSystem.IMPERIAL);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        return rx_a_clickInputField(E, Gender.MALE)
            .flatMap(a -> THIS.rx_a_clickInputField(E, Gender.FEMALE))
            .flatMap(a -> THIS.rx_a_selectEthnicity(E, ETH))
            .flatMap(a -> THIS.rx_a_selectCoachPref(E, CP))

            .flatMap(a -> THIS.rx_a_clickInputField(E, Height.CM))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_a_selectNumericInput(E, HEIGHT_M))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> THIS.rx_a_clickInputField(E, Height.FT))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_a_selectNumericInput(E, HEIGHT_I))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> THIS.rx_a_clickInputField(E, Weight.KG))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rx_a_selectNumericInput(E, WEIGHT_M))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> THIS.rx_a_clickInputField(E, Weight.LB))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rx_a_selectNumericInput(E, WEIGHT_I))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E));
    }

    /**
     * Sequentially validate error messages due to empty inputs (refer to
     * {@link TextInput} and {@link ChoiceInput}, assuming the user is
     * already in the acceptable age input screen.
     * @param E {@link Engine} instance.
     * @param M {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_confirmAcceptableAgeInputs(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_h_acceptableAgeEmptyInputErrors(@NotNull final Engine<?> E,
                                                           @NotNull final UserMode M) {
        /* If we are testing on iOS, there is not need to check for empty
         * error messages, since the confirm button is not enabled until
         * all inputs are filled */
        if (E instanceof IOSEngine) return Flowable.just(true);
        final AcceptableAgeActionType THIS = this;
        PlatformType p = E.platform();

        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final List<Zipped<Height,Double>> HEIGHT_M = Height.random(p, M, UnitSystem.METRIC);
        final List<Zipped<Height,Double>> HEIGHT_I = Height.random(p, M, UnitSystem.IMPERIAL);
        final List<Zipped<Weight,Double>> WEIGHT_M = Weight.random(p, M, UnitSystem.METRIC);
        final List<Zipped<Weight,Double>> WEIGHT_I = Weight.random(p, M, UnitSystem.IMPERIAL);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        /* At this stage, the gender error message should be shown */
        return rx_a_confirmAcceptableAgeInputs(E)
            .flatMap(a -> THIS.rxIsShowingError(E, GENDER.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_clickInputField(E, GENDER))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rx_a_clickInputField(E, Height.CM))
            .flatMap(a -> THIS.rx_a_confirmAcceptableAgeInputs(E))
            .flatMap(a -> THIS.rxIsShowingError(E, Height.CM.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_selectNumericInput(E, HEIGHT_M))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rx_a_clickInputField(E, Height.FT))
            .flatMap(a -> THIS.rx_a_confirmAcceptableAgeInputs(E))
            .flatMap(a -> THIS.rxIsShowingError(E, Height.FT.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_selectNumericInput(E, HEIGHT_I))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rx_a_clickInputField(E, Weight.KG))
            .flatMap(a -> THIS.rx_a_confirmAcceptableAgeInputs(E))
            .flatMap(a -> THIS.rxIsShowingError(E, Weight.KG.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_selectNumericInput(E, WEIGHT_M))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rx_a_clickInputField(E, Weight.LB))
            .flatMap(a -> THIS.rx_a_confirmAcceptableAgeInputs(E))
            .flatMap(a -> THIS.rxIsShowingError(E, Weight.LB.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_selectNumericInput(E, WEIGHT_I))

            /* At this stage, the ethnicity error message should be shown */
            .flatMap(a -> THIS.rx_a_confirmAcceptableAgeInputs(E))
            .flatMap(a -> THIS.rxIsShowingError(E, ChoiceInput.ETHNICITY.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_selectEthnicity(E, ETH))

            /* At this stage, the coach pref error message should be shown */
            .flatMap(a -> THIS.rx_a_confirmAcceptableAgeInputs(E))
            .flatMap(a -> THIS.rxIsShowingError(E, ChoiceInput.COACH_PREF.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_selectCoachPref(E, CP));
    }

    /**
     * Confirm that 12 {@link Height#INCH} is converted to {@link Height#FT}
     * when we are picking {@link ChoiceInput#HEIGHT}, assuming the user
     * is already in the acceptable age screen.
     * @param ENGINE {@link Engine} instance.
     * @param ft {@link Height#FT} value to be selected.
     * @return {@link Flowable} instance.
     * @see Height#stringValue(PlatformType, UnitSystem, List)
     * @see #rx_a_selectNumericInput(Engine, List)
     * @see #rx_a_confirmNumericChoice(Engine)
     * @see #rx_v_editFieldHasValue(Engine, SLInputType, String)
     */
    @NotNull
    default Flowable<?> rx_h_checkInchToFoot(@NotNull final Engine<?> ENGINE, double ft) {
        final AcceptableAgeTestHelperType THIS = this;
        PlatformType platform = ENGINE.platform();

        final double INCH = 0;

        final List<Zipped<Height,Double>> INPUTS = Arrays.asList(
            new Zipped<>(Height.FT, ft),
            new Zipped<>(Height.INCH, INCH)
        );

        final String STR = Height.stringValue(platform, UnitSystem.IMPERIAL, INPUTS);

        return rx_a_clickInputField(ENGINE, Height.FT)
            .flatMap(a -> THIS.rx_a_clickInputField(ENGINE, ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_a_selectNumericInput(ENGINE, INPUTS))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(ENGINE))
            .flatMap(a -> THIS.rx_v_editFieldHasValue(ENGINE, ChoiceInput.HEIGHT, STR));
    }

    /**
     * Recursively check all {@link Height#FT} selectable values and confirm
     * that {@link Height#INCH} to {@link Height#FT} conversion works
     * correctly.
     * @param ENGINE {@link Engine} instance.
     * @param MODE {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see Height#FT
     * @see Height#selectableNumericRange(UserMode)
     * @see #rx_h_checkInchToFoot(Engine, double)
     */
    @NotNull
    default Flowable<?> rx_h_checkInchToFootRecursive(@NotNull final Engine<?> ENGINE,
                                                      @NotNull final UserMode MODE) {
        final AcceptableAgeTestHelperType THIS = this;
        final List<Double> SELECTABLE = Height.FT.selectableNumericRange(MODE);
        final int LENGTH = SELECTABLE.size();

        class Repeater {
            @NotNull
            private Flowable<?> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    double ft = SELECTABLE.get(INDEX);

                    return THIS
                        .rx_h_checkInchToFoot(ENGINE, ft)
                        .flatMap(a -> new Repeater().repeat(INDEX + 1));
                } else {
                    return Flowable.just(true);
                }
            }
        }

        return new Repeater().repeat(0);
    }
}
