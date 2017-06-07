package com.holmusk.SuperLeapQA.test.screen;

import com.holmusk.HMUITestKit.model.HMChoiceType;
import com.holmusk.HMUITestKit.model.HMInputType;
import com.holmusk.HMUITestKit.model.HMTextType;
import com.holmusk.HMUITestKit.model.UnitSystem;
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
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.collection.Zip;
import org.swiften.javautilities.number.NumberUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkitcomponents.platform.PlatformType;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by haipham on 6/4/17.
 */
public interface UIScreenValidationTestType extends
    UIBaseTestType,
    ForwardNavigationType,
    BackwardNavigationType
{
    /**
     * This test validates {@link Screen#WELCOME} by checking that all
     * {@link org.openqa.selenium.WebElement} are visible.
     * @see Screen#SPLASH
     * @see Screen#WELCOME
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_welcomeScreen(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_welcomeScreen_containsCorrectElements() {
        // Setup
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(UserMode.PARENT, Screen.SPLASH, Screen.WELCOME)
            .flatMap(a -> THIS.rxv_welcomeScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Check that {@link Screen#LOGIN} has valid
     * {@link org.openqa.selenium.WebElement}, by checking their visibility
     * and interacting with them.
     * @see Screen#SPLASH
     * @see Screen#LOGIN
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
        UserMode mode = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN)
            .flatMap(a -> THIS.rxv_loginScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Verify that {@link Screen#FORGOT_PASSWORD} has valid
     * {@link org.openqa.selenium.WebElement} by checking their visibility
     * and interacting with them.
     * @see Screen#SPLASH
     * @see Screen#FORGOT_PASSWORD
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxv_forgotPassword(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_forgotPassword_isValidScreen() {
        // Setup
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.FORGOT_PASSWORD)
            .flatMap(a -> THIS.rxv_forgotPassword(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that {@link Screen#REGISTER} contains the correct
     * {@link org.openqa.selenium.WebElement} by checking their visibility,
     * and then navigate back once to check {@link Screen#WELCOME}.
     * @see Screen#SPLASH
     * @see Screen#REGISTER
     * @see Screen#WELCOME
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_clickBackButton(Engine)
     * @see #rxv_registerScreen(Engine)
     * @see #rxv_welcomeScreen(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_registerScreen_isValidScreen() {
        // Setup
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.REGISTER)
            .flatMap(a -> THIS.rxv_registerScreen(ENGINE))

            /* Make sure the back button works */
            .flatMap(a -> THIS.rxa_clickBackButton(ENGINE))
            .flatMap(a -> THIS.rxv_welcomeScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that {@link Screen#DOB} has correct elements,
     * by checking that all {@link org.openqa.selenium.WebElement} are present
     * and back navigation shows {@link Screen#REGISTER}.
     * @param mode {@link UserMode} instance.
     * @see Engine#rxa_click(WebElement)
     * @see Engine#rxa_navigateBackOnce()
     * @see Engine#rxe_containsText(String...)
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#SPLASH
     * @see Screen#REGISTER
     * @see Screen#DOB
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
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.DOB)
            .flatMap(a -> Flowable.mergeArray(
                ENGINE.rxe_containsText("register_title_dateOfBirth"),
                ENGINE.rxe_containsText(
                    "parentSignUp_title_whatIsYourChild",
                    "teenSignUp_title_whatIsYour"
                )
            ))
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .flatMap(a -> THIS.rxa_openDoBPicker(ENGINE))
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS)
            .flatMap(a -> ENGINE.rxa_navigateBackOnce())
            .delay(generalDelay(ENGINE), TimeUnit.MILLISECONDS)
            .flatMap(a -> THIS.rxa_clickBackButton(ENGINE))
            .flatMap(a -> THIS.rxv_registerScreen(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks {@link Screen#DOB} dialog has the correct
     * elements, by verifying that all required
     * {@link org.openqa.selenium.WebElement} are present. It selects a random
     * {@link java.util.Date} with which to interact with the calendar/date
     * picker.
     * @param MODE {@link UserMode} instance.
     * @see NumberUtil#randomBetween(int, int)
     * @see Calendar#DAY_OF_MONTH
     * @see Calendar#MONTH
     * @see Calendar#YEAR
     * @see Screen#SPLASH
     * @see Screen#DOB
     * @see Screen#INVALID_AGE
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_openDoBPicker(Engine)
     * @see #rxa_selectDoB(Engine, Date)
     * @see #rxa_clickBackButton(Engine)
     * @see #rxv_DoBEditFieldHasDate(Engine, Date)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_DoBPickerDialog_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, NumberUtil.randomBetween(1, 28));
        calendar.set(Calendar.MONTH, NumberUtil.randomBetween(0, 11));
        calendar.set(Calendar.YEAR, NumberUtil.randomBetween(1970, 2000));
        final Date DATE = calendar.getTime();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.DOB)
            .flatMap(a -> THIS.rxa_openDoBPicker(ENGINE))
            .flatMap(a -> THIS.rxa_selectDoB(ENGINE, DATE))
            .flatMap(a -> THIS.rxa_confirmDoB(ENGINE))
            .flatMap(a -> THIS.rxa_clickBackButton(ENGINE))
            .flatMap(a -> THIS.rxv_DoBEditFieldHasDate(ENGINE, DATE))
            .subscribe(subscriber);

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
     * @see Screen#SPLASH
     * @see Screen#INVALID_AGE
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
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.INVALID_AGE)
            .flatMap(a -> THIS.rxa_confirmInvalidAgeInputs(ENGINE))
            .flatMap(a -> THIS.rxa_clickInput(ENGINE, TextInput.NAME))
            .subscribe(subscriber);

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
     * @see ChoiceInput#HEIGHT
     * @see ChoiceInput#WEIGHT
     * @see ChoiceInput#ETHNICITY
     * @see ChoiceInput#COACH_PREF
     * @see CoachPref#values()
     * @see CollectionUtil#randomElement(Object[])
     * @see Engine#platform()
     * @see Ethnicity#values()
     * @see Height#CM
     * @see Height#CM_DEC
     * @see Height#FT
     * @see Height#INCH
     * @see Height#random(PlatformType, UserMode, UnitSystem)
     * @see Height#stringValue(PlatformType, UnitSystem, List)
     * @see Weight#KG
     * @see Weight#KG_DEC
     * @see Weight#LB
     * @see Weight#LB_DEC
     * @see Weight#random(PlatformType, UserMode, UnitSystem)
     * @see Weight#stringValue(PlatformType, UnitSystem, List)
     * @see Screen#SPLASH
     * @see Screen#VALID_AGE
     * @see UnitSystem#IMPERIAL
     * @see UnitSystem#METRIC
     * @see UserMode#validAgeInfo(PlatformType)
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_clickInput(Engine, HMInputType)
     * @see #rxa_selectChoice(Engine, List)
     * @see #rxa_selectUnitSystemPicker(Engine, HMChoiceType, SLNumericChoiceType)
     * @see #rxa_confirmNumericChoice(Engine)
     * @see #rxv_hasValue(Engine, HMInputType, String)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_validAgeInputs_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UIScreenValidationTestType THIS = this;
        final Engine<?> E = engine();
        PlatformType p = E.platform();
        UnitSystem metric = UnitSystem.METRIC;
        UnitSystem imperial = UnitSystem.IMPERIAL;
        final List<HMTextType> INPUTS = MODE.validAgeInfo(p);
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
        final Ethnicity ETH = CollectionUtil.randomElement(Ethnicity.values());
        final CoachPref CP = CollectionUtil.randomElement(CoachPref.values());
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.VALID_AGE)

            /* Select metric unit system for height selection */
            .flatMap(a -> THIS.rxa_randomInputs(E, INPUTS))
            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_HEIGHT, Height.CM))

            /* Select height in metric */
            .flatMap(a -> THIS.rxa_selectChoice(E, HEIGHT_M))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))
            .flatMap(a -> THIS.rxv_hasValue(E, C_HEIGHT, HEIGHT_M_STR))

            /* Select imperial unit system for height selection */
            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_HEIGHT, Height.FT))

            /* Select height in imperial */
            .flatMap(a -> THIS.rxa_selectChoice(E, HEIGHT_I))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))
            .flatMap(a -> THIS.rxv_hasValue(E, C_HEIGHT, HEIGHT_I_STR))

            /* Select metric unit system for weight selection */
            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_WEIGHT, Weight.KG))

            /* Select weight in metric */
            .flatMap(a -> THIS.rxa_selectChoice(E, WEIGHT_M))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))
            .flatMap(a -> THIS.rxv_hasValue(E, C_WEIGHT, WEIGHT_M_STR))

            /* Select imperial unit system for weight selection */
            .flatMap(a -> THIS.rxa_selectUnitSystemPicker(E, C_WEIGHT, Weight.LB))

            /* Select weight in imperial */
            .flatMap(a -> THIS.rxa_selectChoice(E, WEIGHT_I))
            .flatMap(a -> THIS.rxa_confirmNumericChoice(E))
            .flatMap(a -> THIS.rxv_hasValue(E, C_WEIGHT, WEIGHT_I_STR))

            /* Select gender */
            .flatMap(a -> THIS.rxa_clickInput(E, Gender.MALE))
            .flatMap(a -> THIS.rxa_clickInput(E, Gender.FEMALE))

            /* Select ethnicity */
            .flatMap(a -> THIS.rxa_clickInput(E, C_ETH))
            .flatMap(a -> THIS.rxa_selectChoice(E, C_ETH, ETH.stringValue()))

            /* Select coach preference */
            .flatMap(a -> THIS.rxa_clickInput(E, C_COACH))
            .flatMap(a -> THIS.rxa_selectChoice(E, C_COACH, CP.stringValue()))
            .subscribe(subscriber);

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
     * @see Engine#platform()
     * @see Engine#rxa_toggleNextOrFinishInput(WebElement)
     * @see Engine#rxa_togglePasswordMask(WebElement)
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#SPLASH
     * @see Screen#PERSONAL_INFO
     * @see UserMode#personalInfo(PlatformType)
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #generalUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInput(Engine, HMTextType)
     * @see #rxa_makeNextInputVisible(Engine, WebElement)
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
        final PlatformType PLATFORM = ENGINE.platform();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO)
            .flatMap(a -> THIS.rxv_personalInfoScreen(ENGINE, MODE))
            .concatMapIterable(a -> MODE.personalInfo(PLATFORM))
            .ofType(HMTextType.class)
            .concatMap(a -> THIS.rxa_randomInput(ENGINE, a))
            .concatMap(a -> THIS.rxa_makeNextInputVisible(ENGINE, a))
            .all(ObjectUtil::nonNull)
            .toFlowable()
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test builds upon
     * {@link UIPersonalInfoTestType#test_guarantorNeeded_requiresParentInfo(UserMode)},
     * but also provides validations for {@link Screen#DASHBOARD}. It will
     * check {@link Screen#USE_APP_NOW} and {@link Screen#DASHBOARD_TUTORIAL}
     * as well.
     * @param MODE {@link UserMode} instance.
     * @see Screen#SPLASH
     * @see Screen#DOB
     * @see Screen#USE_APP_NOW
     * @see Screen#DASHBOARD_TUTORIAL
     * @see Screen#DASHBOARD
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #guarantorSpecificUserModeProvider()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_useAppNow(Engine)
     * @see #rxa_dismissDashboardTutorial(Engine)
     * @see #rxv_useAppNow(Engine)
     * @see #rxv_dashboardTutorial(Engine)
     */
    @SuppressWarnings("unchecked")
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "guarantorSpecificUserModeProvider"
    )
    default void test_signUpNewAccount_shouldSucceed(@NotNull final UserMode MODE) {
        // Setup
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.DOB, Screen.USE_APP_NOW)
            .flatMap(a -> THIS.rxv_useAppNow(ENGINE))
            .flatMap(a -> THIS.rxa_useAppNow(ENGINE))
            .flatMap(a -> THIS.rxv_dashboardTutorial(ENGINE))
            .flatMap(a -> THIS.rxa_dismissDashboardTutorial(ENGINE))
            .subscribe(subscriber);

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
     * @see DashboardMode#BMI
     * @see DashboardMode#ACTIVITY
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#DASHBOARD
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
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.LOGIN, Screen.DASHBOARD)
            .flatMap(a -> THIS.rxa_dashboardMode(ENGINE, DashboardMode.BMI))
            .flatMap(a -> THIS.rxv_dashboardBMI(ENGINE))
            .flatMap(a -> THIS.rxa_dashboardMode(ENGINE, DashboardMode.ACTIVITY))
            .flatMap(a -> THIS.rxv_dashboardActivity(ENGINE, MODE))
            .flatMap(a -> THIS.rxa_scrollToBottom(ENGINE))
            .flatMap(a -> THIS.rxv_cardListView(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * Validate {@link com.holmusk.SuperLeapQA.navigation.Screen#LOG_MEAL}
     * and confirm that all {@link org.openqa.selenium.WebElement} are present.
     * @see Screen#SPLASH
     * @see Screen#LOGIN
     * @see Screen#LOG_MEAL
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxv_mealLog(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_logMeal_isValidScreen() {
        // Setup
        final UIScreenValidationTestType THIS = this;
        final Engine<?> ENGINE = engine();
        UserMode mode = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(mode, Screen.SPLASH, Screen.LOGIN, Screen.LOG_MEAL)
            .flatMap(a -> THIS.rxv_mealLog(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
