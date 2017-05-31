package com.holmusk.SuperLeapQA.ui.validage;

import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.model.type.SLChoiceInputType;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLNumericChoiceInputType;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.swiften.javautilities.collection.CollectionTestUtil;
import org.swiften.javautilities.collection.Zip;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.android.AndroidEngine;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by haipham on 23/5/17.
 */
public interface UIValidAgeTestType extends UIBaseTestType, ValidAgeTestHelperType {
    /**
     * This test validates that {@link Screen#VALID_AGE} contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility,
     * @param MODE {@link UserMode} instance.
     * @see Screen#VALID_AGE
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_clickInputField(Engine, SLInputType)
     * @see #rxa_selectChoice(Engine, List)
     * @see #rxv_hasValue(Engine, SLInputType, String)
     * @see #rxa_selectUnitSystemPicker(Engine, SLChoiceInputType, SLNumericChoiceInputType)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider",
        groups = "ValidateScreen"
    )
    default void test_validAgeInputs_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UIValidAgeTestType THIS = this;
        final Engine<?> E = engine();
        PlatformType p = E.platform();
        UnitSystem metric = UnitSystem.METRIC;
        UnitSystem imperial = UnitSystem.IMPERIAL;
        final ChoiceInput C_HEIGHT = ChoiceInput.HEIGHT;
        final ChoiceInput C_WEIGHT = ChoiceInput.WEIGHT;
        final ChoiceInput C_ETH = ChoiceInput.ETHNICITY;
        final ChoiceInput C_COACH = ChoiceInput.COACH_PREF;
        final List<Zip<Height,String>> HEIGHT_M = Height.random(p, MODE, metric);
        final List<Zip<Height,String>> HEIGHT_I = Height.random(p, MODE, imperial);
        final List<Zip<Weight,String>> WEIGHT_M = Weight.random(p, MODE, metric);
        final List<Zip<Weight,String>> WEIGHT_I = Weight.random(p, MODE, imperial);
        final String HEIGHT_M_STR = Height.stringValue(p, metric, HEIGHT_M);
        final String HEIGHT_I_STR = Height.stringValue(p, imperial, HEIGHT_I);
        final String WEIGHT_M_STR = Weight.stringValue(p, metric, WEIGHT_M);
        final String WEIGHT_I_STR = Weight.stringValue(p, imperial, WEIGHT_I);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.VALID_AGE)
            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_HEIGHT, Height.CM))
            .flatMap(a -> THIS.rxa_selectChoice(E, HEIGHT_M))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))
            .flatMap(a -> THIS.rxv_hasValue(E, C_HEIGHT, HEIGHT_M_STR))

            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_HEIGHT, Height.FT))
            .flatMap(a -> THIS.rxa_selectChoice(E, HEIGHT_I))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))
            .flatMap(a -> THIS.rxv_hasValue(E, C_HEIGHT, HEIGHT_I_STR))

            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_WEIGHT, Weight.KG))
            .flatMap(a -> THIS.rxa_selectChoice(E, WEIGHT_M))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))
            .flatMap(a -> THIS.rxv_hasValue(E, C_WEIGHT, WEIGHT_M_STR))

            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_WEIGHT, Weight.LB))
            .flatMap(a -> THIS.rxa_selectChoice(E, WEIGHT_I))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))
            .flatMap(a -> THIS.rxv_hasValue(E, C_WEIGHT, WEIGHT_I_STR))

            .flatMap(a -> THIS.rxa_clickInputField(E, Gender.MALE))
            .flatMap(a -> THIS.rxa_clickInputField(E, Gender.FEMALE))
            .flatMap(a -> THIS.rxa_clickInputField(E, C_ETH))
            .flatMap(a -> THIS.rxa_selectChoice(E, C_ETH, ETH.stringValue()))
            .flatMap(a -> THIS.rxa_clickInputField(E, C_COACH))
            .flatMap(a -> THIS.rxa_selectChoice(E, C_COACH, CP.stringValue()))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Confirm that when the user selects
     * {@link ChoiceInput#HEIGHT} in
     * {@link com.holmusk.SuperLeapQA.model.Height#FT}, every 12
     * {@link com.holmusk.SuperLeapQA.model.Height#INCH} is converted to
     * {@link com.holmusk.SuperLeapQA.model.Height#FT}.
     * @param MODE {@link UserMode} instance.
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rx_h_inchToFootRecursive(Engine, UserMode)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_12Inch_shouldBeConvertedTo1Foot(@NotNull final UserMode MODE) {
        // Setup
        final UIValidAgeTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.VALID_AGE)
            .flatMap(a -> THIS.rx_h_inchToFootRecursive(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the {@link Screen#VALID_AGE} inputs show
     * the correct empty input errors, by sequentially entering/selecting
     * inputs and clicking the confirm button. If the inputs are not completed,
     * the user will be notified.
     * @param M {@link UserMode} instance.
     * @see Screen#VALID_AGE
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see Height#randomValue(UserMode)
     * @see Weight#randomValue(UserMode)
     * @see #rxa_clickInputField(Engine, SLInputType)
     * @see #rxa_selectChoice(Engine, SLChoiceInputType, String)
     * @see #rxa_confirmNumericChoice(Engine)
     * @see #rxa_confirmTextChoice(Engine)
     * @see #generalUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_validAgeEmptyInputs_showsCorrectErrors(@NotNull final UserMode M) {
        // Setup
        final UIValidAgeTestType THIS = this;
        final Engine<?> E = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        PlatformType p = E.platform();
        final Gender GENDER = CollectionTestUtil.randomElement(Gender.values());
        final List<Zip<Height,String>> HEIGHT_M = Height.random(p, M, UnitSystem.METRIC);
        final List<Zip<Height,String>> HEIGHT_I = Height.random(p, M, UnitSystem.IMPERIAL);
        final List<Zip<Weight,String>> WEIGHT_M = Weight.random(p, M, UnitSystem.METRIC);
        final List<Zip<Weight,String>> WEIGHT_I = Weight.random(p, M, UnitSystem.IMPERIAL);
        final Ethnicity ETH = CollectionTestUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionTestUtil.randomElement(CoachPref.values());

        // When
        Flowable.just(E)
            /* If we are testing on iOS, there is not need to check for empty
             * error messages, since the confirm button is not enabled until
             * all inputs are filled */
            .filter(a -> a instanceof AndroidEngine)
            .flatMap(a -> rxa_navigate(M, Screen.SPLASH, Screen.VALID_AGE))

            /* At this stage, the gender error message should be shown */
            .flatMap(a -> THIS.rxa_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rxv_isShowingError(E, GENDER.emptyInputError(M)))
            .flatMap(a -> THIS.rxa_clickInputField(E, GENDER))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rxa_clickInputField(E, Height.CM))
            .flatMap(a -> THIS.rxa_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rxv_isShowingError(E, Height.CM.emptyInputError(M)))
            .flatMap(a -> THIS.rxa_selectChoice(E, HEIGHT_M))

            /* At this stage, the height error message should be shown */
            .flatMap(a -> THIS.rxa_clickInputField(E, Height.FT))
            .flatMap(a -> THIS.rxa_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rxv_isShowingError(E, Height.FT.emptyInputError(M)))
            .flatMap(a -> THIS.rxa_selectChoice(E, HEIGHT_I))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rxa_clickInputField(E, Weight.KG))
            .flatMap(a -> THIS.rxa_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rxv_isShowingError(E, Weight.KG.emptyInputError(M)))
            .flatMap(a -> THIS.rxa_selectChoice(E, WEIGHT_M))

            /* At this stage, the weight error message should be shown */
            .flatMap(a -> THIS.rxa_clickInputField(E, Weight.LB))
            .flatMap(a -> THIS.rxa_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rxv_isShowingError(E, Weight.LB.emptyInputError(M)))
            .flatMap(a -> THIS.rxa_selectChoice(E, WEIGHT_I))

            /* At this stage, the ethnicity error message should be shown */
            .flatMap(a -> THIS.rxa_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rxv_isShowingError(E, ChoiceInput.ETHNICITY.emptyInputError(M)))
            .flatMap(a -> THIS.rxa_clickInputField(E, ChoiceInput.ETHNICITY))
            .flatMap(a -> THIS.rxa_selectChoice(E, ChoiceInput.ETHNICITY, ETH.value()))
            .flatMap(a -> THIS.rxa_confirmTextChoice(E))

            /* At this stage, the coach pref error message should be shown */
            .flatMap(a -> THIS.rxa_confirmValidAgeInputs(E))
            .flatMap(a -> THIS.rxv_isShowingError(E, ChoiceInput.COACH_PREF.emptyInputError(M)))
            .flatMap(a -> THIS.rxa_clickInputField(E, ChoiceInput.COACH_PREF))
            .flatMap(a -> THIS.rxa_selectChoice(E, ChoiceInput.COACH_PREF, CP.value()))
            .flatMap(a -> THIS.rxa_confirmTextChoice(E))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
