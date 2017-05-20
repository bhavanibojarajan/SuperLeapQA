package com.holmusk.SuperLeapQA.ui.signup.main;

import com.holmusk.SuperLeapQA.model.*;
import io.reactivex.Flowable;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.Zipped;
import org.swiften.javautilities.log.LogUtil;
import org.swiften.xtestkit.base.BaseEngine;
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
     * @param mode A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_clickInputField(SLInputType)
     * @see #rx_selectNumericInput(List)
     * @see #rx_editFieldHasValue(SLInputType, String)
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rxEnterAndValidateAcceptableAgeInputs(@NotNull UserMode mode) {
        PlatformType platform = engine().platform();
        final AcceptableAgeActionType THIS = this;
        final List<Zipped<Height,Double>> HEIGHT_M = Height.random(platform, mode, UnitSystem.METRIC);
        final List<Zipped<Height,Double>> HEIGHT_I = Height.random(platform, mode, UnitSystem.IMPERIAL);
        final List<Zipped<Weight,Double>> WEIGHT_M = Weight.random(platform, mode, UnitSystem.METRIC);
        final List<Zipped<Weight,Double>> WEIGHT_I = Weight.random(platform, mode, UnitSystem.IMPERIAL);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        return rx_clickInputField(Gender.MALE)
            .flatMap(a -> THIS.rx_clickInputField(Gender.FEMALE))
            .flatMap(a -> THIS.rx_selectEthnicity(ETH))
            .flatMap(a -> THIS.rx_selectCoachPref(CP))

            .flatMap(a -> THIS.rx_clickInputField(Height.CM))
            .flatMap(a -> THIS.rx_clickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_selectNumericInput(HEIGHT_M))
            .flatMap(a -> THIS.rx_confirmNumericChoiceInput())

            .flatMap(a -> THIS.rx_clickInputField(Height.FT))
            .flatMap(a -> THIS.rx_clickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_selectNumericInput(HEIGHT_I))
            .flatMap(a -> THIS.rx_confirmNumericChoiceInput())

            .flatMap(a -> THIS.rx_clickInputField(Weight.KG))
            .flatMap(a -> THIS.rx_clickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rx_selectNumericInput(WEIGHT_M))
            .flatMap(a -> THIS.rx_confirmNumericChoiceInput())

            .flatMap(a -> THIS.rx_clickInputField(Weight.LB))
            .flatMap(a -> THIS.rx_clickInputField(ChoiceInput.WEIGHT))
            .flatMap(a -> THIS.rx_selectNumericInput(WEIGHT_I))
            .flatMap(a -> THIS.rx_confirmNumericChoiceInput());
    }

    /**
     * Sequentially validate error messages due to empty inputs (refer to
     * {@link TextInput} and {@link ChoiceInput}, assuming the user is
     * already in the acceptable age input screen.
     * @param MODE A {@link UserMode} instance.
     * @return A {@link Flowable} instance.
     * @see #rx_confirmAcceptableAgeInputs()
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default Flowable<Boolean> rx_validateAcceptableAgeEmptyInputErrors(@NotNull final UserMode MODE) {
        final BaseEngine<?> ENGINE = engine();

        /* If we are testing on iOS, there is not need to check for empty
         * error messages, since the confirm button is not enabled until
         * all inputs are filled */
        if (ENGINE instanceof IOSEngine) {
            return Flowable.just(true);
        }

        final AcceptableAgeActionType THIS = this;
        PlatformType platform = ENGINE.platform();

        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final List<Zipped<Height,Double>> HEIGHT_M = Height.random(platform, MODE, UnitSystem.METRIC);
        final List<Zipped<Height,Double>> HEIGHT_I = Height.random(platform, MODE, UnitSystem.IMPERIAL);
        final List<Zipped<Weight,Double>> WEIGHT_M = Weight.random(platform, MODE, UnitSystem.METRIC);
        final List<Zipped<Weight,Double>> WEIGHT_I = Weight.random(platform, MODE, UnitSystem.IMPERIAL);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        /* At this stage, the gender error message should be shown */
        return rx_confirmAcceptableAgeInputs()
            .flatMap(a -> THIS.rxIsShowingError(GENDER.emptyInputError(MODE)))
            .flatMap(a -> THIS.rx_clickInputField(GENDER))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rx_clickInputField(Height.CM))
            .flatMap(a -> THIS.rx_confirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Height.CM.emptyInputError(MODE)))
            .flatMap(a -> THIS.rx_selectNumericInput(HEIGHT_M))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rx_clickInputField(Height.FT))
            .flatMap(a -> THIS.rx_confirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Height.FT.emptyInputError(MODE)))
            .flatMap(a -> THIS.rx_selectNumericInput(HEIGHT_I))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rx_clickInputField(Weight.KG))
            .flatMap(a -> THIS.rx_confirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Weight.KG.emptyInputError(MODE)))
            .flatMap(a -> THIS.rx_selectNumericInput(WEIGHT_M))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rx_clickInputField(Weight.LB))
            .flatMap(a -> THIS.rx_confirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(Weight.LB.emptyInputError(MODE)))
            .flatMap(a -> THIS.rx_selectNumericInput(WEIGHT_I))

            /* At this stage, the ethnicity error message should be shown */
            .flatMap(a -> THIS.rx_confirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(ChoiceInput.ETHNICITY.emptyInputError(MODE)))
            .flatMap(a -> THIS.rx_selectEthnicity(ETH))

            /* At this stage, the coach pref error message should be shown */
            .flatMap(a -> THIS.rx_confirmAcceptableAgeInputs())
            .flatMap(a -> THIS.rxIsShowingError(ChoiceInput.COACH_PREF.emptyInputError(MODE)))
            .flatMap(a -> THIS.rx_selectCoachPref(CP));
    }

    /**
     * Confirm that 12 {@link Height#INCH} is converted to a {@link Height#FT}
     * when we are picking {@link ChoiceInput#HEIGHT}, assuming the user
     * is already in the acceptable age screen.
     * @return A {@link Flowable} instance.
     * @see Height#stringValue(PlatformType, UnitSystem, List)
     * @see #rx_selectNumericInput(List)
     * @see #rx_confirmNumericChoiceInput()
     * @see #rx_editFieldHasValue(SLInputType, String)
     */
    @NotNull
    default Flowable<Boolean> rx_validate12InchConvertedToAFoot(@NotNull UserMode mode) {
        PlatformType platform = engine().platform();
        final AcceptableAgeTestHelperType THIS = this;

        final double FT = Math.max(
            Height.FT.randomValue(mode),
            Height.FT.minSelectableNumericValue(mode) + 1
        );

        final double INCH = 0;

        final List<Zipped<Height,Double>> INPUTS = Arrays.asList(
            new Zipped<>(Height.FT, FT),
            new Zipped<>(Height.INCH, INCH)
        );

        final String STRING_VAL = Height.stringValue(platform, UnitSystem.IMPERIAL, INPUTS);

        LogUtil.println(INPUTS);

        return rx_clickInputField(Height.FT)
            .flatMap(a -> THIS.rx_clickInputField(ChoiceInput.HEIGHT))
            .flatMap(a -> THIS.rx_selectNumericInput(INPUTS))
            .flatMap(a -> THIS.rx_confirmNumericChoiceInput())
            .flatMap(a -> THIS.rx_editFieldHasValue(ChoiceInput.HEIGHT, STRING_VAL));
    }
}
