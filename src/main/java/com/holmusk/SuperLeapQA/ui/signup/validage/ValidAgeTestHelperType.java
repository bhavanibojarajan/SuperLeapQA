package com.holmusk.SuperLeapQA.ui.signup.validage;

import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.type.SLChoiceInputType;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.Zipped;
import org.swiften.javautilities.localizer.LCFormat;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.mobile.ios.IOSEngine;

import java.util.Arrays;
import java.util.List;

/**
 * Created by haipham on 19/5/17.
 */
public interface ValidAgeTestHelperType extends ValidAgeActionType {
    /**
     * Enter random inputs for acceptable age screen, assuming the user is
     * already in the acceptable age input screen.
     * @param E {@link Engine} instance.
     * @param m {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_clickInputField(Engine, SLInputType)
     * @see #rx_a_selectChoice(Engine, List)
     * @see #rx_v_editFieldHasValue(Engine, SLInputType, String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_h_enterAndCheckValidAgeInputs(@NotNull final Engine<?> E,
                                                         @NotNull UserMode m) {
        PlatformType p = E.platform();
        final ValidAgeActionType THIS = this;
        final List<Zipped<Height,String>> HEIGHT_M = Height.random(p, m, UnitSystem.METRIC);
        final List<Zipped<Height,String>> HEIGHT_I = Height.random(p, m, UnitSystem.IMPERIAL);
        final List<Zipped<Weight,String>> WEIGHT_M = Weight.random(p, m, UnitSystem.METRIC);
        final List<Zipped<Weight,String>> WEIGHT_I = Weight.random(p, m, UnitSystem.IMPERIAL);
//        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
//        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());
        final Ethnicity ETH = Ethnicity.WHITE_NON_HISPANIC;
        final CoachPref CP = CoachPref.FEMALE;

        return rx_a_clickInputField(E, Gender.MALE)
            .flatMap(a -> THIS.rx_a_clickInputField(E, Gender.FEMALE))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.ETHNICITY))
            .flatMap(a -> THIS.rx_a_selectChoice(E, ChoiceInput.ETHNICITY, ETH.stringValue()))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.COACH_PREF))
            .flatMap(a -> THIS.rx_a_selectChoice(E, ChoiceInput.COACH_PREF, CP.stringValue()))

            .flatMap(a -> THIS.rx_a_clickInputField(E, Height.CM))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_a_selectChoice(E, HEIGHT_M))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> THIS.rx_a_clickInputField(E, Height.FT))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_a_selectChoice(E, HEIGHT_I))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> THIS.rx_a_clickInputField(E, Weight.KG))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rx_a_selectChoice(E, WEIGHT_M))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E))

            .flatMap(a -> THIS.rx_a_clickInputField(E, Weight.LB))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rx_a_selectChoice(E, WEIGHT_I))
            .flatMap(a -> THIS.rx_a_confirmNumericChoice(E));
    }

    /**
     * Sequentially validate error messages due to empty inputs (refer to
     * {@link TextInput} and {@link ChoiceInput}, assuming the user is
     * already in the acceptable age input screen.
     * @param E {@link Engine} instance.
     * @param M {@link UserMode} instance.
     * @return {@link Flowable} instance.
     * @see #rx_a_confirmValidAgeInputs(Engine)
     * @see #rx_isShowingError(Engine, LCFormat)
     * @see #rx_a_clickInputField(Engine, SLInputType)
     * @see #rx_a_selectChoice(Engine, SLChoiceInputType, String)
     * @see #rx_a_selectChoice(Engine, List)
     * @see #rx_a_confirmValidAgeInputs(Engine)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<?> rx_h_validAgeEmptyInputErrors(@NotNull final Engine<?> E,
                                                      @NotNull final UserMode M) {
        /* If we are testing on iOS, there is not need to check for empty
         * error messages, since the confirm button is not enabled until
         * all inputs are filled */
        if (E instanceof IOSEngine) return Flowable.just(true);
        final ValidAgeActionType THIS = this;
        PlatformType p = E.platform();

        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final List<Zipped<Height,String>> HEIGHT_M = Height.random(p, M, UnitSystem.METRIC);
        final List<Zipped<Height,String>> HEIGHT_I = Height.random(p, M, UnitSystem.IMPERIAL);
        final List<Zipped<Weight,String>> WEIGHT_M = Weight.random(p, M, UnitSystem.METRIC);
        final List<Zipped<Weight,String>> WEIGHT_I = Weight.random(p, M, UnitSystem.IMPERIAL);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        /* At this stage, the gender error message should be shown */
        return rx_a_confirmValidAgeInputs(E)
            .flatMap(a -> THIS.rx_isShowingError(E, GENDER.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_clickInputField(E, GENDER))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rx_a_clickInputField(E, Height.CM))
            .flatMap(a -> THIS.rx_a_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rx_isShowingError(E, Height.CM.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_selectChoice(E, HEIGHT_M))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rx_a_clickInputField(E, Height.FT))
            .flatMap(a -> THIS.rx_a_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rx_isShowingError(E, Height.FT.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_selectChoice(E, HEIGHT_I))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rx_a_clickInputField(E, Weight.KG))
            .flatMap(a -> THIS.rx_a_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rx_isShowingError(E, Weight.KG.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_selectChoice(E, WEIGHT_M))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rx_a_clickInputField(E, Weight.LB))
            .flatMap(a -> THIS.rx_a_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rx_isShowingError(E, Weight.LB.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_selectChoice(E, WEIGHT_I))

            /* At this stage, the ethnicity error message should be shown */
            .flatMap(a -> THIS.rx_a_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rx_isShowingError(E, ChoiceInput.ETHNICITY.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.ETHNICITY))
            .flatMap(a -> THIS.rx_a_selectChoice(E, ChoiceInput.ETHNICITY, ETH.value()))

            /* At this stage, the coach pref error message should be shown */
            .flatMap(a -> THIS.rx_a_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rx_isShowingError(E, ChoiceInput.COACH_PREF.emptyInputError(M)))
            .flatMap(a -> THIS.rx_a_clickInputField(E, ChoiceInput.COACH_PREF))
            .flatMap(a -> THIS.rx_a_selectChoice(E, ChoiceInput.COACH_PREF, CP.value()));
    }

    /**
     * Confirm that 12 {@link Height#INCH} is converted to {@link Height#FT}
     * when we are picking {@link ChoiceInput#HEIGHT}, assuming the user
     * is already in the acceptable age screen.
     * @param ENGINE {@link Engine} instance.
     * @param FT {@link Height#FT} value to be selected.
     * @return {@link Flowable} instance.
     * @see Height#stringValue(PlatformType, UnitSystem, List)
     * @see #rx_a_selectChoice(Engine, List)
     * @see #rx_a_confirmNumericChoice(Engine)
     * @see #rx_v_editFieldHasValue(Engine, SLInputType, String)
     */
    @NotNull
    default Flowable<?> rx_h_checkInchToFoot(@NotNull final Engine<?> ENGINE, final int FT) {
        final ValidAgeTestHelperType THIS = this;
        PlatformType platform = ENGINE.platform();

        final List<Zipped<Height,String>> INPUTS = Arrays.asList(
            new Zipped<>(Height.FT, String.valueOf(FT)),
            new Zipped<>(Height.INCH, String.valueOf(0))
        );

        final String STR = Height.stringValue(platform, UnitSystem.IMPERIAL, INPUTS);

        return rx_a_clickInputField(ENGINE, Height.FT)
            .flatMap(a -> THIS.rx_a_clickInputField(ENGINE, ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_a_selectChoice(ENGINE, Height.FT, String.valueOf(FT)))
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
     * @see #rx_h_checkInchToFoot(Engine, int)
     */
    @NotNull
    default Flowable<?> rx_h_checkInchToFootRecursive(@NotNull final Engine<?> ENGINE,
                                                      @NotNull final UserMode MODE) {
        final ValidAgeTestHelperType THIS = this;
        final List<Integer> SELECTABLE = Height.FT.selectableNumericRange(MODE);
        final int LENGTH = SELECTABLE.size();

        class Repeater {
            @NotNull
            private Flowable<?> repeat(final int INDEX) {
                if (INDEX < LENGTH) {
                    return THIS
                        .rx_h_checkInchToFoot(ENGINE, SELECTABLE.get(INDEX))
                        .flatMap(a -> new Repeater().repeat(INDEX + 1));
                } else {
                    return Flowable.just(true);
                }
            }
        }

        return new Repeater().repeat(0);
    }
}
