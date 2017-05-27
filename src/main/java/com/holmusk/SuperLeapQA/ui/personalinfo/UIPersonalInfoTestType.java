package com.holmusk.SuperLeapQA.ui.personalinfo;

import com.holmusk.SuperLeapQA.model.TextInput;
import com.holmusk.SuperLeapQA.model.UserMode;
import com.holmusk.SuperLeapQA.model.type.SLInputType;
import com.holmusk.SuperLeapQA.model.type.SLTextType;
import com.holmusk.SuperLeapQA.navigation.Screen;
import com.holmusk.SuperLeapQA.ui.base.UIBaseTestType;
import com.holmusk.SuperLeapQA.util.GuarantorAware;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import org.swiften.javautilities.bool.BooleanUtil;
import org.swiften.javautilities.collection.CollectionUtil;
import org.swiften.javautilities.object.ObjectUtil;
import org.swiften.javautilities.rx.CustomTestSubscriber;
import org.swiften.javautilities.rx.RxUtil;
import org.swiften.xtestkit.android.AndroidEngine;
import org.swiften.xtestkit.base.Engine;
import org.swiften.xtestkit.base.type.PlatformType;
import org.swiften.xtestkit.model.InputType;
import org.swiften.xtestkit.model.TextInputType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by haipham on 23/5/17.
 */
public interface UIPersonalInfoTestType extends UIBaseTestType, PersonalInfoActionType {
    /**
     * This {@link DataProvider} is used to check for either/or input
     * requirement when the user is entering guarantor information.
     * @return {@link Iterator} instance.
     */
    @NotNull
    @DataProvider
    static Iterator<Object[]> parentPersonalInfoProvider() {
        return Arrays.asList(
            new Object[] {
                Arrays.asList(TextInput.PARENT_NAME, TextInput.PARENT_EMAIL)
            },
            new Object[] {
                Arrays.asList(TextInput.PARENT_NAME, TextInput.PARENT_MOBILE)
            }
        ).iterator();
    }

    /**
     * This test validates that {@link Screen#PERSONAL_INFO} contains the
     * correct {@link org.openqa.selenium.WebElement} by verifying their
     * visibility and interacting with each of them.
     * @param MODE {@link UserMode} instance.
     * @see Screen#PERSONAL_INFO
     * @see Engine#rx_toggleNextOrFinishInput(WebElement)
     * @see Engine#rxa_togglePasswordMask(WebElement)
     * @see Engine#isShowingPassword(WebElement)
     * @see ObjectUtil#nonNull(Object)
     * @see UserMode#personalInfo(PlatformType)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #generalUserModeProvider()
     * @see #rxa_randomInput(Engine, SLTextType)
     * @see #rxe_editField(Engine, SLInputType)
     * @see #rxa_makeNextInputVisible(Engine, WebElement)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider",
        groups = "ValidateScreen"
    )
    default void test_personalInfoScreen_isValidScreen(@NotNull final UserMode MODE) {
        // Setup
        final UIPersonalInfoTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final PlatformType PLATFORM = ENGINE.platform();
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO)
            .flatMap(a -> THIS.rxv_personalInfoScreen(ENGINE, MODE))
            .concatMapIterable(a -> MODE.personalInfo(PLATFORM))
            .ofType(SLTextType.class)
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
     * This test validates that the password mask, when clicks, reveals the
     * password. It is only applicable to
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID} since on
     * {@link org.swiften.xtestkit.mobile.Platform#IOS} there is no way to
     * reveal the password content.
     * @see Engine#rxa_togglePasswordMask(WebElement)
     * @see Engine#isShowingPassword(WebElement)
     * @see RxUtil#error()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInput(Engine, SLTextType)
     * @see #rxa_confirmTextInput(Engine)
     */
    @Test
    @SuppressWarnings("unchecked")
    default void test_togglePasswordMask_shouldWork() {
        // Setup
        final UIPersonalInfoTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final UserMode MODE = UserMode.PARENT;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.just(ENGINE)
            .filter(a -> a instanceof AndroidEngine)
            .flatMap(a -> THIS.rxa_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO))
            .flatMap(a -> THIS.rxa_randomInput(ENGINE, TextInput.PASSWORD))
            .flatMap(a -> THIS.rxa_confirmTextInput(ENGINE)
                .flatMap(b -> ENGINE.rxa_togglePasswordMask(a))
                .filter(ENGINE::isShowingPassword)
                .switchIfEmpty(RxUtil.error())
            )
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test confirms that when the user clicks on the TOC and opens up
     * the Web browser, {@link Screen#PERSONAL_INFO} inputs are saved and then
     * restored when the user gets back to the app. This is more relevant for
     * {@link org.swiften.xtestkit.mobile.Platform#ANDROID}.
     * @see org.swiften.xtestkit.mobile.Platform#ANDROID
     * @see BooleanUtil#isTrue(boolean)
     * @see ObjectUtil#nonNull(Object)
     * @see Screen#PERSONAL_INFO
     * @see UserMode#personalInfo(PlatformType)
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_input(Engine, SLInputType, String)
     * @see #rxa_openTOC(Engine)
     * @see #rxa_makeNextInputVisible(Engine, WebElement)
     * @see #rxv_hasValue(Engine, SLInputType, String)
     */
    @Test
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    default void test_leavePersonalInfo_shouldSaveState() {
        // Setup
        final UIPersonalInfoTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final PlatformType PLATFORM = ENGINE.platform();
        final UserMode MODE = UserMode.PARENT;
        final Map<String,String> INPUTS = new HashMap<>();
        List<SLTextType> info = MODE.personalInfo(PLATFORM);

        final List<SLTextType> TEXT_INFO = info.stream()
            .filter(TextInputType.class::isInstance)
            .map(SLTextType.class::cast)
            .collect(Collectors.toList());

        TEXT_INFO.forEach(a -> INPUTS.put(a.toString(), a.randomInput()));
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        Flowable.just(ENGINE)
            .filter(a -> a instanceof AndroidEngine)
            .flatMap(a -> THIS.rxa_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO))
            .concatMapIterable(a -> TEXT_INFO)
            .concatMap(a -> THIS.rxa_input(ENGINE, a, INPUTS.get(a.toString())))
            .concatMap(a -> THIS.rxa_makeNextInputVisible(ENGINE, a))

            /* We use toList here because we want to intercept the empty
             * event as well */
            .toList()
            .filter(CollectionUtil::isNotEmpty)
            .toFlowable()

            /* We need to unmask the password field so that later its text
             * can be verified. Otherwise, the text returned will be empty */
            .flatMap(a -> THIS.rxe_editField(ENGINE, TextInput.PASSWORD))
            .flatMap(ENGINE::rxa_togglePasswordMask)
            .delay(generalDelay(), TimeUnit.MILLISECONDS)

            .flatMap(a -> THIS.rxa_openTOC(ENGINE))
            .delay(webViewDelay(), TimeUnit.MILLISECONDS)
            .flatMap(a -> ENGINE.rxa_navigateBackOnce())
            .flatMapIterable(a -> TEXT_INFO)
            .flatMap(a -> THIS.rxv_hasValue(ENGINE, a, INPUTS.get(a.toString())))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that the TOC checkbox has to be ticked before the
     * user continues any further. The check happens in
     * {@link Screen#PERSONAL_INFO}.
     * @param MODE {@link UserMode} instance.
     * @see Engine#rxa_hideKeyboard()
     * @see Screen#PERSONAL_INFO
     * @see UserMode#personalInfo(PlatformType)
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInputs(Engine, List)
     * @see #rxa_confirmPersonalInfo(Engine)
     * @see #rxv_personalInfoScreen(Engine, UserMode)
     * @see #assertCorrectness(TestSubscriber)
     * @see #engine()
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "generalUserModeProvider"
    )
    default void test_requireTOCAccepted_toProceed(@NotNull final UserMode MODE) {
        // Setup
        final UIPersonalInfoTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final PlatformType PLATFORM = ENGINE.platform();
        final List<SLTextType> INFO = MODE.personalInfo(PLATFORM);
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.PERSONAL_INFO)
            .flatMap(a -> THIS.rxa_randomInputs(ENGINE, INFO))
            .flatMap(a -> THIS.rxa_confirmPersonalInfo(ENGINE))
            .delay(2000, TimeUnit.MILLISECONDS)
            .flatMap(a -> THIS.rxv_personalInfoScreen(ENGINE, MODE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test validates that the {@link Screen#GUARANTOR_INFO} for
     * parents/guarantors should only require either
     * {@link TextInput#PARENT_MOBILE} or {@link TextInput#PARENT_EMAIL}.
     * This test is only applicable for {@link UserMode#TEEN_U18}, so we use
     * {@link DataProvider} that provides {@link InputType}.
     * @param INPUTS {@link List} of {@link InputType}.
     * @see Screen#GUARANTOR_INFO
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #rxa_randomInputs(Engine, List)
     * @see #rxa_confirmGuarantorInfo(Engine, UserMode)
     * @see #rxe_progressBar(Engine)
     * @see #parentPersonalInfoProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = false)
    @Test(dataProvider = "parentPersonalInfoProvider")
    default void test_parentInfo_phoneOrEmail(@NotNull final List<SLTextType> INPUTS) {
        // Setup
        final UIPersonalInfoTestType THIS = this;
        final Engine<?> ENGINE = engine();
        final UserMode MODE = UserMode.TEEN_U18;
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        rxa_navigate(MODE, Screen.SPLASH, Screen.GUARANTOR_INFO)
            .flatMap(a -> THIS.rxa_randomInputs(ENGINE, INPUTS))
            .flatMap(a -> THIS.rxa_confirmGuarantorInfo(ENGINE, MODE))

            /* If all inputs are valid, the progress bar should be visible
             * to indicate data being processed */
            .flatMap(a -> THIS.rxe_progressBar(ENGINE))
            .subscribe(subscriber);

        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }

    /**
     * This test checks that {@link UserMode#TEEN_U18} will see
     * {@link Screen#GUARANTOR_INFO}, while {@link UserMode#TEEN_A18} will
     * not. It uses a custom {@link DataProvider} that provides only
     * {@link UserMode#TEEN_U18} and {@link UserMode#TEEN_A18}.
     * @param MODE {@link UserMode} instance.
     * @see Screen#GUARANTOR_INFO
     * @see #engine()
     * @see #rxa_navigate(UserMode, Screen...)
     * @see #guarantorSpecificUserModeProvider()
     * @see #assertCorrectness(TestSubscriber)
     */
    @SuppressWarnings("unchecked")
    @GuarantorAware(value = true)
    @Test(
        dataProviderClass = UIBaseTestType.class,
        dataProvider = "guarantorSpecificUserModeProvider"
    )
    default void test_guarantorNeeded_requiresParentInfo(@NotNull final UserMode MODE) {
        // Setup
        TestSubscriber subscriber = CustomTestSubscriber.create();

        // When
        /* During the tests, if the current user requires a guarantor (i.e
         * below 18 years-old), we expect the parent information screen to
         * be present */
        rxa_navigate(MODE, Screen.SPLASH, Screen.USE_APP_NOW).subscribe(subscriber);
        subscriber.awaitTerminalEvent();

        // Then
        assertCorrectness(subscriber);
    }
}
