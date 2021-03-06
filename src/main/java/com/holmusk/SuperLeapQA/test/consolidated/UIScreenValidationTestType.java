package com.holmusk.SuperLeapQA.test.consolidated;

import com.holmusk.HMUITestKit.model.*;
import com.holmusk.SuperLeapQA.model.*;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.navigation.type.BackwardNavigationType;
import com.holmusk.SuperLeapQA.navigation.type.ForwardNavigationType;
import com.holmusk.SuperLeapQA.test.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.test.personalinfo.UIPersonalInfoTestType;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.HPBooleans;
import org.swiften.javautilities.collection.HPIterables;
import org.swiften.javautilities.functional.Tuple;
import org.swiften.javautilities.object.HPObjects;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by haipham on 6/4/17.
 */
public interface UIScreenValidationTestType extends
    UIBaseTestType,
    ForwardNavigationType,
    BackwardNavigationType
{
    /**
     * Check that {@link Screen#LOGIN} has valid
     * {@link org.openqa.selenium.WebElement}, by checking their visibility
     * and interacting with them.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_loginScreen(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_loginPage_isValidScreen() {
        // Setup
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN),
            rxv_loginScreen(ENGINE)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Verify that {@link Screen#FORGOT_PASSWORD} has valid
     * {@link org.openqa.selenium.WebElement} by checking their visibility
     * and interacting with them.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_forgotPassword(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_forgotPassword_isValidScreen() {
        // Setup
        Engine<?> engine = engine();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.FORGOT_PASSWORD),
            rxv_forgotPassword(engine)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#REGISTER} contains the correct
     * {@link org.openqa.selenium.WebElement} by checking their visibility,
     * and then navigate back once to check {@link Screen#WELCOME}.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_clickBackButton(Engine)
     * @see #rxv_registerScreen(Engine)
     * @see #rxv_welcomeScreen(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_register_isValidScreen() {
        // Setup
        Engine<?> engine = engine();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.REGISTER),
            rxv_registerScreen(engine),
            rxa_clickBackButton(engine),
            rxv_welcomeScreen(engine)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that {@link Screen#DOB} has correct elements,
     * by checking that all {@link org.openqa.selenium.WebElement} are present.
     * @param mode {@link UserMode} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalDelay(Engine)
     * @see #generalUserModeProvider()
     * @see #rxa_clickBackButton(Engine)
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_openDoBPicker(Engine)
     * @see #rxv_registerScreen(Engine)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_DoBPicker_isValidScreen(@NotNull UserMode mode) {
        // Setup
        Engine<?> engine = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.DOB),

            Flowable.mergeArray(
                engine.rxe_containsText("register_title_dateOfBirth"),
                engine.rxe_containsText(
                    "parentSignUp_title_whatIsYourChild",
                    "teenSignUp_title_whatIsYour"
                )
            ).all(HPObjects::nonNull).toFlowable(),

            rxa_openDoBPicker(engine),
            engine.rxa_navigateBackOnce()
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#INVALID_AGE} has the
     * correct {@link org.openqa.selenium.WebElement}, and clicking on the
     * submit button without filling in required inputs, and check that
     * it should fail.
     * @param mode {@link UserMode} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_confirmInvalidAgeInputs(Engine)
     * @see #rxa_clickInput(Engine, HMInputType)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_invalidAgeInputs_isValidScreen(@NotNull UserMode mode) {
        // Setup
        Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.INVALID_AGE),
            rxa_confirmInvalidAgeInputs(ENGINE),
            rxa_clickInput(ENGINE, TextInput.NAME)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#VALID_AGE} contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility,
     * The procedures here mimic
     * {@link #rxa_enterValidAgeInputs(Engine, UserMode, boolean)} in lots of ways,
     * but this is intentional because we want to check for numeric formatting
     * for {@link ChoiceInput#HEIGHT} and {@link ChoiceInput#WEIGHT} choice
     * selection.
     * Be aware that this test does not take into account BMI calculations.
     * @param MODE {@link UserMode} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_selectGender(Engine, Gender)
     * @see #rxa_clickInput(Engine, HMInputType)
     * @see #rxa_selectChoice(Engine, List)
     * @see #rxa_selectUnitSystemPicker(Engine, HMChoiceType, SLNumericChoiceType)
     * @see #rxa_confirmNumericChoice(Engine)
     * @see #rxa_confirmTextChoice(Engine)
     * @see #rxv_fieldHasValue(Engine, HMInputType, String)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_validAgeInputs_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        Engine<?> engine = engine();
        PlatformType platform = engine.platform();
        UnitSystem metric = UnitSystem.METRIC;
        UnitSystem imperial = UnitSystem.IMPERIAL;
        List<HMTextType> inputs = MODE.validAgeInfo(platform);
        List<Tuple<Height,String>> heightM = Height.random(platform, MODE, metric);
        List<Tuple<Height,String>> heightI = Height.random(platform, MODE, imperial);
        List<Tuple<Weight,String>> weightM = Weight.random(platform, MODE, metric);
        List<Tuple<Weight,String>> weightI = Weight.random(platform, MODE, imperial);
        String heightMStr = Height.stringValue(platform, metric, heightM);
        String heightIStr = Height.stringValue(platform, imperial, heightI);
        String weightMStr = Weight.stringValue(platform, metric, weightM);
        String weightIStr = Weight.stringValue(platform, imperial, weightI);
        Ethnicity ethnicity = HPIterables.randomElement(Ethnicity.values());
        CoachPref coachPref = HPIterables.randomElement(CoachPref.values());
        Institute institute =HPIterables.randomElement(Institute.values());
                String selectedEth = ethnicity.stringValue(engine);
        String selectedCP = coachPref.stringValue(engine);
        String selectedIN=institute.stringValue(engine);
        TestSubscriber subscriber = CustomTestSubscriber.create();

        Flowable.concatArray(
            rxa_navigate(MODE, Screen.SPLASH, Screen.VALID_AGE),

            /* Select gender */
            rxa_selectGender(engine, Gender.MALE),
            rxa_selectGender(engine, Gender.FEMALE),

            /* Select metric unit system for height selection */
            rxa_randomInputs(engine, inputs),
            rxa_selectUnitSystemPicker(engine, ChoiceInput.HEIGHT, Height.CM),
            rxa_selectChoice(engine, heightM),
            rxa_confirmNumericChoice(engine),
            rxv_fieldHasValue(engine, ChoiceInput.HEIGHT, heightMStr),

            /* Select imperial unit system for height selection */
            rxa_selectUnitSystemPicker(engine, ChoiceInput.HEIGHT, Height.FT),
            rxa_selectChoice(engine, heightI),
            rxa_confirmNumericChoice(engine),
            rxv_fieldHasValue(engine, ChoiceInput.HEIGHT, heightIStr),

            /* Select metric unit system for weight selection */
            rxa_selectUnitSystemPicker(engine, ChoiceInput.WEIGHT, Weight.KG),
            rxa_selectChoice(engine, weightM),
            rxa_confirmNumericChoice(engine),
            rxv_fieldHasValue(engine, ChoiceInput.WEIGHT, weightMStr),

            /* Select imperial unit system for weight selection */
            rxa_selectUnitSystemPicker(engine, ChoiceInput.WEIGHT, Weight.LB),
            rxa_selectChoice(engine, weightI),
            rxa_confirmNumericChoice(engine),
            rxv_fieldHasValue(engine, ChoiceInput.WEIGHT, weightIStr),

            /* Select ethnicity */
            rxa_clickInput(engine, ChoiceInput.ETHNICITY),
            rxa_selectChoice(engine, ChoiceInput.ETHNICITY, selectedEth),
            rxa_confirmTextChoice(engine),

            /* Select coach preference */
            rxa_clickInput(engine, ChoiceInput.COACH_PREF),
            rxa_selectChoice(engine, ChoiceInput.COACH_PREF, selectedCP),
            rxa_confirmTextChoice(engine),

                /* Select Institute */
                rxa_clickInput(engine, ChoiceInput.INSTITUTE),
                rxa_selectChoice(engine, ChoiceInput.INSTITUTE, selectedIN),
                rxa_confirmTextChoice(engine)

        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#PERSONAL_INFO} contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility and interacting with each of them.
     * @param MODE {@link UserMode} instance.
     * @see Engine#isShowingPassword(WebElement)
     * @see Engine#togglePasswordMaskFn()
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInput(Engine, HMTextType)
     * @see #rxa_makeNextInputVisible(Engine)
     * @see #rxe_editField(Engine, HMInputType)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_personalInfo_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        PlatformType platform = ENGINE.platform();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO),
            rxv_personalInfoScreen(ENGINE, MODE),

            Flowable.fromIterable(MODE.personalInfo(platform))
                .concatMap(a -> Flowable.concatArray(
                    THIS.rxa_randomInput(ENGINE, a),
                    THIS.rxa_makeNextInputVisible(ENGINE)
                ))
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test builds upon
     * {@link UIPersonalInfoTestType#test_guarantorNeeded_requiresGuarantorInfo(UserMode)},
     * but also provides validations for {@link Screen#DASHBOARD}. It will
     * check {@link Screen#USE_APP_NOW} and {@link Screen#DASHBOARD_TUTORIAL}
     * as well.
     * @param mode {@link UserMode} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #guarantorSpecificUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_scrollToBottom(Engine)
     * @see #rxa_dashboardMode(Engine, DashboardMode)
     * @see #rxv_dashboardBMI(Engine)
     * @see #rxv_dashboardActivity(Engine, UserMode)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "guarantorSpecificUserModeProvider"
    )
    default void test_signUpNewAccount_shouldSucceed(@NotNull UserMode mode) {
        // Setup
        Engine<?> engine = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.DOB, Screen.DASHBOARD),
            rxa_dashboardMode(engine, DashboardMode.BMI),
            rxv_dashboardBMI(engine),
            rxa_dashboardMode(engine, DashboardMode.ACTIVITY),
            rxv_dashboardActivity(engine, mode)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates login with different default credentials that
     * correspond to each {@link UserMode}, and confirms that
     * {@link Screen#DASHBOARD} should appear differently depending on the
     * {@link UserMode}.
     * @param MODE {@link UserMode} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_scrollToBottom(Engine)
     * @see #rxa_dashboardMode(Engine, DashboardMode)
     * @see #rxv_dashboardBMI(Engine)
     * @see #rxv_dashboardActivity(Engine, UserMode)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_loginToDashboard_shouldSucceed(@NotNull final UserMode MODE) {
        // Setup
        Engine<?> engine = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(MODE, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD),
            rxa_dashboardMode(engine, DashboardMode.BMI),
            rxv_dashboardBMI(engine),
            rxa_dashboardMode(engine, DashboardMode.ACTIVITY),
            rxv_dashboardActivity(engine, MODE)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates the drawer found in {@link Screen#DASHBOARD}.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_toggleDrawer(Engine, boolean)
     * @see #rxv_drawer(Engine)
     * @see #rxv_isDrawerOpen(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_dashboardDrawer_isValidScreen() {
        // Setup
        Engine<?> engine = engine();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD),
            rxa_toggleDrawer(engine, true),
            rxv_drawer(engine),
            rxa_toggleDrawer(engine, false),

            rxv_isDrawerOpen(engine)
                .filter(HPBooleans::isFalse)
                .switchIfEmpty(engine.rxv_error())
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Validate {@link Screen#MEAL_ENTRY} and confirm that all
     * {@link org.openqa.selenium.WebElement} are present.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxv_mealLog(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logMeal_isValidScreen() {
        // Setup
        Engine<?> engine = engine();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.MEAL_ENTRY),
            rxv_mealLog(engine)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Validate {@link Screen#WEIGHT_VALUE} and confirm that all
     * {@link WebElement} are present.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_CSSValue(Engine, HMCSSInputType)
     * @see #rxv_weightEntry(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logWeightValue_isValidScreen() {
        // Setup
        Engine<?> engine = engine();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.WEIGHT_VALUE),
            rxv_CSSValue(engine, CSSInput.WEIGHT)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Validate {@link Screen#WEIGHT_ENTRY} and confirm that all
     * {@link WebElement} are present.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxv_weightEntry(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logWeightEntry_isValidScreen() {
        // Setup
        Engine<?> engine = engine();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.WEIGHT_ENTRY),
            rxv_weightEntry(engine)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Validate {@link Screen#ACTIVITY_VALUE} and confirm that all
     * {@link WebElement} are present.
     * @param mode {@link UserMode} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxv_activityEntry(Engine)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_logActivityValue_isValidScreen(@NotNull UserMode mode) {
        // Setup
        Engine<?> engine = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.ACTIVITY_VALUE),
            rxv_CSSValue(engine, CSSInput.ACTIVITY)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Validate {@link Screen#ACTIVITY_ENTRY} and confirm that all
     * {@link WebElement} are present.
     * @param mode {@link UserMode} instance.
     * @see #assertCorrectness(TestSubscriber)
     * @see #generalUserModeProvider()
     * @see #engine()                          
     * @see #rxv_activityEntry(Engine)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_logActivityEntry_isValidScreen(@NotNull UserMode mode) {
        // Setup
        Engine<?> engine = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.ACTIVITY_ENTRY),
            rxv_activityEntry(engine)
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Validate {@link Screen#SETTINGS} and confirm that all {@link WebElement}
     * are present.
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_toggleSetting(Engine, Setting)
     * @see #rxa_changeUnitSystem(Engine, UnitSystem)
     * @see #rxv_settings(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_settings_isValidScreen() {
        // Setup
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UnitSystem[] units = UnitSystem.values();
        UserMode mode = UserMode.defaultUserMode();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.concatArray(
            rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.SETTINGS),
            rxv_settings(ENGINE),
            rxa_toggleSetting(ENGINE, Setting.UNITS),
            Flowable.fromArray(units).concatMap(a -> THIS.rxa_changeUnitSystem(ENGINE, a))
        ).all(HPObjects::nonNull).toFlowable().subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
